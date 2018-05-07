package opengl;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;
import com.jogamp.opengl.GL2;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.glu.GLU;
import java.io.File;
import java.io.IOException;
import opengl.part.LoadTextura;
import opengl.part.SistemaParticulas;

/**
 *
 * @author Leonardo Villeth
 */
public class MeuOpenGL implements GLEventListener{

    private GLU glu;
    public static GL2 gl2;
    
    SistemaParticulas sp;
    int[] textura_particula;    
    
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();         // get the OpenGL graphics context                
        glu = GLU.createGLU(gl);                    // get GL Utilities        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);    // set background (clear) color
        gl.glClearDepth(1.0f);                    // set clear depth value to farthest
        //gl.glEnable(GL_DEPTH_TEST);               // enables depth testing
        //gl.glDepthFunc(GL_LEQUAL);                     // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH);                     // blends colors nicely, and smoothes out lighting                     
        
        MeuOpenGL.gl2 = drawable.getGL().getGL2();
        
        
        // Testando
        gl.glEnable(GL_BLEND); //Enable alpha blending
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); //Set the blend function
        File alpha = new File("img/circlealpha.bmp");
        File rgb = new File("img/circle.bmp");
        try {            
            textura_particula = LoadTextura.loadTexturaAlpha(rgb, alpha);            
        } catch (IOException ex) {
            System.err.println("Erro carregando textura");
        }
        
        float step = 1.0f/30.0f; //30fps
        //float step = 0.01f;        
        sp = new SistemaParticulas(textura_particula, 100, step);
        
        /***********************/
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        
    }        
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();                      
        
        
        /*/Desenha um triangulo vermelho na tela
        gl.glBegin(GL_TRIANGLES); 
            // Cor a ser usada
            gl.glColor3f(1f,0f,0f);
            //Vertices do triangulo
            gl.glVertex3f(0.0f -0.5f, 0.0f, -5.0f); 
            gl.glVertex3f(0.5f -0.5f, 1.0f, -5.0f); 
            gl.glVertex3f(1.0f -0.5f, 0.0f, -5.0f );                        
        gl.glEnd();      
        */
        
        // Testa o sistema de particulas
        gl.glPushMatrix();            
            sp.draw();
            sp.step();
        gl.glPopMatrix();
        
        /* Desenha uma particula na tela com textura Alpha
        gl.glEnable(GL_TEXTURE_2D);
        gl.glBindTexture(GL_TEXTURE_2D, textura_particula[0]);
        float z = -5.0f;
        gl.glColor4f(1f,1f,1f,1f);
        gl.glBegin(GL_TRIANGLE_STRIP); // Build Quad From A Triangle Strip
                gl.glTexCoord2d(1, 1);
                gl.glVertex3f(0.5f, 0.5f, z); // Top Right
                
                gl.glTexCoord2d(0, 1);                
                gl.glVertex3f(0f, 0.5f, z); // Top Left
                
                gl.glTexCoord2d(1, 0);
                gl.glVertex3f(0.5f, 0f, z); // Bottom Right
                
                gl.glTexCoord2d(0, 0);
                gl.glVertex3f(0f, 0f, z); // Bottom Left
        gl.glEnd();        
        gl.glDisable(GL_TEXTURE_2D);
        /******************************/
        
        drawable.swapBuffers();        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context              
        
        if (height == 0) // prevent divide by zero
            height = 1;   
        float aspect = (float)width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);        

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 1.0, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset  
    }
    
}
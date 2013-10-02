package basiccube;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class BasicCube4 {
	
	int numCorners = 8;
	 
	FloatBuffer matSpecular;
	FloatBuffer whiteLight;
	FloatBuffer lightPosition;
	FloatBuffer lModeAmbient;
	
	//Eye location cords
	float eyex = 0f, eyey=0f, eyez=5f;
	float znear = 4f, zfar = 60f;
	//Cube Cords
	Vector3f[] corners = new Vector3f[numCorners];
	
	// Screen dimensions
	//SVGA 800x600
	// XGA 1024x768
	int WIDTH = 1024, HEIGHT = 768;

	float x = 400.0f, y = 300.0f;
	
	/* object vertices */
	float[][]vertex = { {x-100, y-100}, {x+100, y-100},{x+100, y+100}, {x-100, y+100}};
	
	/* angle to rotate by */
	float rotation = 20.0f;
	
	//take at last frame
	long lastFrame;
	
	//frames per second
	int fps;
	
	//last fps time
	long lastFPS;
	
	public void initCube(){
		corners[0] = new Vector3f(-0.5f, -0.5f,  0.5f);
		corners[1] = new Vector3f(-0.5f,  0.5f,  0.5f);
		corners[2] = new Vector3f( 0.5f,  0.5f,  0.5f);
		corners[3] = new Vector3f( 0.5f, -0.5f,  0.5f);
		corners[4] = new Vector3f(-0.5f, -0.5f, -0.5f);
		corners[5] = new Vector3f(-0.5f,  0.5f, -0.5f);
		corners[6] = new Vector3f( 0.5f,  0.5f, -0.5f);
		corners[7] = new Vector3f( 0.5f, -0.5f, -0.5f);
	}
	
	/**
	 * Get the accurate time system
	 * 
	 */
	public long getTime(){
		return(Sys.getTime() * 2000 / Sys.getTimerResolution());
	}
	
	/**
	 * Calculate how many milliseconds has elapsed since last frame
	 */
	
	public int getDelta(){
		long time = getTime();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	public void start(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		//Display.setTitle("The Rotating Cube");
		Display.setResizable(true);
		//Init GL
		initGL();
		getDelta(); //initialize the last frame
		lastFPS = getTime(); //Initialize FPS timer
		
		initCube();
		
		while(!Display.isCloseRequested()){
			int delta = getDelta();
			
			update(delta);
			renderGL();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	public void initGL(){
		try{
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Keyboard.enableRepeatEvents(true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspect = WIDTH / (float) HEIGHT;
		GLU.gluPerspective(60f, aspect, znear, zfar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		//GL11.glOrtho(-1, 1, -1, 1, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		// locate the eye
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public void update(int delta){
		//Computer new coordinate for the object based on time elapsed
		rotation += 0.015f * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) eyez -= 0.035f;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) eyez += 0.035f;

		updateFPS();
	}
	
	private void initLightArrays() {
        // TODO Auto-generated method stub
        //no constructor, not handled by JVM
        //malloc, memory management done outside of java
        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
       
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(1.0f).put(1.0f).put(1.0f).flip();
       
        lModeAmbient = BufferUtils.createFloatBuffer(4);
        lModeAmbient.put(0.2f).put(0.2f).put(0.2f).put(0.1f).flip();
       

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
       
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecular);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 60.0f);
       
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, whiteLight);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, whiteLight);
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, lModeAmbient);
       
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable((GL11.GL_COLOR_MATERIAL));
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);

}
	
	public void renderGL(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspect = WIDTH / (float) HEIGHT;
		GLU.gluPerspective(60f, aspect, znear, zfar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		//Enable Z buffer Algorithm
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		
		for(int i = -3; i < 4; i++){
		//Draw the square
		
		GL11.glPushMatrix();
		GL11.glTranslatef(2f*i, 0f, 0f);
		GL11.glRotatef(rotation, 1f, 0.5f, 1f);
		
		renderCube();

		GL11.glPopMatrix();
		}
		
		/*
		for(int i = -3; i < 4; i++){
			//Draw the square
			
			GL11.glPushMatrix();
			GL11.glTranslatef(2f*i, 2f*i, 0f);
			GL11.glRotatef(rotation, 1f, 0.5f, 1f);
			
			renderCube();

			GL11.glPopMatrix();
			}
		*/
	}
	
	public void renderCube(){
		GL11.glBegin(GL11.GL_QUADS);
		//Face 1
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glVertex3f(corners[0].x, corners[0].y, corners[0].z);
		GL11.glVertex3f(corners[1].x, corners[1].y, corners[1].z);
		GL11.glVertex3f(corners[2].x, corners[2].y, corners[2].z);
		GL11.glVertex3f(corners[3].x, corners[3].y, corners[3].z);
		
		//Face 2
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		GL11.glVertex3f(corners[0].x, corners[0].y, corners[0].z);
		GL11.glVertex3f(corners[1].x, corners[1].y, corners[1].z);
		GL11.glVertex3f(corners[5].x, corners[5].y, corners[5].z);
		GL11.glVertex3f(corners[4].x, corners[4].y, corners[4].z);
		
		//Face 3
		GL11.glColor3f(0.0f, 0.0f, 1.0f);
		GL11.glVertex3f(corners[5].x, corners[5].y, corners[5].z);
		GL11.glVertex3f(corners[6].x, corners[6].y, corners[6].z);
		GL11.glVertex3f(corners[7].x, corners[7].y, corners[7].z);
		GL11.glVertex3f(corners[4].x, corners[4].y, corners[4].z);
		
		//Face 4
		GL11.glColor3f(0.3f, 0.5f, 0.8f);
		GL11.glVertex3f(corners[0].x, corners[0].y, corners[0].z);
		GL11.glVertex3f(corners[4].x, corners[4].y, corners[4].z);
		GL11.glVertex3f(corners[7].x, corners[7].y, corners[7].z);
		GL11.glVertex3f(corners[3].x, corners[3].y, corners[3].z);
		
		//Face 5
		GL11.glColor3f(0.1f, 0.8f, 0.3f);
		GL11.glVertex3f(corners[1].x, corners[1].y, corners[1].z);
		GL11.glVertex3f(corners[5].x, corners[5].y, corners[5].z);
		GL11.glVertex3f(corners[6].x, corners[6].y, corners[6].z);
		GL11.glVertex3f(corners[2].x, corners[2].y, corners[2].z);
		
		GL11.glEnd();
		
	}
	
	public void updateFPS(){
		if (getTime() - lastFPS > 1000){
			Display.setTitle("The Rotating Cube - " + "FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	public void rotate(double theta){
		for(int i = 0; i < vertex.length; i++){
			float tapx = vertex[i][0];
			float tapy = vertex[i][1];
			tapx -= x;
			tapy -= y;
			
			vertex[i][0] = (float)(tapx * Math.cos(theta * Math.PI/180)
					- tapy * Math.sin(theta * Math.PI/180));
			vertex[i][1] = (float)(tapx * Math.sin(theta * Math.PI/180)
					+ tapy * Math.cos(theta * Math.PI/180));
			
			vertex[i][0] +=x;
			vertex[i][1] +=y;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicCube4 cube = new BasicCube4();
		cube.start();
	}

}

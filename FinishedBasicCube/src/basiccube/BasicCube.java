package basiccube;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class BasicCube {
	
	int numCorners = 8;
	//Cube Cords
	Vector3f[] corners = new Vector3f[numCorners];
	
	int WIDTH = 600, HEIGHT = 600;

	float x = 400.0f, y = 300.0f;
	
	/* object vertices */
	float[][]vertex = { {x-100, y-100}, {x+100, y-100},{x+100, y+100}, {x-100, y+100}};
	
	/* angle to rotate by */
	float rotation = 0.0f;
	
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
		return(Sys.getTime() * 200 / Sys.getTimerResolution());
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
		GL11.glOrtho(-1, 1, -1, 1, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public void update(int delta){
		//Computer new coordinate for the object based on time elapsed
		rotation += 0.15f * delta;
		



		updateFPS();
	}
	
	public void renderGL(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		//Enable Z buffer Algorithm
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		//Draw the square
		GL11.glPushMatrix();
		GL11.glRotatef(rotation, 1f, 1f, 1f);
		
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
		GL11.glPopMatrix();

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
		BasicCube cube = new BasicCube();
		cube.start();
	}

}

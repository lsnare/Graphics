package cube;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class BasicCube {

	int numCorners = 8;
	
	Vector3f[] corners = new Vector3f[numCorners];
	int WIDTH = 600;
	int HEIGHT = 600;
	
	float x = 400.0f, y = 300.0f; 
	float[][] vertex = { {x-100, y-100}, {x+100, y-100}, {x+100, y+100}, {x-100, y+100} }; 
	float rotation = 0.0f; 
	long lastFrame; 
	int fps; 
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
	
	public long getTime() { 
		return(Sys.getTime()*1000/Sys.getTimerResolution()); } 
	
	public int getDelta() { 
		long time = getTime(); int delta = (int)(time - lastFrame); 
		lastFrame = time; return delta; } 
	
	public void start() { 
		try { Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT)); 
		Display.create(); 
		} catch(LWJGLException e) { 
			e.printStackTrace(); 
			System.exit(0); 
		} 
		
		
		initGL(); 
		initCube();
		getDelta(); 
		lastFPS = getTime(); 
		
		while(!Display.isCloseRequested()) { 
			int delta = getDelta(); 
			
			update(delta); 
			renderGL(); 
			
			Display.update(); 
			Display.sync(60); 
		} 
		Display.destroy(); 
	} 
	
	
	public void renderGL() { //Clears the screen.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //R, G, B, and Alpha. 
		GL11.glPushMatrix();
		GL11.glRotatef(rotation, 1f, 1f, 1f);
		
		GL11.glBegin(GL11.GL_QUADS); 
			
		//face one
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glVertex3f(corners[0].x, corners[0].y, corners[0].z);
		GL11.glVertex3f(corners[1].x, corners[1].y, corners[1].z);
		GL11.glVertex3f(corners[2].x, corners[2].y, corners[2].z);
		GL11.glVertex3f(corners[3].x, corners[3].y, corners[3].z);
		
		//face two
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		GL11.glVertex3f(corners[0].x, corners[0].y, corners[0].z);
		GL11.glVertex3f(corners[1].x, corners[1].y, corners[1].z);
		GL11.glVertex3f(corners[5].x, corners[5].y, corners[5].z);
		GL11.glVertex3f(corners[4].x, corners[4].y, corners[4].z);
		
		//face three
		GL11.glColor3f(0.0f, 0.0f, 1.0f);
		GL11.glVertex3f(corners[5].x, corners[5].y, corners[5].z);
		GL11.glVertex3f(corners[6].x, corners[6].y, corners[6].z);
		GL11.glVertex3f(corners[7].x, corners[7].y, corners[7].z);
		GL11.glVertex3f(corners[4].x, corners[4].y, corners[4].z);
		
		//face four
		GL11.glColor3f(0.3f, 0.5f, 0.8f);
		GL11.glVertex3f(corners[0].x, corners[0].y, corners[0].z);
		GL11.glVertex3f(corners[4].x, corners[4].y, corners[4].z);
		GL11.glVertex3f(corners[7].x, corners[7].y, corners[7].z);
		GL11.glVertex3f(corners[3].x, corners[3].y, corners[3].z);
		
		GL11.glEnd(); 
		GL11.glPopMatrix();
	} 
	
	public void initGL() { 
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Keyboard.enableRepeatEvents(true);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION); 
		GL11.glLoadIdentity(); 
		GL11.glOrtho(-1, 1, -1, 1, 1, -1); 
		GL11.glMatrixMode(GL11.GL_MODELVIEW); 
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	} 
	
	public void update(int delta) { 
		rotation +=0.015f * delta;
		updateFPS();
	} 
	
	public void updateFPS() { 
		if(getTime() - lastFPS > 1000) { 
			Display.setTitle("FPS: " + fps); 
			fps = 0; lastFPS += 1000; } 
		fps++; } 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicCube cube = new BasicCube();
		cube.start();
	}

}

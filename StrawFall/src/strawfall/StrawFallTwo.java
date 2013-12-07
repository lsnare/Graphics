/*********************************
 * Author: Lucian Snare 		 *
 * Date: 9/20/13        		 *
 * 								 *
 * A simple program to model the *
 * fall path of a piece of straw *
 *********************************/

package strawfall;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.XRandR.Screen;
import org.lwjgl.util.vector.Vector3f;

public class StrawFallTwo {
	
	public Vector3f red = new Vector3f(1.0f,0.0f,0.0f);
	public Vector3f blue = new Vector3f(0.0f,1.0f,0.0f);
	public Vector3f green = new Vector3f(0.0f,0.0f,1.0f);
	
	public Vector3f[] baseColor = {red,green,blue};
	
	float x = 0;
	float y = Display.getHeight();
	int numlines = 100;
	int dX;
	int dY;
	int delta;
	int xx = 0;
	int yy = 0;
	
	public void show(){
	
		try {
			Display.setDisplayMode(new DisplayMode(1600,1000));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//initialize OpenGL
		initGL();
		x = 0;
		y = Display.getHeight();
		dX = Display.getWidth()/numlines;
		dY = Display.getHeight()/numlines;
		
		while(!Display.isCloseRequested()){
			renderGL();
			update(delta);
			Display.update();
		}
		Display.destroy();	
		
	}

	void renderGL(){
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //R, G, B, and Alpha. 
		GL11.glViewport(xx, yy, (int)Display.getWidth()*10, (int)Display.getHeight()*10); //great for scaling
		GL11.glColor3f(0, 1, 0);
		GL11.glBegin(GL11.GL_LINES); 
		
		//initial line
		GL11.glVertex2f(0,Display.getHeight());
		GL11.glVertex2f(0,0);
		
		
		//generate the bottom half of the straw fall
		for(int i = 1; i < numlines; i++){
			GL11.glColor3f(baseColor[i%3].x, baseColor[i%3].y, baseColor[i%3].z);
			GL11.glVertex2f(0,y-i*dY);
			GL11.glVertex2f(x+i*dX,0);
		}

		//top line
		GL11.glVertex2f(0,y);
		GL11.glVertex2f(Display.getWidth(),y);
		
		//generate the top half of the straw fall
		for(int i = 0; i < numlines; i++){
			GL11.glColor3f(baseColor[i%3].x, baseColor[i%3].y, baseColor[i%3].z);
			GL11.glVertex2f(x+i*dX, y);
			GL11.glVertex2f(Display.getWidth(), y-i*dY);//problem here
		}
		
		GL11.glEnd(); 
		Display.update();
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
		GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1); 
		GL11.glMatrixMode(GL11.GL_MODELVIEW); 
		} 
	
	public void update(int delta){
		delta += 1;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) xx += delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) xx -= delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) yy -= delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) yy += delta;

	}
	
	public static void main(String[] args) {
		StrawFallTwo straw = new StrawFallTwo();
		straw.show();
	}

}

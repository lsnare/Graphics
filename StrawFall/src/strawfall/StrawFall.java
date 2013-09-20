/*********************************
 * Author: Lucian Snare 		 *
 * Date: 9/20/13        		 *
 * 								 *
 * A simple program to model the *
 * fall path of a piece of straw *
 *********************************/

package strawfall;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class StrawFall {
	
	public Vector3f red = new Vector3f(1.0f,0.0f,0.0f);
	public Vector3f blue = new Vector3f(0.0f,1.0f,0.0f);
	public Vector3f green = new Vector3f(0.0f,0.0f,1.0f);
	
	public Vector3f[] baseColor = {red,green,blue};
	
	float x = 0;
	float y = Display.getHeight();
	
	public void show(){
	
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//initialize OpenGL
		initGL();
		x = 0;
		y = Display.getHeight();
		
		while(!Display.isCloseRequested()){
			renderGL();
			Display.update();
		}
		Display.destroy();	
		
	}

	void renderGL(){
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //R, G, B, and Alpha. 
		GL11.glColor3f(0, 1, 0);
		GL11.glBegin(GL11.GL_LINES); 
		
		//initial line
		GL11.glVertex2f(0,Display.getHeight());
		GL11.glVertex2f(0,0);
		
		int numlines = 50;
		int dX = Display.getWidth()/numlines;
		int dY = Display.getHeight()/numlines;
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
			GL11.glVertex2f(Display.getWidth(), y-i*dY);
		}
		
		GL11.glEnd(); 
		Display.update();
		} 
	
	public void initGL() { 
		GL11.glMatrixMode(GL11.GL_PROJECTION); 
		GL11.glLoadIdentity(); 
		GL11.glOrtho(0, 800, 0, 600, 1, -1); 
		GL11.glMatrixMode(GL11.GL_MODELVIEW); 
		} 
	
	public static void main(String[] args) {
		StrawFall straw = new StrawFall();
		straw.show();
	}

}

package displaysample;

import org.lwjgl.LWJGLException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class BasicShape {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicShape display = new BasicShape();
		display.show();
	}

	private void show() {
		// TODO Auto-generated method stub
		try{
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		}catch(LWJGLException e){
			e.getStackTrace();
			System.exit(0);
		}
		

		GL11.glMatrixMode(GL11.GL_PROJECTION); //applies to projection methods
		GL11.glLoadIdentity(); //clears settings
		GL11.glOrtho(0, 800, 0, 600, 1, -1); //orthographic projection
		GL11.glMatrixMode(GL11.GL_MODELVIEW); 
		
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//take repeated events when key is held down
		Keyboard.enableRepeatEvents(true);
		while (!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT); //clear buffers
			GL11.glColor3f(1.0f, 0.0f, 0.0f); 
			GL11.glBegin(GL11.GL_QUADS); //draw a quad out of following vertices
			GL11.glVertex2f(Mouse.getX(), Mouse.getY());
			GL11.glVertex2f(Mouse.getX()+100, Mouse.getY());
			GL11.glVertex2f(Mouse.getX()+100, Mouse.getY()+100);
			GL11.glVertex2f(Mouse.getX(), Mouse.getY()+100);
			GL11.glEnd();
			
			pullInput();
			

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		while (!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glColor3f(0.5f, 0.5f, 0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(100, 100);
			GL11.glVertex2f(100+200, 100);
			GL11.glVertex2f(100+200, 100+200);
			GL11.glVertex2f(100, 100+200);
			GL11.glEnd();
			

			Display.update();
		}
		
		Display.destroy();
		}
	}


	private void pullInput() {
		// TODO Auto-generated method stub
		if (Mouse.isButtonDown(1)){
			int x = Mouse.getX();
			int y = Mouse.getY();
			
			System.out.println("("+x+", "+y+")");
		}
		
		while(Keyboard.next()){
		if(Keyboard.getEventKeyState()){
			if(Keyboard.getEventKey() == Keyboard.KEY_W){
				System.out.println("W was pressed");
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_A){
				System.out.println("A was pressed");
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_S){
				System.out.println("S was pressed");
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_D){
				System.out.println("D was pressed");
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				System.exit(0);
			}
		}
		}
	}


}

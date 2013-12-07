package displaysample;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.lwjgl.opengl.Drawable;


public class DisplaySample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DisplaySample display = new DisplaySample();
		display.show();
	}

	private void show() {
		
		
		try{
			Display.setDisplayMode(new DisplayMode(800, 600)); //graphics card may not support some sizes
			Display.create();
	
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		//Initialize all OpenGL states.
		//OpenGL is a state machine.
		
		while(!Display.isCloseRequested()){
			//Render OpenGL images
			Display.update();//clear "dirty flag", keep picture consistent with buffer
			
		}
		
		Display.destroy();
	}

}

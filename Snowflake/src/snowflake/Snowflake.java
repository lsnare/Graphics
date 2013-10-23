package snowflake;

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Snowflake {
	

	Display display = new Display(); //SWT display
	Shell shell; 
	
	Composite comp;
	GLData data = new GLData();
	GLCanvas canvas;//child of display used for drawing

	long lastFrame;
	
	int fps;
	
	long lastFPS;
	
	float eyex = 0, eyey = 0, eyez = 2;
	float znear= 0.1f, zfar = 1f;
	
	private int numTimesToSubdivide = 5;
	private int numFlakes = (int)Math.pow(4, numTimesToSubdivide);
	private int numVertices = 3*numFlakes;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

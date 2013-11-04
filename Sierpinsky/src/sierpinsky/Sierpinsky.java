package sierpinsky;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class Sierpinsky extends SkelGL{

	
	
	@Override
	protected void initGL() {
		try {
			GLContext.useContext(canvas);

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		float aspect = WIDTH / (float) HEIGHT;
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);

		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClearDepth(1.0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
	}

	@Override
	protected void update(int delta) {
		updateFPS();
	}

	@Override
	protected void resetGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		Rectangle bounds = canvas.getBounds();
		float aspect = WIDTH / (float) HEIGHT;

		GL11.glViewport(0, 0, bounds.width, bounds.height);
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
	}

	@Override
	protected void renderGL() {
		// TODO Auto-generated method stub
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		Rectangle bounds = canvas.getBounds();

		float aspect = WIDTH / (float) HEIGHT;
		GL11.glViewport(0, 0, bounds.width, bounds.height);
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();

		GL11.glColor3f(0, 1, 1);
		GL11.glLineWidth(3.0f);

		//points
		
			GL11.glColor3f(1, 0, 0);
			renderSerpinsky();
		
		GL11.glPopMatrix();
	}

	@Override
	protected void initTextures() {
		// TODO Auto-generated method stub
		
	}
	
	public void renderSerpinsky(){
		GL11.glBegin(GL11.GL_POINTS);
		float ran = (float)Math.random()*6;
		GL11.glVertex2f(0,0);
		GL11.glVertex2f(3,6);
		GL11.glVertex2f(6,0);
		for (int i = 0; i < 100; i++) {
			GL11.glVertex2f(i-ran,i);
		}
		GL11.glEnd();
	}
	
	public static void main(String[] args){
		Sierpinsky s = new Sierpinsky();
		s.start();
	}

}

package sierpinsky;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class Sierpinsky extends SkelGL{

	class Point{
		float x,y;
		Point(float x, float y){
			this.x=x;
			this.y=y;
		}
	}
	
	Point[] path = new Point[7];
	
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
	
	public void initSierpinsky(){
		Point a = new Point(1.5f,3f);
		Point b = new Point(0,0);
		Point c = new Point(3f,0);
		Point start = new Point(0.5f,0.5f);
		
		path.add(a);
		path.add(b);
		path.add(c);
		
		double ran = Math.random();
		
		if(ran < .3)
			path.add(new Point((a.x-start.x)/2, (a.y-start.y)/2));
		else if(ran > .3)
			path.add(new Point((b.x-start.x)/2, (b.y-start.y)/2));
		else
			path.add(new Point((c.x-start.x)/2, (c.y-start.y)/2));
		
		for(int i = 0; i < 10; i++){
			path.add(new Point(path,0));
		}
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

package sierpinsky;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Fern extends SkelGL{
	
	ArrayList<Vector2f> points = new ArrayList<Vector2f>();
	
	Vector3f[] colors = {
		new Vector3f(1,0,0),
		new Vector3f(0,1,0),
		new Vector3f(0,0,1)
	};
	
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
		
		//equilateral triangle of size 3 filled with 1,000,000 dots
		Vector2f start = new Vector2f(0.0f, 0.0f);
		points.add(start);
		initFern(100000);
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

		GL11.glLineWidth(3.0f);

		renderFern();
		
		GL11.glPopMatrix();
	}

	@Override
	protected void initTextures() {
		// TODO Auto-generated method stub
		
	}
	
	public void initFern(int pointNum){
		Vector2f next = new Vector2f();
		double ran = Math.random();
		for(int i = 0; i < pointNum; i++){
			if(ran < 0.01){
				next = new Vector2f(0.0f, 0.16f*points.get(i).y);
			} else if (ran >= 0.01 && ran < 0.85){
				next.x = (0.85f*points.get(i).x + 0.04f*points.get(i).y);
				
				next.y=(-0.04f*points.get(i).x+
						0.85f*points.get(i).y + 1.6f);
			} else if (ran >= 0.85 && ran < 92){
				next = new Vector2f(0.2f*points.get(i).x-0.26f*points.get(i).y
						,0.23f * points.get(i).x + 0.22f * points.get(i).y + 1.6f);
			} else{
				next = new Vector2f(-0.15f * points.get(i).x + 0.28f * points.get(i).y
						,0.26f * points.get(i).x + 0.24f * points.get(i).y + 0.44f);
			}
			points.add(next);
			ran = Math.random();
		}
		
		
				
	}
	
	
	
	public void renderFern(){
		GL11.glBegin(GL11.GL_POINTS);
		
		for (int i = 0; i < points.size(); i++) {
			//multi-colored gasket
			GL11.glColor3f(0,1,0);
			GL11.glVertex2f(points.get(i).x, points.get(i).y);
		}
		
		GL11.glEnd();
	}
	
	public static void main(String[] args){
		Fern s = new Fern();
		s.start();
	}

}

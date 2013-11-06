package sierpinsky;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Sierpinsky extends SkelGL{
	
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
		initSierpinsky(10,10,100000);
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

		renderSerpinsky();
		
		GL11.glPopMatrix();
	}

	@Override
	protected void initTextures() {
		// TODO Auto-generated method stub
		
	}
	
	public void initSierpinsky(float xMax, float yMax, int pointNum){
		Vector2f a = new Vector2f(0.0f,0.0f);
		Vector2f b = new Vector2f(xMax/2f, yMax);
		Vector2f c = new Vector2f(xMax, 0f);
		//TODO: Fix the random number
		Vector2f start = new Vector2f(xMax/2, yMax/2);
		Vector2f vertex = randomVertex(a,b,c);
		Vector2f midpoint = new Vector2f((vertex.x + start.x)/2.0f, (vertex.y + start.y)/2.0f);
		
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(midpoint);
		
		for(int i = 1; i < pointNum; i++){
			vertex = randomVertex(a,b,c);
			points.add(new Vector2f((vertex.x + points.get(i-1).x)/2.0f, (vertex.y + points.get(i-1).y)/2.0f));
		}
				
	}
	
	
	//randomly picks a vertex on the triangle
	public Vector2f randomVertex(Vector2f a, Vector2f b, Vector2f c){
		
		Vector2f selected;
		double ran = Math.random();
		
		if(ran < 0.3){
			selected = a;
		}else if(ran >= 0.3 && ran < 0.6){
			selected = b;
		}else{
			selected = c; 
		}
		
		return selected;
		
	}
	
	
	public void renderSerpinsky(){
		GL11.glBegin(GL11.GL_POINTS);
		
		for (int i = 0; i < points.size(); i++) {
			//multi-colored gasket
			GL11.glColor3f(colors[i%3].x, colors[i%3].y, colors[i%3].z);
			GL11.glVertex2f(points.get(i).x, points.get(i).y);
		}
		
		GL11.glEnd();
	}
	
	public static void main(String[] args){
		Sierpinsky s = new Sierpinsky();
		s.start();
	}

}

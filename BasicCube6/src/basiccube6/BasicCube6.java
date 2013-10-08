package basiccube6;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class BasicCube6 {

	Sphere sphere = new Sphere();
	
	long lastFrame;
	
	int fps;
	
	long lastFPS;
	
	int WIDTH = 1024;
	int HEIGHT = 760;
	
	private int numVertices = 36;
	
	private static int index = 0;
	
	
	Vector4f[] point = new Vector4f[numVertices];
	Vector3f[] normal = new Vector3f[numVertices];
	Vector3f[] color = new Vector3f[numVertices]; 
	
	Vector3f[] palette = {
			new Vector3f(1,0,0),
			new Vector3f(0,1,0),
			new Vector3f(0,0,1),
			new Vector3f(1,0,1),
			new Vector3f(1,1,0),
			new Vector3f(0,1,1)
	};
	
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModeAmbient;
	
	float rotation = 20.0f;
	
	float eyex = 0, eyey = 0, eyez = 2;
	float znear= 0.1f, zfar = 1f;
	
	Vector4f[] corner = {
			new Vector4f(-0.5f, -0.5f,  0.5f, 1.0f),
			new Vector4f(-0.5f,  0.5f,  0.5f, 1.0f),
			new Vector4f( 0.5f,  0.5f,  0.5f, 1.0f),
			new Vector4f( 0.5f, -0.5f,  0.5f, 1.0f),
			new Vector4f(-0.5f, -0.5f, -0.5f, 1.0f),
			new Vector4f(-0.5f,  0.5f, -0.5f, 1.0f),
			new Vector4f( 0.5f,  0.5f, -0.5f, 1.0f),
			new Vector4f( 0.5f, -0.5f, -0.5f, 1.0f)
	};
	
	public void quad(int a, int b, int c, int d, int col){
		
		Vector3f aa = new Vector3f(corner[a].x, corner[a].y, corner[a].z);
		Vector3f bb = new Vector3f(corner[b].x, corner[b].y, corner[b].z);
		Vector3f cc = new Vector3f(corner[c].x, corner[c].y, corner[c].z);
		
		//vector subtraction bb - aa, null constructs a new vector
		Vector3f u = Vector3f.sub(bb,aa,null);
		Vector3f v = Vector3f.sub(cc, bb, null);
		
		Vector3f norm = Vector3f.cross(u, v, null);
		
		normal[index] = norm; point[index] = corner[a]; color[index] = palette[col]; index++;
		normal[index] = norm; point[index] = corner[b]; color[index] = palette[col]; index++;
		normal[index] = norm; point[index] = corner[c]; color[index] = palette[col]; index++;
		normal[index] = norm; point[index] = corner[a]; color[index] = palette[col]; index++;
		normal[index] = norm; point[index] = corner[c]; color[index] = palette[col]; index++;
		normal[index] = norm; point[index] = corner[d]; color[index] = palette[col]; index++;
	}
	
	public void initCube(){
		quad(1,0,3,2,0);
		quad(2,3,7,6,1);
		quad(3,0,4,7,2);
		quad(6,5,1,2,3);
		quad(4,5,6,7,4);
		quad(5,4,0,1,5);

	}
	
	public long getTime(){
		return(Sys.getTime() * 2000 / Sys.getTimerResolution());
	}
	
	/**
	 * Calculate how many milliseconds has elapsed since last frame
	 */
	
	public int getDelta(){
		long time = getTime();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	public void updateFPS(){
		if (getTime() - lastFPS > 1000){
			Display.setTitle("The Rotating Cube - " + "FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public void update(int delta){
		//Computer new coordinate for the object based on time elapsed
		rotation += 0.015f * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) eyez -= 0.035f;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) eyez += 0.035f;

		updateFPS();
	}
	
	public void start(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		//Display.setTitle("The Rotating Cube");
		Display.setResizable(true);
		//Init GL
		initGL();
		getDelta(); //initialize the last frame
		lastFPS = getTime(); //Initialize FPS timer
		
		initCube();
		initLightArrays();
		
		while(!Display.isCloseRequested()){
			int delta = getDelta();
			
			update(delta);
			renderGL();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	private void renderGL() {
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspectRatio = WIDTH/(float)HEIGHT;
		GLU.gluPerspective(60f, aspectRatio, 0.1f, 15f);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glRotatef(rotation, 1, 0, 0);
		GL11.glRotatef(rotation, 0, 1, 0);
		GL11.glRotatef(rotation, 0, 0, 1);
		renderCube();
		GL11.glColor3f(0.7f, 0.4f, 0.5f);
		sphere.draw(.6f, 120, 120);
		GL11.glPopMatrix();
		
	}

	private void initLightArrays() {
        // TODO Auto-generated method stub
        //no constructor, not handled by JVM
        //malloc, memory management done outside of java
        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(2.0f).put(2.0f).put(2.0f).put(1.0f).flip();
       
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(-1f).put(1f).put(0.5f).put(0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
        
        lModeAmbient = BufferUtils.createFloatBuffer(4);
        lModeAmbient.put(0.1f).put(0.1f).put(0.1f).put(1f).flip();
       
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecular);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 60.0f);
       
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, whiteLight);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, whiteLight);
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, lModeAmbient);
       
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glEnable((GL11.GL_COLOR_MATERIAL));
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);

}
	
	private void renderCube(){
		GL11.glBegin(GL11.GL_TRIANGLES);
		
		for(int i = 0; i < numVertices; i++){
			GL11.glNormal3f(normal[i].x, normal[i].y, normal[i].z);
			GL11.glColor3f(color[i].x, color[i].y, color[i].z);
			GL11.glVertex3f(point[i].x, point[i].y, point[i].z);
		}
		
		GL11.glEnd();
	}
	
	public void initGL(){
		try{
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Keyboard.enableRepeatEvents(true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspect = WIDTH / (float) HEIGHT;
		GLU.gluPerspective(60f, aspect, znear, zfar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glClearColor(0, 0, 0, 0);

		initLightArrays();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicCube6 cube = new BasicCube6();
		cube.start();
	}

}

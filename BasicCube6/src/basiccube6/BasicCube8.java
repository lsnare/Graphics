package basiccube6;

import java.nio.FloatBuffer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import textureMapper.TextureMapper;
 
//integrate with SWT
public class BasicCube8 {

	boolean animate = true;
	TextureMapper tmap = new TextureMapper();
	
	/*
	 * Window manager
	 */
	Display display = new Display(); //SWT display
	Shell shell; 
	
	Composite comp;
	GLData data = new GLData();
	GLCanvas canvas;//child of display used for drawing
	
	Sphere sphere = new Sphere();
	
	long lastFrame;
	
	int fps;
	
	long lastFPS;
	
	int WIDTH = 1024;
	int HEIGHT = 760;
	
	private int numVertices = 36;
	
	private static int index = 0;
	
	
	Vector4f[] point   = new Vector4f[numVertices];
	Vector3f[] normal  = new Vector3f[numVertices];
	Vector3f[] color   = new Vector3f[numVertices]; 
	Vector2f[] texture = new Vector2f[numVertices];
	
	Vector3f[] palette = {
			new Vector3f(1,0,0),
			new Vector3f(0,1,0),
			new Vector3f(0,0,1),
			new Vector3f(1,0,1),
			new Vector3f(1,1,0),
			new Vector3f(0,1,1)
	};
	
	Vector2f[] textureBounds = {
		//first triangle
		new Vector2f(0,0),	
		new Vector2f(1,0),	
		new Vector2f(1,1),
		//second triangle
		new Vector2f(0,0),	
		new Vector2f(1,1),
		new Vector2f(0,1)
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
		
		texture[index] = textureBounds[index%6]; normal[index] = norm; point[index] = corner[a]; color[index] = palette[col]; index++;
		texture[index] = textureBounds[index%6]; normal[index] = norm; point[index] = corner[b]; color[index] = palette[col]; index++;
		texture[index] = textureBounds[index%6]; normal[index] = norm; point[index] = corner[c]; color[index] = palette[col]; index++;
		texture[index] = textureBounds[index%6]; normal[index] = norm; point[index] = corner[a]; color[index] = palette[col]; index++;
		texture[index] = textureBounds[index%6]; normal[index] = norm; point[index] = corner[c]; color[index] = palette[col]; index++;
		texture[index] = textureBounds[index%6]; normal[index] = norm; point[index] = corner[d]; color[index] = palette[col]; index++;
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
			shell.setText("The Rotating Cube - " + "FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public void update(int delta){
		//Computer new coordinate for the object based on time elapsed
		if(animate)
			rotation += 0.015f * delta;	
	

		updateFPS();
	}
	
	public void start(){
		//initialize window manager
		initSWT();
		
		//initialize GL
		initGL();
		
		//setup cube
		initCube();
		
		//call timing inits
		getDelta();
		lastFPS = getTime();
		
		//start a thread that loops and handles the generation
		final Runnable frameHandler = new Runnable(){

			@Override
			public void run() {
				//Software to handle frame creation
				//runs per frame
				if(!canvas.isDisposed()){
					canvas.setCurrent();
					
					try {
						GLContext.useContext(canvas);
					} catch (LWJGLException e) {
						System.err.println("Context error");
						System.exit(0);
					}
					
					int delta = getDelta();
					update(delta);
					renderGL();
					
					canvas.swapBuffers();
					
					//display.timerExec(time in millis, this);
					//run this asynchronously
					display.asyncExec(this);
				}
			}
		};//end runnable
		
		canvas.addListener(SWT.Paint, new Listener(){

			@Override
			public void handleEvent(Event event) {
				frameHandler.run();
			}
		});//end listener
		
		canvas.addListener(SWT.RESIZE, new Listener(){
			public void handleEvent(Event event){
				try{
					GLContext.useContext(canvas);
				}catch (LWJGLException e){
					e.printStackTrace();
				}
				//per drawing
				resetGL();
			}
		});
		
		display.asyncExec(frameHandler);
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	public Menu initMenu(){
		Menu menu = new Menu(shell, SWT.POP_UP);
		
		//add items to the menu
		
		//create an item
		final MenuItem item0 = new MenuItem(menu, SWT.PUSH);
		//set its text dependent on animation
		item0.setText(animate ? "Stop" : "Animate");
		item0.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				animate = !animate;
				item0.setText(animate ? "Stop" : "Animate");
			}
		});
		
		MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.setText("Exit");
		item1.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				shell.dispose();
			}
			
		});
		
		return menu;
	}
	
	
	private void initSWT() {
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		comp = new Composite(shell, SWT.BORDER);
		comp.setLayout(new FillLayout());
		
		//GL data
		data.depthSize = 1;
		data.doubleBuffer = true;
		
		//canvas
		canvas = new GLCanvas(comp, SWT.NONE, data);
		canvas.setCurrent();
		canvas.setMenu(initMenu());
		shell.addListener(SWT.KeyDown, new Listener(){
			public void handleEvent(Event e){
				onKbdEvent(e);
			}
		});
		
		shell.addListener(SWT.MouseWheel, new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if (event.count > 0)
					eyez -= 0.05f;
				else
					eyez += 0.05f;
			}
			
		});
		shell.setText("SWT/LWJGL Basic Cube");
		shell.setSize(WIDTH, HEIGHT);
		shell.open();
		
	}

	public void initTextures(){
		//enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		tmap = new TextureMapper();
		tmap.createTexture("firefox-512.png", "PNG", "fox");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void resetGL(){
		Rectangle bounds = canvas.getBounds();
		GL11.glViewport(0, 0, bounds.width, bounds.height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspectRatio = (float) bounds.width / (float) bounds.height;
		
		GLU.gluPerspective(60f, aspectRatio, znear, zfar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	protected void onKbdEvent(Event e) {
		
		int key = e.keyCode;
		
		switch(key){
		case SWT.ARROW_UP:
			eyez -= 0.01f;
			break;
		case SWT.ARROW_DOWN:
			eyez += 0.01f;
			break;
		case SWT.ESC:
			shell.dispose();
			break;
		}
	}

	private void renderGL() {
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		Rectangle bounds = canvas.getBounds();
		GL11.glViewport(0, 0, bounds.width, bounds.height);

		float aspectRatio = (float) bounds.width/(float)bounds.height;
		GLU.gluPerspective(60f, aspectRatio, 0.1f, 15f);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		
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
			GL11.glTexCoord2f(texture[i].x, texture[i].y);
			GL11.glNormal3f(normal[i].x, normal[i].y, normal[i].z);
			GL11.glColor3f(color[i].x, color[i].y, color[i].z);
			GL11.glVertex3f(point[i].x, point[i].y, point[i].z);
		}
		
		GL11.glEnd();
	}
	
	public void initGL(){
		
		canvas.setCurrent();
		
		try {
			GLContext.useContext(canvas);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspect = WIDTH / (float) HEIGHT;
		GLU.gluPerspective(60f, aspect, znear, zfar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glClearColor(0, 0, 0, 0);

		initLightArrays();
		initTextures();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void main(String[] args) {
		BasicCube8 cube = new BasicCube8();
		cube.start();
	}

}

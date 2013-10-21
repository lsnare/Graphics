package sprite;

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
public class Sprite {

	
	
	boolean animate = true;
	TextureMapper tmap = new TextureMapper();
	
	/*
	 * Window manager
	 */
	Display display = new Display(); //SWT display
	Shell shell; 
	
	float deltaS = 0.90f;
	
	Composite comp;
	GLData data = new GLData();
	GLCanvas canvas;//child of display used for drawing
	
	Sphere sphere = new Sphere();
	
	long lastFrame;
	
	int fps;
	
	long lastFPS;
	
	int WIDTH = 1024;
	int HEIGHT = 760;
	
	int animIndex = 0;
	int cycleTime = 0;
	
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModeAmbient;
	
	float rotation = 20.0f;
	
	float eyex = 0, eyey = 0, eyez = 0.05f;
	float znear= 0.1f, zfar = 1f;
	
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
			shell.setText("The Duke - " + "FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public void update(int delta){
		//Computer new coordinate for the object based on time elapsed
		cycleTime +=delta;
		if(cycleTime > 150){
			animIndex++;
			animIndex %= 10;
			cycleTime = 0;
		}
		updateFPS();
	}
	
	public void start(){
		//initialize window manager
		initSWT();
		
		//initialize GL
		initGL();
		
		//call timing initializers
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
		tmap.createTexture("sprite.png", "PNG", "duke");
		
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
		renderSprite();
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
	
	private void renderSprite(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmap.getTextureID("duke"));
		GL11.glBegin(GL11.GL_QUADS);
		
			GL11.glTexCoord2f(deltaS * animIndex, 0.6f);
			GL11.glVertex3f(-0.005f, -0.005f, 0);
			GL11.glTexCoord2f(deltaS * 1+animIndex, 0.6f);
			GL11.glVertex3f( 0.005f, -0.005f, 0);
			GL11.glTexCoord2f(deltaS * 1+animIndex, 0f);
			GL11.glVertex3f( 0.005f,  0.005f, 0);
			GL11.glTexCoord2f(deltaS * animIndex, 0f);
			GL11.glVertex3f(-0.005f,  0.005f, 0);
		
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

		animIndex = 0;
		
		initLightArrays();
		initTextures();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void main(String[] args) {
		Sprite cube = new Sprite();
		cube.start();
	}

}

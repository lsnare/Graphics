package snowflake;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public abstract class SkelGL {

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
	
		updateFPS();
	}
	
	public void start(){
		//initialize window manager
		initSWT();
		
		//initialize GL
		initGL();
		
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
	
	
}

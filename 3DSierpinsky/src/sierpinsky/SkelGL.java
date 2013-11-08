package sierpinsky;

import org.eclipse.swt.SWT;
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
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector2f;

import textureMapper.TextureMapper;

public abstract class SkelGL {
	
	TextureMapper tmap = null;
	
	Vector2f[] textBounds = {
		new Vector2f(0.25f,0),
		new Vector2f(0.75f,0),
		new Vector2f(0.5f,0.5f)
	};
	
	/* animation indicator for menu selector */
	boolean animate = true;

	/* time at last frame */
	long lastFrame;

	/* frames per second */
	int fps;

	/* last fps time */
	long lastFPS;

	// SVGA 800x600
	// XVGA 1024x768
	int WIDTH = 1024, HEIGHT = 768;

	/* Eye location */
	float eyex = 0, eyey = 0, eyez = 1;
	float zNear = 0.01f, zFar = 1000f;
	float fovy = 60f;
	/*
	 * Window manager parameters
	 */
	Display display = new Display();
	Shell shell;
	Composite comp;
	GLData data = new GLData();
	GLCanvas canvas;

	/**
	 * Get the accurate time system
	 * 
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate how many milliseconds has elapsed since last frame
	 */

	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			shell.setText("The rotating Cube - FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void start() {

		// Initialize the Window Manager
		initSWT();

		initGL();

		// Call all timing initializations
		getDelta();
		lastFPS = getTime();

		// Start a thread that loops and
		// handles frame generation
		final Runnable frameHandler = new Runnable() {

			@Override
			public void run() {
				// Software to handle frame creation
				// runs per frame
				if (!canvas.isDisposed()) {
					canvas.setCurrent();

					try {
						GLContext.useContext(canvas);
					} catch (LWJGLException e) {
						System.err.println("Context error!");
						System.exit(-1);
					}
					int delta = getDelta();

					update(delta);
					updateFPS();

					renderGL();
					canvas.swapBuffers();

					// display.timerExec(50, this);
					// Run this asynchronously
					display.asyncExec(this);
				}
			}
		}; // End runnable
		canvas.addListener(SWT.Paint, new Listener() {

			@Override
			public void handleEvent(Event event) {
				resetGL();
			}
		});

		canvas.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				// per drawing reset
				resetGL();
			}
		});

		display.asyncExec(frameHandler);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void initSWT() {

		shell = new Shell(display);
		shell.setLayout(new FillLayout());

		// Create a composite
		comp = new Composite(shell, SWT.BORDER);
		comp.setLayout(new FillLayout());

		// Depth size
		data.depthSize = 1;
		data.doubleBuffer = true;

		// set up canvas
		canvas = new GLCanvas(comp, SWT.NONE, data);
		canvas.setCurrent();
		canvas.setMenu(initMenu());

		try {
			GLContext.useContext(canvas);
		} catch (LWJGLException e) {
			System.err.println("Graphics Context setting error!");
			System.exit(-1);
		}
		shell.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event e) {
				onKbdEvent(e);
			}
		});

		shell.addListener(SWT.MouseWheel, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.count > 0)
					eyez -= 0.05f;
				else
					eyez += 0.05f;
			}

		});
		shell.setText("WT/LWJGL Snowflake");
		shell.setSize(WIDTH, HEIGHT);
		shell.open();

	}

	private void onKbdEvent(Event e) {

		int key = e.keyCode;

		switch (key) {
		case SWT.ARROW_UP:
			eyez -= 0.004f;
			break;

		case SWT.ARROW_DOWN:
			eyez += 0.004f;
			break;

		case SWT.ARROW_LEFT:
			eyex -= 0.004f;
			break;

		case SWT.ARROW_RIGHT:
			eyex += 0.004f;
			break;

		case SWT.ESC:
			shell.dispose();
			break;
		}
	}

	public Menu initMenu() {

		// Creates a pop-up menu
		Menu menu = new Menu(shell, SWT.POP_UP);
		// Add items to it
		final MenuItem item0 = new MenuItem(menu, SWT.PUSH);

		item0.setText(animate ? "Stop" : "Animate");

		item0.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				animate = !animate;
				item0.setText(animate ? "Stop" : "Animate");
			}

		});

		MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.setText("Exit");
		item1.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
		return menu;
	}

	protected abstract void initGL();

	protected abstract void update(int delta);

	protected abstract void resetGL();

	protected abstract void renderGL();

	protected abstract void initTextures();

}

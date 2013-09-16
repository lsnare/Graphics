import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class StacksDemoTwo {

	float x = 400.0f, y = 300.0f; 
	float[][] vertex = { {x-100, y-100}, {x+100, y-100}, {x+100, y+100}, {x-100, y+100} }; 
	float rotation = 0.0f; 
	long lastFrame; 
	int fps; 
	long lastFPS; 
	
	public long getTime() { 
		return(Sys.getTime()*1000/Sys.getTimerResolution()); } 
	
	public int getDelta() { 
		long time = getTime(); int delta = (int)(time - lastFrame); 
		lastFrame = time; return delta; } 
	
	public void start() { 
		try { Display.setDisplayMode(new DisplayMode(800,600)); 
		Display.create(); } catch(LWJGLException e) { 
			e.printStackTrace(); 
			System.exit(0); } 
		initGL(); 
		getDelta(); 
		lastFPS = getTime(); 
		while(!Display.isCloseRequested()) { 
			int delta = getDelta(); 
			update(delta); renderGL(); 
			Display.update(); 
			Display.sync(60); } 
		Display.destroy(); } 
	
	
	public void renderGL() { //Clears the screen. 
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(rotation, 0f, 0f, 1f);
		GL11.glTranslatef(-x, -y, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //R, G, B, and Alpha. 
		GL11.glColor3f(0.5f, 0.5f, 1.0f); //Draw a square, yo. 
		GL11.glBegin(GL11.GL_QUADS); 
		GL11.glVertex2f(vertex[0][0], vertex[0][1]); 
		GL11.glVertex2f(vertex[1][0], vertex[1][1]); 
		GL11.glVertex2f(vertex[2][0], vertex[2][1]); 
		GL11.glVertex2f(vertex[3][0], vertex[3][1]); 
		GL11.glEnd(); } 
	
	public void initGL() { 
		GL11.glMatrixMode(GL11.GL_PROJECTION); 
		GL11.glLoadIdentity(); 
		GL11.glOrtho(0, 800, 0, 600, 1, -1); 
		//change to model view, so we rotate the model, not the camera
		GL11.glMatrixMode(GL11.GL_MODELVIEW); } 
	
	public void update(int delta) { 
		rotation = 0.05f * delta; updateFPS(); } 
	
	public void updateFPS() { 
		if(getTime() - lastFPS > 1000) { 
			Display.setTitle("FPS: " + fps); 
			fps = 0; lastFPS += 1000; } 
		fps++; } 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StacksDemoTwo example = new StacksDemoTwo();
		example.start();
	}

}

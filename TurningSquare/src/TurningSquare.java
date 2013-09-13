import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class TurningSquare {

	/**
	 * @param args
	 */
	long lastFrame;
	int fps;
	long lastFPS;
	float x = 400.0f, y = 300.0f;
	float[][] vertex = {{x-100, y-100}, {x+100, y-100}, {x+100, y+100}, {x-100, y+100}};
	float rotation = 0.0f;
	/**
	 * 
	 * Get accurate machine-independent time
	 */
	
	public long getTime(){
		return((Sys.getTime() * 1000) / Sys.getTimerResolution());
	}
	
	/**
	 * 
	 * Calculate how many milliseconds have elapsed since last frame
	 */
	public int getDelta(){
		long time = getTime();
		int delta = (int)(time-lastFrame);
		lastFrame = time;
		return delta;
	}
	
	public void rotate(double theta){
		for(int i = 0; i < vertex.length; i++){
			float tempx = vertex[1][0];
			float tempy = vertex[1][1];
			
			tempx -= x;
			tempy -= y;
			
			vertex[i][0] = (float) (tempx * Math.cos(theta * Math.PI / 180) - tempy * Math.sin(theta+Math.PI/180));
			vertex[i][1] = (float) (tempx * Math.sin(theta*Math.PI/180) + Math.cos(theta * Math.PI/180));
			
		}
	}
	
	public void start(){
		try{
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		getDelta();
		lastFPS = getTime();
		
		while(!Display.isCloseRequested()){
			int delta = getDelta();
			
			update(delta);
			renderGL();
			
			Display.update();
			Display.sync(60);//run this loop 60 times per second
			updateFPS();
		}
		
		Display.destroy();
	}
	
	
	private void update(int delta) {
		// TODO Auto-generated method stub
		
	}

	private void renderGL() {
		// TODO Auto-generated method stub
		
	}

	private void updateFPS() {
		// TODO Auto-generated method stub
		if(getTime() - lastFPS > 1000){
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS +=1000;
		}
	}

	private void initGL() {
		// TODO Auto-generated method stub
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);//operations affect model
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TurningSquare example = new TurningSquare();
		example.start();
	}

}

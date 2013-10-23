package turtle;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class CopyOfTurtle {

	public static final double PI = Math.PI;
	@SuppressWarnings("unused")
	private double theta;
	@SuppressWarnings("unused")
	private boolean ink;
	@SuppressWarnings("unused")
	private Location loc;
	
	//tiny class for location of turtle
	private class Location{
		float x,y;
		Location(float x, float y){
			this.x = x;
			this.y = y;
		}
	}
	
	private ArrayList<Location> path = new ArrayList<Location>();
	
	
	public CopyOfTurtle(){
		loc = new Location(0,0);
		theta = 90;
		ink = false;
	}
	
	//place turtle at any point
	public void init(float x, float y, double theta){
		this.theta = theta;
		loc.x = x;
		loc.y = y;
	}
	
	public void forward(float distance){
		//create new path so it is not overwritten
		if (ink){
			path.add(new Location(loc.x, loc.y));
		}
		loc.x = loc.x + (float) (distance * Math.cos(theta * PI / 180));
		loc.y = loc.y + (float) (distance * Math.sin(theta * PI / 180));
		if(ink){
			path.add(new Location(loc.x, loc.y));
		}
	}
	
	public void right(double theta){
		this.theta -= theta;
		if (this.theta < -360){
			this.theta += 360;
		}
	}
	
	public void left(double theta){
		this.theta += theta;
		if (this.theta > 360){
			this.theta -= 360;
		}
	}
	
	public void pen(boolean position){
		ink = position;
	}
	
	public void show(){
		
		try {
			Display.setDisplayMode(new DisplayMode(1000,600));
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		//init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 550, 1,-1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		while(!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glColor3f(0.5f, 0.0f, 0.0f);
			GL11.glBegin(GL11.GL_LINES);
			
			for (Location ll : path){
				GL11.glVertex2f(ll.x, ll.y);
			}
			
			GL11.glEnd();
			Display.update();
		}
		Display.destroy();	
		
	}
	
	public void patternOne(int size, double angle, int max){
		pen(true);
		do{
			forward(size);
			right(angle);
			size+=2;
		}while(size<max);
		pen(false);
	}
	
	/**
	 * SAVE THIS METHOD
	 * @param size
	 * @param angle
	 * @param max
	 */
	public void patterntwo(int size, double angle, int max){
		pen(true);
		do{
			
			forward(size);
			left(135);
			forward(1);
			right(45);
			forward(1);
			left(135);
			forward(1);
			right(angle);
			size+=1;
		}while(size<max);
		pen(false);
	}
	
	public static void main(String[] args){
		CopyOfTurtle turtle = new CopyOfTurtle();
		turtle.init(370,310, 0);
		turtle.patterntwo(100, 90, 500);
		turtle.show();
	}
	
}

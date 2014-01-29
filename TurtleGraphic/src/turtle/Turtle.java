/****************************************************************
 * Turtle Graphics Implementation   							*
 * 																*
 * Author: Lucian Snare             							*
 * 																*
 * A simple graphics paradigm in which a "turtle" with 			*
 * a pen attached to its stomach draws a picture by moving 		*
 * and turning at a specified distance and angle.	 			*
 * 																*
 * Special thanks to Dr. Krish Pillai for providing the basic 	*
 * code as part of the Interactive Graphics Programming class 	*
 * at Lock Haven University.									*
 * 																*
 * **************************************************************/

package turtle;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Turtle {

	public static final double PI = Math.PI;
	//the current angle of the turtle in degrees
	public double theta;
	//true if the pen is down, flase if it is up
	private boolean ink;
	//the location current location of the turtle, given as an (x,y) pair
	public Location loc;
	
	//Stacks to hold position and angle values
	protected Stack<Location> lStack = new Stack<Location>();
	protected Stack<Double> aStack = new Stack<Double>();

	//tiny class for location of turtle
	class Location{
		float x,y;
		Location(float x, float y){
			this.x = x;
			this.y = y;
		}
	}
	
	//stores the locations that the turtle has visited
	public ArrayList<Location> path = new ArrayList<Location>();
	
	
	public Turtle(){
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
	
	/*
	 * Turtle movement methods
	 */
	
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
	
	public void backward(Location old){
		//create new path so it is not overwritten
		if (ink){
			path.add(new Location(loc.x, loc.y));
		}
		loc.x = old.x;
		loc.y = old.y;
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
	
	//OpenGL setup method
	public void show(){
		
		//Create a display
		try {
			Display.setDisplayMode(new DisplayMode(800,800));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1,-1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		while(!Display.isCloseRequested()){
			GL11.glClearColor(1, 1, 1, 0);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glColor3f(0.0f, 0.0f, 0.0f);
			//Vertices will be connected by lines
			GL11.glBegin(GL11.GL_LINES);
			
			//Add turtle path locations as vertices
			for (Location ll : path){
				GL11.glVertex2f(ll.x, ll.y);
			}
			
			GL11.glEnd();
			Display.update();
		}
		Display.destroy();	
		
	}
	
	/*Method for translating L-System grammars into 
	 movement rules for the turtle */
	
	public void interpretLSystem(String seq, int distance, double angle){
		
		for(int i = 0; i < seq.length(); i++){
			char c = seq.charAt(i);
			//typical forward movement characters
			if(c == 'F' || c == 'A' || c == 'B')
				this.forward(distance);
			//typical rules for direction
			else if (c == '+')
				this.right(angle);
			else if (c == '-')
				this.left(angle);
			//Push location and angle
			else if (c == '['){
				lStack.push(new Location(loc.x, loc.y));
				aStack.push(theta);
			//Pop location and angle
			}else if (c == ']'){
				loc = lStack.pop();
				theta = aStack.pop();
			}
		}
	}
	
	/*
	 * A few example methods
	 */
	
	public void pentagram(int size, double angle, int max){
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
			size+=5;//1,5
		}while(size<max);
		pen(false);
	}
	
	void thetaMaze(int size, double angle, int max, int n){
		pen(true);
		forward(size);
		right(angle);
		forward(size);
		right(angle);
		size+=5;
		do{
			forward(size);
			right(angle);
			forward(size);
			right(angle + n%2);
			n+=1;
			size+=5;
		}while(size<max);
		pen(false);
	}
	
	public static void main(String[] args) {
		
		Turtle turtle = new Turtle();
		LSystem l = new LSystem();
		
		//The string that will be modified by L-System operations
		String seq = new String();
		
		/*
		 * L-Systems with optimal number of iterations
		 */
		
		//seq = l.LSystemDragon(13);
		//seq = l.LSystemSierpinski(7);
		//seq = l.LSystemLevy(10);
		//seq = l.LSystemBrush(5);
		//seq = l.LSystemKochSnowflake(4);
		//seq = l.LSystemThueMorse(13);
		//seq = l.LSystemTree(6);
		
		turtle.init(300,400,0);
		turtle.pen(true);
		
		turtle.interpretLSystem(seq, 5, l.angle);
		turtle.show();
	}
}

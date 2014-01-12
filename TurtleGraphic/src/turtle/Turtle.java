package turtle;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Turtle {

	public static final double PI = Math.PI;
	public double theta;
	private boolean ink;
	public Location loc;
	
	//tiny class for location of turtle
	class Location{
		float x,y;
		Location(float x, float y){
			this.x = x;
			this.y = y;
		}
	}
	
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
	
	public void show(){
		
		try {
			Display.setDisplayMode(new DisplayMode(800,800));
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
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
			GL11.glBegin(GL11.GL_LINES);
			
			int i = 0;
			for (Location ll : path){
				GL11.glVertex2f(ll.x, ll.y);
				i++;
			}
			
			GL11.glEnd();
			Display.update();
		}
		Display.destroy();	
		
	}
	
	public void interpretLSystem(String seq, int distance, double angle){
		
		for(int i = 0; i < seq.length(); i++){
			char c = seq.charAt(i);
			if(c == 'F' || c == 'A' || c == 'B')
				this.forward(distance);
			else if (c == '+')
				this.right(angle);
			else if (c == '-')
				this.left(angle);
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
	
	boolean menu(Turtle turtle){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a patter you wish to view. Patterns included: ");
		System.out.println(" - Pentagram \n - ThetaMaze\n");
		String choice = scan.nextLine();
		
		switch (choice){
			case "Pentagram": turtle.pentagram(1, 80, 5000);
				return true;
			case "ThetaMaze": turtle.thetaMaze(10,  90, 1000, 2); 
				return true;
			default: return false;
		}
	}
	
	public static void mainA(String[] args){
		Turtle turtle = new Turtle();
		turtle.init(370,310, 0);
		while(!turtle.menu(turtle)){
			System.out.println("You did not enter a valid choice, please try again.");
		}
		turtle.show();
	}
	
}

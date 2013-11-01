package turtle;
import java.util.Stack;

import turtle.Turtle.Location;



public class Dragon {

	private Turtle turtle;
	private int distance;
	private Stack<Location> stack = new Stack<Location>();
	
	public Dragon(Turtle turtle){
		this.turtle = turtle;
		distance = 10;
	}
	
	public Dragon(Turtle turtle, int distance){
		this.turtle = turtle;
		this.distance = distance;
	}
	public void dragon(int n){
		if (n == 0){
			turtle.forward(distance);
			
		} else {
			dragon(n-1);
			turtle.right(90);
			nogard(n-1);
		}
	}
	
	public void nogard(int n){
		if (n == 0){
			turtle.forward(distance);
			
		} else {
			dragon(n-1);
			turtle.left(90);
			nogard(n-1);
		}
	}
	
	public void cathedral(int n, int angle){
		if (n==0){
			turtle.forward(distance);
		} else {
			for(int i = 0; i < n; i++){
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			turtle.left(-(angle*2));
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			}
		}
		
	}
	
	public void koch(int n, int angle){
		if (n==0){
			turtle.forward(distance);
		} else {
			
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			turtle.left(-(angle*2));
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			
		}
		
	}
	
	public void koch2(int n, int angle){
		if (n==0){
			turtle.forward(distance);
		} else {
			
			koch(n-1,angle);
			turtle.right(angle);
			koch(n-1,angle);
			turtle.left(-(angle*2));
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			
		}
		
	}
	
	//fractal wiki
	public void tree(int n){
		for(int i = 0; i < 10; i++){
		stack.push(turtle.loc);
		turtle.left(60);
		turtle.forward(10);
		turtle.loc=stack.pop();
		turtle.right(60);
		turtle.forward(10);
		}
		
		
	}
	
	public boolean[] thuemorse(boolean[] inseq){
		
		boolean[] sequence = new boolean[2*inseq.length];
		
		for(int i = 0; i < inseq.length; i++){
			sequence[i] = inseq[i];
		}
		
		int index = inseq.length;
		for(int j = 0; j < inseq.length; j++){
			sequence[index] = !inseq[j];
			index++;
		}
		
		return sequence;
	}
	
	public void thueTree(boolean[] seq, int distance, int angle){
		for(int i = 0; i < seq.length; i++){
			if(seq[i])
				turtle.forward(distance);
			else
				turtle.left(angle);
		}
	}
	
	//generates an oscillation pattern at 90 degrees
	public void thueTree2(boolean[] seq, int distance, int angle){
		for(int i = 0; i < seq.length; i++){
			if(seq[i])
				turtle.right(angle);
			else
				turtle.left(angle);
			turtle.forward(distance);
		}
	}
	
	//generates an interesting Koch curve at 60 degrees
	public void thueTree3(boolean[] seq, int distance, int angle){
		for(int i = 0; i < seq.length; i++){
			if(seq[i])
				turtle.left(-2*angle);
			else
				turtle.left(angle);
			turtle.forward(distance);
		}
	}

	public void thueTree4(boolean[] seq, int distance, int angle){
		for(int i = 0; i < seq.length; i++){
			if(seq[i])
				thueTree(seq, distance, angle);
			else
				turtle.left(angle);
			turtle.forward(distance);
		}
	}
	
	public static void main(String[] args){
		Turtle turtle = new Turtle();
		Dragon dragon = new Dragon(turtle, 5);
		turtle.init(200, 200, 0);
		turtle.pen(true);
		
		//dragon.dragon(12);
		//dragon.koch(6,60);
		//dragon.tree(50);
		
		
		//////////////////////
		//  Koch Snowflake  //
		//////////////////////
		
		/*dragon.koch(4,60);
		turtle.left(-120);
		dragon.koch(4,60);
		turtle.left(-120);
		dragon.koch(4,60);*/
		
		//dragon.cathedral(4, 90);
		
		
		boolean[] seq = {false};
		for(int i = 0; i < 13; i++){
			seq=dragon.thuemorse(seq);
		}
		
		/////////////////////////////////////////////////////
		// Koch Curve generated with a Thue-Morse Sequence //
		/////////////////////////////////////////////////////
		//dragon.thueTree(seq, 5, 60);
		//dragon.tree(8);
		
		turtle.pen(false);
		turtle.show();
		
	}
	
	
}

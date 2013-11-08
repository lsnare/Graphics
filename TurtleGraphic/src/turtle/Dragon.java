package turtle;
import java.util.ArrayList;
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
	
	public void thueTree(ArrayList<Boolean> seq, int distance, int angle){
		for(int i = 0; i < seq.size(); i++){
			if(seq.get(i))
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
				//thueTree(seq, distance, angle);
			//else
				turtle.left(angle);
			turtle.forward(distance);
		}
	}
	
	public void algae(ArrayList<Boolean> seq, int distance, int angle){
		for(int i = 0; i < seq.size(); i++){
			if(seq.get(i))
				turtle.right(angle);
			else
				turtle.forward(distance);
		}
	}
	
	public void sier(ArrayList<String> seq, int distance, int angle){
		for(int i = 0; i < seq.size(); i++){
			if(seq.get(i).equals("A") || seq.get(i).equals("B")){
				turtle.forward(distance);
			}
			else if (seq.get(i).equals("-")){
				turtle.right(angle);
			}
			else{
				turtle.left(angle);
			}
		}
	}
	
	public void levy(ArrayList<String> seq, int distance, int angle){
		for(int i = 0; i < seq.size(); i++){
			if(seq.get(i).equals("F")){
				turtle.forward(distance);
			}
			else if (seq.get(i).equals("+")){
				turtle.right(angle);
			}
			else{
				turtle.left(angle);
			}
		}
	}
	
	public static void main(String[] args){
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
		
		
		
		
		/*ArrayList<Boolean> seq = new ArrayList<Boolean>();
		seq.add(true);
		for(int i = 0; i < 15; i++){
			seq=dragon.algaeSeries(seq);
		}
		
		for(int i = 0; i  <seq.size(); i++){
			System.out.print(seq.get(i)+", ");
		}*/
		
		/*ArrayList<String> seq = new ArrayList<String>();
		seq.add("A");
		for(int i = 0; i < 7; i++){
			seq=dragon.LSystemSierpinski(seq);
		}
		
		for(int i = 0; i < seq.size(); i++){
			System.out.print(seq.get(i)+" ");
		}*/
		
		Turtle turtle = new Turtle();
		LSystem l = new LSystem();
		Dragon dragon = new Dragon(turtle, 5);
		turtle.init(300, 600, 0);
		turtle.pen(true);
		
		
		ArrayList<Boolean> seq = new ArrayList<Boolean>();
		seq.add(true);
		for(int i = 0; i < 13; i++){
			seq=l.LSystemThueMorse(seq);
		}
		
		/*ArrayList<String> seq = new ArrayList<String>();
		seq.add("F");
		for(int i = 0; i < 18; i++){
			seq=l.LSystemLevy(seq);
		}*/
		
		/////////////////////////////////////////////////////
		// Koch Curve generated with a Thue-Morse Sequence //
		/////////////////////////////////////////////////////
		dragon.thueTree(seq, 5, 60);
		//dragon.tree(8);
		//dragon.algae(seq, 75, 60);
		//dragon.sier(seq, 4, 60);
		//dragon.levy(seq, 1, 45);
		turtle.pen(false);
		turtle.show();
		
	}
	
	
}

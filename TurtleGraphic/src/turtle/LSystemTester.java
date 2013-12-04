package turtle;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import turtle.Turtle.Location;



public class LSystemTester {

	private Turtle turtle;
	private int distance;
	private Stack<Float> xStack = new Stack<Float>();
	private Stack<Float> yStack = new Stack<Float>();
	private Stack<Double> angleStack = new Stack<Double>();
	
	public LSystemTester(Turtle turtle){
		this.turtle = turtle;
		distance = 10;
	}
	
	public LSystemTester(Turtle turtle, int distance){
		this.turtle = turtle;
		this.distance = distance;
	}

	
	public void algae(ArrayList<String> seq, int n){
		
		for(int i = 0; i < seq.size(); i++){
			if(seq.get(i).equals("1") || seq.get(i).equals("0")){
				turtle.forward(3);
			}else if(seq.get(i).equals("[")){
				xStack.push(turtle.loc.x);
				yStack.push(turtle.loc.y);
				angleStack.push(turtle.theta);
				turtle.left(45);
			}else{
				turtle.loc.x=xStack.pop();
				turtle.loc.y=yStack.pop();
				turtle.theta=angleStack.pop();
				turtle.right(45);
			}
		}
	}
	
	public void straw(ArrayList<String> seq, int distance){
		
		for(int i = 0; i < seq.size(); i++){
			if(seq.get(i).equals("F")){
				turtle.forward(distance);
			}else if(seq.get(i).equals("[")){
				xStack.push(turtle.loc.x);
				yStack.push(turtle.loc.y);
				angleStack.push(turtle.theta);
			}else if (seq.get(i).equals("]")){
				turtle.loc.x=xStack.pop();
				turtle.loc.y=yStack.pop();
				turtle.theta=angleStack.pop();
			}else if (seq.get(i).equals("+")){
				turtle.right(21);
			}else{
				turtle.left(21);
			}
		}
	}
	
	public void sierpinski(ArrayList<String> seq, int distance, int angle){
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
	
	public void koch(ArrayList<String> seq, int distance, int angle){
		for(int i = 0; i < seq.size(); i++){
			if(seq.get(i).equals("F")){
				turtle.forward(distance);
			}
			else if (seq.get(i).equals("-")){
				turtle.left(angle);
			}
			else{
				turtle.right(angle);
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
	
	public void dragon(ArrayList<String> seq, int distance, int angle){
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
	

	boolean menu(Turtle turtle, ArrayList<String> seq,
			LSystem l, LSystemTester test){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a pattern number you wish to view. Patterns included: ");
		System.out.println(" (1) Sierpinski \n (2) Straw\n" +
				" (3) Levy C Curve");
		int choice = scan.nextInt();
		System.out.println("How many iterations?");
		int iterations = scan.nextInt();
		
		switch (choice){
		case 1:
			seq.add("A");
			for(int i = 0; i < iterations; i++){
				seq=l.LSystemSierpinski(seq);
			}
			test.sierpinski(seq, 5, 60);
			return true;
		
		case 2:
			seq.add("X");
			for(int i = 0; i < iterations; i++){
				seq=l.LSystemBrush(seq);
			}
			test.straw(seq, 1);
			return true;
		
		
		/*ArrayList<Boolean> seq = new ArrayList<Boolean>();
		seq.add(true);
		for(int i = 0; i < 12; i++){
			seq=l.LSystemThueMorse(seq);
		}*/
			
		case 3:
			seq.add("F");
			for(int i = 0; i < iterations; i++){
				seq=l.LSystemLevy(seq);
			}
			test.levy(seq, 1, 45);
			return true;
		case 4:
			seq.add("F");
			seq.add("-");
			seq.add("-");
			seq.add("F");
			seq.add("-");
			seq.add("-");
			seq.add("F");
			for(int i = 0; i < iterations; i++){
				seq=l.LSystemKoch(seq);
			}
			test.koch(seq, 1, 60);
			return true;
			
		default: return false;
		}
	}
	
	public static void main(String[] args){
		
		Turtle turtle = new Turtle();
		LSystem l = new LSystem();
		LSystemTester tester = new LSystemTester(turtle, 5);
		turtle.init(400, 300, 0);
		turtle.pen(true);
		ArrayList<String> seq = new ArrayList<String>();
		tester.menu(turtle, seq, l, tester);
		//What if we feed l systems into different methods
		/*ArrayList<String> seq = new ArrayList<String>();
		seq.add("F");
		seq.add("X");
		for(int i = 0; i < 14; i++){
			seq=l.LSystemDragon(seq);
		}*/
		
		
		/////////////////////////////////////////////////////
		// Koch Curve generated with a Thue-Morse Sequence //
		/////////////////////////////////////////////////////
		//dragon.thueTree(seq, 3, 60);
		//dragon.tree(8);
		//dragon.algae(seq, 2);
		/*for(int i = 7; i < seq.size(); i++){
			System.out.print(seq.get(i));
		}*/
		//tester.sierpinski(seq, 2, 60);
		//////////////////////////////////////////////////////////
		// If we feed the Dragon L-System to the Levy movement  //
		// rules, using 90 degrees, we still get a dragon       //
		//////////////////////////////////////////////////////////
		//dragon.levy(seq, 2, 45);
		//dragon.dragon(seq, 3, 90);
		//dragon.brush(seq);
		//dragon.koch(seq, 5, 60);
		turtle.pen(false);
		turtle.show();
		
	}
	
	
}

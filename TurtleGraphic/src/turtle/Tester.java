package turtle;

import java.util.ArrayList;

public class Tester {

	
	public void dragon(Turtle turtle, String seq, int distance){
		for(int i = 0; i < seq.length(); i++){
			if(seq.charAt(i) == 'F')
				turtle.forward(distance);
			else if(seq.charAt(i) == '-')
				turtle.left(90);
			else if(seq.charAt(i) == '+')
				turtle.right(90);
		}
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Turtle turtle = new Turtle();
		LSystem l = new LSystem();
		Tester test = new Tester();
		String seq = new String();
		//seq = l.LSystemDragon(13);
		seq = l.LSystemSierpinski(7);
		System.out.println(seq.toString());
		turtle.init(400,400,0);
		turtle.pen(true);
		turtle.interpretLSystem(seq, 3, 60);
		turtle.show();
	}

}

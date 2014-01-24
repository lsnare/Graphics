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
		String seq = new String();
		//seq = l.LSystemDragon(13);
		//seq = l.LSystemSierpinski(7);
		//seq = l.LSystemLevy(14);
		seq = l.LSystemBrush(5);
		System.out.println(seq.toString());
		turtle.init(300,400,0);
		turtle.pen(true);
		turtle.interpretLSystem(seq, 5, 22.5);
		turtle.show();
	}

}

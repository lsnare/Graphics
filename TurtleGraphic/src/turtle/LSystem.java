/********************************************************************
 * Lindenmayer System (L-System) Implementation   					*
 * 																	*
 * Author: Lucian Snare             								*
 * 																	*
 * L-Systems are a string rewriting system used to model 			*
 * plant and fractal growth. When combined with Turtle Graphics,	*
 * many beautiful shapes and realistic plant models can be drawn.	*
 * 																	*
 * Much more information can be found in Aristid Lindenmaer's book,	*
 * The Algorithmic Beauty of Plants. This text provides invaluable	*
 * knowledge about rewriting systems, plant growth, biology,		*
 * and computer graphics											*
 * 																	*
 * ******************************************************************/

package turtle;

import java.util.ArrayList;

public class LSystem {
	
	//angle specific to turtle interpretation of each L-System
	double angle = 0;

	// L-System for generating the Sierpinski triangle
	public String LSystemSierpinski(int iterations) {
		
		String axiom = "A";
		String next;
		angle = 60;

		for (int i = 0; i < iterations; i++) {

			next = "";
			for(int j = 0; j < axiom.length(); j++){
				char c = axiom.charAt(j);
				if(c == 'A')
					next += "B-A-B";
				else if (c == 'B')
					next += "A+B+A";
				else 
					next += c;
			}
		
			axiom = next;
		}
		
		return axiom;
	}
	
		
	//L-System for generating the Dragon Fractal
	public String LSystemDragon(int iterations) {

		String axiom = "FX";
		String next;
		angle = 90;	
		
		for(int  i = 0; i < iterations; i++){
			//clear out the next string
			next = "";
			for(int j = 0; j < axiom.length(); j++){
				char c = axiom.charAt(j);
				//system rules
				if(c == 'X')
					next += "X+YF";
				else if (c == 'Y')
					next += "FX-Y";
				else
					next += c;
			}
			axiom = next;
		}

		return axiom;
	}

	// L-System for generating the Levy C Curve
	public String LSystemLevy(int iterations) {

		String axiom = "F";
		String next;
		angle = 45;

		for (int i = 0; i < iterations; i++) {
			
			next = "";
			for(int j = 0; j < axiom.length(); j++){
				char c = axiom.charAt(j);
				if(c == 'F')
					next += "+F--F+";
				else 
					next += c;
			}
			
			axiom = next;
		}

		return axiom;
	}
	
	//L System for creating a simple fractal tree
	public String LSystemTree(int iterations) {

		String axiom = "A";
		String next;
		angle = 45;

		for (int i = 0; i < iterations; i++) {
			
			next = "";
			for(int j = 0; j < axiom.length(); j++){
				char c = axiom.charAt(j);
				if(c == 'A')
					next += "B[-A]+A";
				else if (c == 'B')
					next += "BB";
				else 
					next += c;
			}
			
			axiom = next;
		}

		return axiom;
		
	}
	
	// L-System for generating a bush-like plant
	public String LSystemBrush(int iterations) {

		String axiom = "X";
		String next;
		angle = 22.5;
		
		for(int i = 0; i < iterations; i++){
			
			next = "";
			for(int j = 0; j < axiom.length(); j++){
				char c = axiom.charAt(j);
				if(c == 'F')
					next += "FF";
				else if (c == 'X')
					next += "F-[[X]+X]+F[+FX]-X";
				else
					next += c;
			}
			
			axiom = next;
			
		}
		
		return axiom;
	}
	
	//L-System for generating the Koch's Snowflake fractal
	public String LSystemKochSnowflake(int iterations) {

		String axiom = "F--F--F";
		String next;
		angle = 60;

		for (int i = 0; i < iterations; i++) {
			
			next = "";
			for(int j = 0; j < axiom.length(); j++){
				char c = axiom.charAt(j);
				if(c == 'F')
					next += "F+F--F+F";
				else 
					next += c;
			}
			
			axiom = next;
		}

		return axiom;
	}

	//L-System for generating a modified version of the Thue-Morse Sequence
	public String LSystemThueMorse(int iterations) {
		
		//To make interpreting the string easier, "0" has been
		//replaced by "-" (turn left), and "1" has been replaced
		//by "F" (move forward).
		
		String axiom = "-";
		String next;
		angle = 60;

		for (int i = 0; i < iterations; i++) {
			
			next = "";
			for(int j = 0; j < axiom.length(); j++){
				char c = axiom.charAt(j);
				if(c == '-')
					next += "-F";
				else 
					next += "F-";
			}
			
			axiom = next;
		}

		return axiom;
		
	}
}

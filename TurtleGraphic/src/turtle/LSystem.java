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
	

	// L-System for generating the Sierpinski triangle
	public String LSystemSierpinski(int iterations) {
		
		String axiom = "A";
		String next;

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
	
	public ArrayList<String> LSystemAlgae(ArrayList<String> inseq) {

		ArrayList<String> sequence = new ArrayList<String>();

		for (int j = 0; j < inseq.size(); j++) {
			if (inseq.get(j) == "1") {
				sequence.add("1");
				sequence.add("1");
			} else if (inseq.get(j) == "0"){
				sequence.add("1");
				sequence.add("[");
				sequence.add("0");
				sequence.add("]");
				sequence.add("0");
			}else if (inseq.get(j) == "[" || inseq.get(j) == "]"){
				sequence.add(inseq.get(j));
			}
		}
		return sequence;
	}
	
	// L-System for generating a bush-like plant
	public String LSystemBrush(int iterations) {

		String axiom = "X";
		String next;
		
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
	
	public ArrayList<String> LSystemKoch(ArrayList<String> inseq) {

		String axiom = "F";
		String next;

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
		
		ArrayList<String> sequence = new ArrayList<String>();

		for (int j = 0; j < inseq.size(); j++) {
			if (inseq.get(j) == "F"){
				sequence.add("F");
				sequence.add("+");
				sequence.add("F");
				sequence.add("-");
				sequence.add("-");
				sequence.add("F");
				sequence.add("+");
				sequence.add("F");
			}else{
				sequence.add(inseq.get(j));
			}
		}
		return sequence;
	}

	public ArrayList<Boolean> LSystemThueMorse(ArrayList<Boolean> inseq) {
		ArrayList<Boolean> sequence = new ArrayList<Boolean>();
		//populate the new sequences with the entire old sequence 
		for (int i = 0; i < inseq.size(); i++) {
			sequence.add(inseq.get(i));
		}
		//concatenate the bitwise negation of the sequence
		for (int j = 0; j < inseq.size(); j++) {
			sequence.add(!inseq.get(j));
		}
		return sequence;
	}
}

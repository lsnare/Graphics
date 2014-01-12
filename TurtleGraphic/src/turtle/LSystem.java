package turtle;

import java.util.ArrayList;

public class LSystem {
	

	// grammar for the Sierpinski L-System
	public String LSystemSierpinski(int iterations) {
		
		String axiom = "A";
		String next = "";

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
			String next = "";
			
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

	// grammar for the Lev'y C Curve L-System
	public ArrayList<String> LSystemLevy(ArrayList<String> inseq) {

		ArrayList<String> sequence = new ArrayList<String>();

		for (int j = 0; j < inseq.size(); j++) {
			if (inseq.get(j) == "F") {
				sequence.add("+");
				sequence.add("F");
				sequence.add("-");
				sequence.add("-");
				sequence.add("F");
				sequence.add("+");
			} else
				sequence.add(inseq.get(j));
		}

		return sequence;
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
	
	public ArrayList<String> LSystemBrush(ArrayList<String> inseq) {

		ArrayList<String> sequence = new ArrayList<String>();

		for (int j = 0; j < inseq.size(); j++) {
			if (inseq.get(j) == "F") {
				sequence.add("F");
				sequence.add("F");
			} else if (inseq.get(j) == "X"){
				sequence.add("F");
				sequence.add("-");
				sequence.add("[");
				sequence.add("[");
				sequence.add("X");
				sequence.add("]");
				sequence.add("+");
				sequence.add("X");
				sequence.add("]");
				sequence.add("+");
				sequence.add("F");
				sequence.add("[");
				sequence.add("+");
				sequence.add("F");
				sequence.add("X");
				sequence.add("]");
				sequence.add("-");
				sequence.add("X");
			}else{
				sequence.add(inseq.get(j));
			}
		}
		return sequence;
	}
	
	public ArrayList<String> LSystemKoch(ArrayList<String> inseq) {

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

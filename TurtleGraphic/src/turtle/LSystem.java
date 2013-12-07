package turtle;

import java.util.ArrayList;

public class LSystem {
	

	// grammar for the Sierpinski L-System
	public ArrayList<String> LSystemSierpinski(ArrayList<String> inseq) {

		ArrayList<String> sequence = new ArrayList<String>();

		for (int j = 0; j < inseq.size(); j++) {

			if (inseq.get(j).equals("A")) {
				sequence.add("B");
				sequence.add("-");
				sequence.add("A");
				sequence.add("-");
				sequence.add("B");

			} else if (inseq.get(j).equals("B")) {
				// a, b = forward, + = left, - = right
				sequence.add("A");
				sequence.add("+");
				sequence.add("B");
				sequence.add("+");
				sequence.add("A");
			} else if (inseq.get(j).equals("+") || inseq.get(j).equals("-")) {
				sequence.add(inseq.get(j));
			}
		}
		return sequence;
	}
		
		public ArrayList<String> LSystemDragon(ArrayList<String> curSeq) {

			//create a sequence to replace the current one
			ArrayList<String> newSeq = new ArrayList<String>();

			for (int i = 0; i < curSeq.size(); i++) {

				if (curSeq.get(i).equals("X")) {
					newSeq.add("X");
					newSeq.add("+");
					newSeq.add("Y");
					newSeq.add("F");
				} else if (curSeq.get(i).equals("Y")) {
					// a, b = forward, + = left, - = right
					newSeq.add("F");
					newSeq.add("X");
					newSeq.add("-");
					newSeq.add("Y");
				} else {
					newSeq.add(curSeq.get(i));
				}
			}

		return newSeq;
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

package turtle;

import java.util.ArrayList;

public class LSystem {
	
	// grammar for the Algae L-System
	public ArrayList<Boolean> LSystemAlgae(ArrayList<Boolean> inseq) {

		ArrayList<Boolean> sequence = new ArrayList<Boolean>();

		for (int j = 0; j < inseq.size(); j++) {
			if (inseq.get(j)) {
				sequence.add(true);
				sequence.add(false);
			} else
				sequence.add(true);
		}

		return sequence;
	}

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

	public ArrayList<Boolean> LSystemThueMorse(ArrayList<Boolean> inseq) {

		ArrayList<Boolean> sequence = new ArrayList<Boolean>();

		for (int i = 0; i < inseq.size(); i++) {
			sequence.add(inseq.get(i));
		}

		for (int j = 0; j < inseq.size(); j++) {
			sequence.add(!inseq.get(j));
		}

		return sequence;
	}
}

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Interpreter {

	/* MAIN */

	public static void main(String... args) {
		if (args.length == 1) {
			out("");
			(new Interpreter(args[0])).execute();
			out("");
		} else {
			out("Usage:\njava Interpreter <inputfile.myl>");
		}
	}

	/* ACTUAL INTERPRETER */

	private ArrayList<Instruction> instructions = new ArrayList<Instruction>();
	private int progCounter = 0;
	private Stack<Integer> stack = new Stack<Integer>();
	private HashMap<String, Integer> labels = new HashMap<String, Integer>();

	public Interpreter(String inputFile) {
		// read instructions into array
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = br.readLine();
			while (line != null) {
				// parse this instruction
				Instruction i;

				// skip comment and blank lines
				if (!(line.trim().length() == 0 || (line.trim().length() >= 2 && line.substring(0, 2).equals("//")))) {
					// does it have a label?
					if (line.contains(":")) {
						// extract and store the label
						String[] labelParts = line.split(":");
						labels.put(labelParts[0].trim(), instructions.size());

						// restore the rest of the line
						line = labelParts[1].trim();
					}

					// does it have an argument?
					if (line.contains(" ")) {
						String[] parts = line.split(" ");

						// int or string?
						try {
							i = new Instruction(parts[0].trim(), Integer.parseInt(parts[1].trim()));
						} catch (NumberFormatException e3) {
							i = new Instruction(parts[0].trim(), parts[1].trim());
						}
					} else {
						i = new Instruction(line.trim());
					}

					// add to array
					instructions.add(i);
				}

				// continue through the file
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			out("Input file not found.");
		} catch (IOException e2) {
			out("Error when reading from input file.");
		}
	}

	public void execute() {
		// finished?
		if (progCounter >= instructions.size()) {
			out("-- Done.");
		} else {
			// possible intArg holders
			int a1, a2;

			// get the instruction at this point
			Instruction activeInstruction = instructions.get(progCounter);

			// decide what to do
			switch (activeInstruction.getCommand()) {
				case INT:
					stack.push(activeInstruction.getIntArg());
					break;

				case ADD:
					a1 = stack.pop();
					a2 = stack.pop();
					stack.push(a1 + a2);
					break;

				case SUB:
					a1 = stack.pop();
					a2 = stack.pop();
					stack.push(a2 - a1);
					break;

				case JGE:
					a1 = stack.peek();
					if (a1 >= 0) {
						// which arg to move to?
						if (activeInstruction.getIntArg() != null) {
							progCounter = activeInstruction.getIntArg();
						} else {
							progCounter = labels.get(activeInstruction.getStrArg());
						}
						execute();
						return;
					}
					break;

				case PRINT:
					out(stack.peek());
					break;

				case PRINTALL:
					out(stack.toString());
					break;

				case SWAP:
					a1 = stack.pop();
					a2 = stack.pop();
					stack.push(a1);
					stack.push(a2);
					break;

				case CALL:
					stack.push(progCounter + 1);
					progCounter = activeInstruction.getIntArg();
					execute();
					return;

				case RET:
					progCounter = stack.pop();
					execute();
					return;

				case EXIT:
					progCounter = instructions.size();
					execute();
					return;

				case DUP:
					stack.push(stack.peek());
					break;

				case JEQ:
					a1 = stack.peek();
					if (a1 == 0) {
						// which arg to move to?
						if (activeInstruction.getIntArg() != null) {
							progCounter = activeInstruction.getIntArg();
						} else {
							progCounter = labels.get(activeInstruction.getStrArg());
						}
						execute();
						return;
					}
					break;

				case POP:
					stack.pop();
					break;
			}

			// advance
			++progCounter;

			// loop
			execute();
		}
	}

	/* CONVENIENCE METHODS */

	private static void out(String out) {
		System.out.println(out);
	}

	private static void out(int out) {
		System.out.println(out);
	}
}

public class Instruction {

	private Command command = null;
	private Integer intArg = null;
	private String strArg = null;

	public Instruction(String command) {
		this.command = Command.valueOf(command.toUpperCase());
	}

	public Instruction(String command, Integer argument) {
		this.command = Command.valueOf(command.toUpperCase());
		this.intArg = argument;
	}

	public Instruction(String command, String argument) {
		this.command = Command.valueOf(command.toUpperCase());
		this.strArg = argument;
	}

	public Command getCommand() {
		return command;
	}

	public Integer getIntArg() {
		return intArg;
	}

	public String getStrArg() {
		return strArg;
	}

	public enum Command {
		INT,
		ADD,
		SUB,
		JGE,
		PRINT,
		PRINTALL,
		SWAP,
		CALL,
		RET,
		EXIT,
		DUP,
		JEQ,
		POP
	}
}
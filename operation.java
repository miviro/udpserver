public class operation {
	private int n1, n2;
	private byte sign;

	public operation(byte[] operation) {
		String input = new String(operation);
		String[] partes = input.split("[+-/*]");

		n1 = Integer.parseInt(partes[0]);
		n2 = Integer.parseInt(partes[1]);

		// si el primer numero mide N signos, el signo estara en la posicion N
		sign = operation[partes[0].length()];
	}

	public int solve() {
		int result;
		switch (sign) {
			case '+':
				result = n1 + n2;
				break;
			case '-':
				result = n1 - n2;
				break;
			case '*':
				result = n1 * n2;
				break;
			case '/':
				result = n1 / n2;
				break;
			default:
				throw new IllegalArgumentException("Operacion no válida.");
		}
		return result;
	}
}

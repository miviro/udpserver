class udpser {
	public static void main(String[] args) {
		int PORT_NUMBER, SECRETO;
		try {
			assert args.length == 2; // udpser port_numer secreto
			PORT_NUMBER = Integer.parseInt(args[1]);
			SECRETO = Integer.parseInt(args[2]);

			assert PORT_NUMBER >= 1 && PORT_NUMBER <= 65535;
			assert SECRETO >= 0 && SECRETO <= 255;
		} catch (Exception e) {
			System.out.println("Error en el formato de los argumentos. El formato correcto es:");
			System.out.println("			udpser [numero de puerto] [secreto]");
			return;
		}

	}
}
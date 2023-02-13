import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class udpser {
	public static void main(String[] args) throws IOException {
		int PORT_NUMBER, SECRET;
		try {
			assert args.length == 2; // java udpser port_number(arg0) secreto(arg1)
			PORT_NUMBER = Integer.parseInt(args[0]);
			SECRET = Integer.parseInt(args[1]);

			assert PORT_NUMBER >= 1 && PORT_NUMBER <= 65535;
			assert SECRET >= 0 && SECRET <= 255;
		} catch (Exception e) {
			System.out.println("Error en el formato o valor de los argumentos. El formato correcto es:");
			System.out.println("			udpser [numero de puerto] [secreto]");
			return;
		}

		DatagramSocket socket = new DatagramSocket(PORT_NUMBER);
		// maxima longitud de paquete recibido: 255+255: 7 bytes
		byte[] rx = new byte[7];
		DatagramPacket rxPacket = new DatagramPacket(rx, rx.length);

		// https://www.baeldung.com/jvm-shutdown-hooks
		Thread printingHook = new Thread(() -> socket.close());
		Runtime.getRuntime().addShutdownHook(printingHook);

		// bucle infinito para que escuche siempre
		// (menos justo cuando reciba un paquete)
		while (true) {
			socket.receive(rxPacket);

			int result = solve(rxPacket.getData()) + SECRET;
			byte[] txData = String.valueOf(result).getBytes();
			DatagramPacket txPacket = new DatagramPacket(txData, txData.length, rxPacket.getAddress(),
					rxPacket.getPort());
			socket.send(txPacket);
			System.out.println("Rx: " + new String(rxPacket.getData()) + " Tx: " + new String(txPacket.getData()));
			// "limpiar" paquete al acabar con el ? TODO:
			rxPacket.setData(new byte[7]);
		}
	}

	public static int solve(byte[] operation) {
		String input = new String(operation);
		String[] partes = input.split("[+-/*]");

		// trim es necesario ya que debemos eliminar los whitespaces
		// para que parseint no tenga problemas convirtiendo
		int n1 = Integer.parseInt(partes[0].trim());
		int n2 = Integer.parseInt(partes[1].trim());

		// si el primer numero mide N signos, el signo estara en la posicion N
		byte sign = operation[partes[0].length()];
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
				// en caso de decimales, no redondear, sino truncar
				// "La operación división debe devolver la parte entera del cociente."
				// el compartamiento con n2=0 no esta especificado asi que lo dejamos crashear
				result = Math.floorDiv(n1, n2);
				break;
			default:
				throw new IllegalArgumentException("Operacion no válida.");
		}
		return result;
	}
}
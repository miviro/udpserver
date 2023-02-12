import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class udpser {
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
			System.out.println("Rx: " + rxPacket.getData());

			int result = new operation(rxPacket.getData()).solve() + SECRET;
			byte[] txData = String.valueOf(result).getBytes();
			DatagramPacket txPacket = new DatagramPacket(txData, txData.length, rxPacket.getAddress(),
					rxPacket.getPort());
			socket.send(txPacket);
		}
	}
}
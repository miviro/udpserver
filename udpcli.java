import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class udpcli {
	public static void main(String[] args) throws IOException {
		InetAddress IP_ADDR;
		byte[] OPERATION;
		int PORT_NUMBER;
		try {
			assert args.length == 3;

			IP_ADDR = InetAddress.getByName(args[0]);
			PORT_NUMBER = Integer.parseInt(args[1]);
			OPERATION = args[2].getBytes();

			assert PORT_NUMBER >= 1 && PORT_NUMBER <= 65535;
			// ignoramos comprobacion ip addr y operacion
		} catch (Exception e) {
			System.out.println("Error en el formato de los argumentos. El formato correcto es:");
			System.out.println("			udpcli [ip del servidor] [numero de puerto] [operacion]");
			System.out.println("Ejemplo:");
			System.out.println("			updcli 127.0.0.1 8887 255+255");
			return;
		}

		DatagramPacket txPacket = new DatagramPacket(OPERATION, OPERATION.length, IP_ADDR, PORT_NUMBER);
		DatagramSocket socket = new DatagramSocket();
		socket.send(txPacket);

		// maxima longitud recibida: 255*255 = 65025 (5 bytes)
		byte[] rxData = new byte[5];
		DatagramPacket rxPacket = new DatagramPacket(rxData, rxData.length);

		// esperar 10s por la respuesta
		socket.setSoTimeout(10000);
		try {
			socket.receive(rxPacket);
			System.out.println(new String(rxPacket.getData()));
		} catch (SocketTimeoutException e) {
			System.out.println("El paquete fue enviado, pero no se obtuvo respuesta tras 10s. Cerrando programa.");
		}
		socket.close();
	}
}

package client;

import java.io.IOException;
import java.net.Socket;

public class CMain {

	public static void main(String[] args) throws Exception, IOException {
		Socket withServer = null;

		withServer = new Socket("10.0.0.53", 9999);

		new Client(withServer);

	}

}

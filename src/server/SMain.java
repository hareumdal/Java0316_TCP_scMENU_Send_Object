package server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SMain {

	public static void main(String[] args) throws Exception {

		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress("10.0.0.53", 9999));

		Socket withClient = null;

		ServerCenter sc = ServerCenter.getInstance();
		while (true) {
			System.out.println("서버 대기 중");
			withClient = server.accept();
			System.out.println(withClient.getInetAddress() + "클라이언트 접속함");
			Server s = new Server(withClient);
			sc.addServer(s);
			s.start();
		}
	}
}

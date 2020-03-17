package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import object.MenuObject;

public class Client {
	private Socket withServer = null; // 클라이언트가 누가 될지 모르니까 일단 null
	private InputStream reMsg = null;
	private OutputStream sendMsg = null;
	private String IdPwd = null;
	private Scanner input = new Scanner(System.in);
	private ObjectInputStream ois = null;
	private ArrayList<MenuObject> list = new ArrayList<>();
	Client(Socket c) throws Exception {
		this.withServer = c;
		streamSet();
		receive();
		send();
	}

	private void send() {
		new Thread(new Runnable() { // 참조 변수가 없는 객체: 익명의 객체
			@Override
			public void run() {
				try {
					while (true) {
						String msg = input.nextLine();
						sendMsg = withServer.getOutputStream();
						sendMsg.write(msg.getBytes());
//						if (msg.equals("/menu")) {
//					
//							try {
//								System.out.println("객체 받을 준비 중..");
//								reMsg = withServer.getInputStream();
//								ois = new ObjectInputStream(reMsg);
//								MenuObject mo = (MenuObject) ois.readObject();
//								list.add(mo);
//								System.out.println(list);
//								System.out.println("받았나?");
//								System.out.println(mo.getItems());
//								System.out.println("받기 완료");
//							} catch (ClassNotFoundException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
							//ArrayList<MenuObject> list = new ArrayList<>();
							
					}
				} catch (IOException e) {
					System.out.println("메세지를 주고 받을 수 없습니다.");
				}
			}
		}).start();
	}

	private void receive() { // 메소드에서 독자적으로 스레드 실행

		new Thread(new Runnable() { // Ctrl+spacebar
			@Override
			public void run() {
				try {
					while (true) {
						
						
						reMsg = withServer.getInputStream();
						byte[] reBuffer = new byte[100];
						reMsg.read(reBuffer);
						String msg = new String(reBuffer);
						msg = msg.trim(); // 공백이 있다면 제거
						System.out.println(msg);
					}
				} catch (Exception e) {
					System.out.println("메세지를 주고 받을 수 없습니다.");
				}
			}
		}).start();

	}

	private void streamSet() throws Exception {
		try {
			while (true) {
				System.out.println("ID/pwd를 입력하세요>");
				IdPwd = input.nextLine();
				sendMsg = withServer.getOutputStream();
				sendMsg.write(IdPwd.getBytes());

				reMsg = withServer.getInputStream(); // socket으로부터 받은 자원을 가져와라
				byte[] reBuffer = new byte[100];
				reMsg.read(reBuffer);
				String msg = new String(reBuffer);
				msg = msg.trim(); // 공백이 있다면 제거
				System.out.println(msg);

				if (msg.equals("ok")) { // 로그인이 성공했을 경우
					reMsg = withServer.getInputStream(); // socket으로부터 받은 자원을 가져와라
					reMsg.read(reBuffer);
					msg = new String(reBuffer);
					msg = msg.trim(); // 공백이 있다면 제거
					System.out.println(msg);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("메세지를 주고 받을 수 없습니다.");
		}

	}

}

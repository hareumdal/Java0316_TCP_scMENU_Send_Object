package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import object.MenuObject;

public class Server extends Thread {
	private Socket withClient = null; // 클라이언트와 통신하기 위해서
	private InputStream reMsg = null;
	private OutputStream sendMsg = null;
	private String IdPwd = null;

	public Socket getSocket() {
		return withClient;
	}
	public String getID() {
		return IdPwd;
	}

	private ArrayList<MenuObject> chooseMenu = new ArrayList<>();

	public ArrayList<MenuObject> getChooseMenu() {
		return chooseMenu;
	}

	public void addChooseMenu(MenuObject menu) {
		chooseMenu.add(menu);
	}

	Server(Socket c) {
		this.withClient = c;
		// streamSet();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		streamSet();
		receive();

	}

	private void receive() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					System.out.println("receive start~~");
					while (true) {

						reMsg = withClient.getInputStream();
						byte[] reBuffer = new byte[100];
						reMsg.read(reBuffer);
						String msg = new String(reBuffer);
						msg = msg.trim();

						System.out.println(msg);

						ServerCenter.getInstance().reMsg(IdPwd, msg);
					}
				} catch (Exception e) {
					System.out.println("receive End");
					return;
				}
			}
		}).start();

	}

	public void send(String msg) {

		try {
			System.out.println("send start~~");

			sendMsg = withClient.getOutputStream();
			sendMsg.write(msg.getBytes());
			System.out.println("보냄");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("send End");
			return;
		}
	}

	private void streamSet() {
		try {
			while (true) {
				reMsg = withClient.getInputStream();
				byte[] reBuffer = new byte[100];
				reMsg.read(reBuffer);
				IdPwd = new String(reBuffer);
				IdPwd = IdPwd.trim();
				boolean loginSign = login();
				String loginMSG = "";

				if (loginSign) { // 로그인이 되었다면
					loginMSG = "ok";
					sendMsg = withClient.getOutputStream();
					sendMsg.write(loginMSG.getBytes());
					InetAddress c_ip = withClient.getInetAddress();
					String ip = c_ip.getHostAddress();
					sendMsg = withClient.getOutputStream();
					System.out.println(IdPwd + "님 로그인 (" + ip + ")");
					loginMSG = "정상접속 되었습니다.";
					sendMsg = withClient.getOutputStream();
					sendMsg.write(loginMSG.getBytes());
					break;

				} else { // 로그인에 실패했다면
					loginMSG = "fail";
					sendMsg = withClient.getOutputStream();
					sendMsg.write(loginMSG.getBytes());
				}
			}

		} catch (Exception e) {
			System.out.println("메세지를 주고 받을 수 없습니다.");
		}

	}

	private boolean login() {
		for (String member : ServerCenter.getInstance().getMList()) {
			if (IdPwd.equals(member)) {
				return true;
			}
		}
		return false;
	}
	
}

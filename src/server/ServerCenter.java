package server;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

import object.MenuObject;

public class ServerCenter {

	private Server whoServer = null;
	private ArrayList<Server> sList = new ArrayList<>();

	private ArrayList<String> mList = new ArrayList<>();

	public ArrayList<String> getMList() {
		return mList;
	}

	private ArrayList<MenuObject> menuList = new ArrayList<>();

	public ArrayList<MenuObject> getMenuList() {
		return menuList;
	}

	public void setMList() {
		mList.add("aaa/111");
		mList.add("bbb/222");
		mList.add("ccc/333");
	}

	public void setMenuList() {
		menuList.add(new MenuObject("라면", 3000));
		menuList.add(new MenuObject("치킨", 2000));
		menuList.add(new MenuObject("핫도그", 1000));
	}

	private static ServerCenter serverCenter = null;

	private ServerCenter() {
		setMList();
		setMenuList();
		System.out.println(getMList());
		System.out.println(getMenuList());
	}

	public static ServerCenter getInstance() {
		if (serverCenter == null) {
			serverCenter = new ServerCenter();
		}
		return serverCenter;
	}

	public void addServer(Server s) {
		this.sList.add(s);
	}

	public void reMsg(String idpwd, String msg) throws Exception {
		System.out.println("reMs() " + msg);
		if (msg.charAt(0) == '/') {

			AnalSignal(msg, idpwd);

		} else {
			System.out.println("보내조");

			sendAll("[" + analID(idpwd) + "]" + msg);
		}
	}

	private String analID(String idpwd) {
		return idpwd.substring(0, idpwd.indexOf('/'));
	}

	private void AnalSignal(String msg, String idpwd) throws Exception {

		whoServer = who(idpwd);
		String id = whoServer.getID().substring(0, whoServer.getID().indexOf("/"));
//		switch (subOrder(msg)) {
//		case "/menu": // 메뉴 목록 보여주기
//			System.out.println("switch");
//			showMenu(whoServer);
//			break;
//		case "/choose": // 메뉴 선택
//			selectMenu(msg, whoServer);
//			break;
//		case "/count": // 정산
//			settle(whoServer);
//			break;
//		case "/wisper": // 귓속말
//			wisperAnalMsg(msg);
//			break;
//		default: // 저장 되지 않은 명령어 // 전체에게 보내기?
//			sendAll("[" + analID(idpwd) + "]" + msg);
//			break;
//		}

		if (msg.equals("/menu")) {// 메뉴를 보여 달라고 했을 경우에
			showMenu(whoServer);
		} else if (msg.equals("/coast")) {
			System.out.println("사용자가 /coast를 선택했음\n settle()실행할 것");
			settle(whoServer);
		} else if (subOrder(msg).equals("/choose")) {
			selectMenu(msg, whoServer);
		} else if (subOrder(msg).equals("/wisper")) {
			wisperAnalMsg(msg, id);
		} else {
			sendAll("[" + analID(idpwd) + "]" + msg);
		}

		for (MenuObject c : whoServer.getChooseMenu()) {
			System.out.println(c.getCoast());
		}
	}

//	private String cutMsg(String msg) {
//		msg.substring(msg.indexOf(" ") + 1, msg.length());
//		System.out.println();
//		return null;
//	}

	private void settle(Server whoServer) throws Exception {
		System.out.println("settle()이 실행 중");
		int total = 0;
		for (MenuObject ateMenu : whoServer.getChooseMenu()) {
			total = total + ateMenu.getCoast();
			System.out.println("total: " + total);
			System.out.println(ateMenu.getItems() + "/" + ateMenu.getCoast());
			whoServer.send(ateMenu.getItems() + "/" + ateMenu.getCoast());
			Thread.sleep(1);
		}
		whoServer.send("총합계: " + String.valueOf(total));
	}

	private String chooseMenu = "";

	private void selectMenu(String msg, Server idpwd) {
		chooseMenu = msg.substring(msg.indexOf(" ") + 1, msg.length());
		for (MenuObject menu : menuList) { // 없는 메뉴를 골랐다면 곤란하니까 확인해 보자
			if (chooseMenu.equals(menu.getItems())) {
				whoServer.addChooseMenu(menu);
				System.out.println(chooseMenu); // 선택한 메뉴 보여주기
				whoServer.send(chooseMenu + "가 선택 되었습니다.");
				break;
			}
		}
	}

	private String subOrder(String msg) {
		return msg.substring(0, msg.indexOf(" "));
	}

	private Server who(String idpwd) {
		for (Server s : sList) {
			if (s.getID().equals(idpwd)) {
				return s;
			}
		}
		return null;
	}

//private ObjectOutputStream obOut = null;
	private void showMenu(Server whoServer) {

		try {
			for (MenuObject mo : menuList) {
				System.out.println(mo.getItems());
			}

			System.out.println("메뉴 객체 보내주기");

			for (MenuObject mo : menuList) {
				menuList.get(menuList.size() - 1).setEndWord("end");
				ObjectOutputStream obOut = new ObjectOutputStream(whoServer.getSocket().getOutputStream()); // 새로 열어주지
																											// 않으면 한 번만
																											// 전송 되고,
																											// 클라이언트가 한
																											// 번 받고 더는 못
																											// 받음
				// 하나 보낼 때마다 새로 객체 생성해줘야 함
				if (mo != null) {
					System.out.println(mo);

					obOut.writeObject(mo);
					// obOut.flush();
					Thread.sleep(3000);
					System.out.println("object 변환/보내기 완료");
				}
			}

//			for (MenuObject menu : menuList) {
//				whoServer.send("[Server] " + menu.getItems() + "/" + menu.getCoast());
//				Thread.sleep(1);
//			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

	}

	private void wisperAnalMsg(String msg, String ids) {
		int firstInt = msg.indexOf(" ") + 1;
		int endInt = msg.indexOf(" ", firstInt);
		String id = msg.substring(firstInt, endInt);
		System.out.println(id);
		String idMsg = msg.substring(endInt + 1);
		System.out.println(idMsg);
		for (Server s : sList) {
			if (s.getID().substring(0, s.getID().indexOf("/")).equals(id)) {
				s.send("[@" + ids + "] " + idMsg);
				break;
			}
		}
	}

	public void sendAll(String msg) {
		for (int i = 0; i < sList.size(); i++) {
			sList.get(i).send(msg);
		}
	}

}

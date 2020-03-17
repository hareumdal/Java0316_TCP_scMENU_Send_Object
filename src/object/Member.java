package object;
import java.util.ArrayList;

public class Member {
	private String id = "";
	private String pwd = "";
	
	private ArrayList<Member> member = new ArrayList<>();
	
	public Member(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public ArrayList<Member> getMember() {
		return member;
	}


	
}

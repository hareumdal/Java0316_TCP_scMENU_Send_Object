import static org.junit.Assert.*;

public class Test {

	@org.junit.Test
	public void test() {

		String msg = "/message hi there";
		System.out.println(msg.substring(msg.indexOf(" ") + 1, msg.length()));
		System.out.println(msg.substring(0, msg.indexOf("/")));

	}

}

package object;

import java.io.Serializable;

public class MenuObject implements Serializable{
	private String item = "";
	private int coast = 0;

	public MenuObject(String item, int coast) {
		this.item = item;
		this.coast = coast;
	}

	public String getItems() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getCoast() {
		return coast;
	}

	public void setCoast(int coast) {
		this.coast = coast;
	}
}

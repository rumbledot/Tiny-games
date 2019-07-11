package userinterface;
import java.awt.Color;

import ecs100.UI;

public class Finish implements Element {
	//trigger end of a level block
	private double x;
	private double y;
	private Color color;
	private double size;
	private String name;
	private String blockName = " ";
	
	public Finish (double x, double y, double size, Color c) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = Color.black;
		this.name = "finish";
	}
	
	public void Draw() {
		UI.setColor(color);
		UI.fillRect(x, y, size, size);
		UI.setColor(Color.black);
	}
	public Color getColor() {
		return color;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBlockName(String blockname) {
		this.blockName = blockname;
	}
	public String getBlockName() {
		return this.blockName;
	}
}

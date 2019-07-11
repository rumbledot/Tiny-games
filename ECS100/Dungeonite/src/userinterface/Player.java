package userinterface;

import java.awt.Color;

import ecs100.UI;

public class Player implements Element {
	//controllable block
	private double x;
	private double y;
	private Color color;
	private String name;
	private double size;
	private String blockName = " ";

	public Player (double x, double y, double size, Color c) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = Color.blue;
		this.name = "player";
	}
	
	public void Draw() {
		
		UI.setColor(color);
		UI.fillOval(x, y, size, size);
		UI.setColor(Color.black);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Color getColor() {
		return color;
	}
	public void setBlockName(String blockname) {
		this.blockName = blockname;
	}
	public String getBlockName() {
		return this.blockName;
	}
}

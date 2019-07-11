package userinterface;

import java.awt.Color;

import ecs100.UI;

public class Block implements Element {
	//block player's movement
	private double x;
	private double y;
	private double size;
	private Color color;
	private String name;
	private String blockName = " ";
	
	public Block (double x, double y, double size, Color c) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = c;
		this.name = "block";
	}
	
	public void Draw() {
		UI.setColor(color);
		UI.fillRect(x, y, size, size);
		UI.setColor(Color.black);
	}
	public Color getColor() {
		return this.color;
	}
	public String getName() {
		return this.name;
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

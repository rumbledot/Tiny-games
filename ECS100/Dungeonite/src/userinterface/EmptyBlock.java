package userinterface;

import java.awt.Color;

import ecs100.UI;

public class EmptyBlock implements Element {

	private double x;
	private double y;
	private double size;
	private Color color;
	private String name;
	private String blockName = " ";

	public EmptyBlock (double x, double y, double size, Color c) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = Color.white;
		this.name = "empty";
	}

	public void Draw() {
		UI.setColor(color);
		UI.fillRect(x, y, size, size);
		UI.setColor(Color.gray);
		UI.drawRect(x, y, size, size);
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

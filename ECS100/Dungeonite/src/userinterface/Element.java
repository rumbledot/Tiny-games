package userinterface;

import java.awt.Color;

public interface Element {
	/* basic 'block' object behaviour
	 * 
	 * Block 		: block player's movement
	 * Apple 		: collectible block
	 * Player		: controllable block
	 * EmptyBlock 	: walkable block
	 * Finish		: trigger block
	 * 
	 * future expansion
	 * Enemy		: moving enemy with simple AI
	 * Items		: collectible like weapons, potions, keys
	 * Door			: interactive block with certain Items
	 * Trigger		: invisible block that trigger an event
	 * 
	 */
	public String getName();
	public void setName(String name);
	public Color getColor();
	public void Draw();
	public void setBlockName(String blockname);
	public String getBlockName();
	
}

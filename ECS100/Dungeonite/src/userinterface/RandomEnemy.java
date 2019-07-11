package userinterface;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import ecs100.UI;
/**
 * 
 * For future refinement
 *
 */
public class RandomEnemy extends UserInterface {

	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	Enemy activeEnemy;
	Enemy activePlayer;
	UserInterface ui;
	
	public RandomEnemy(UserInterface ui) {
		this.ui = ui;
		
		Enemy e = new Enemy("blob1.ppm", 10, 4);
		enemies.add(e);
		e = new Enemy("angry blob.ppm", 15, 6);
		enemies.add(e);
		e = new Enemy("skull.ppm", 20, 8);
		enemies.add(e);
		e = new Enemy("player_d.ppm", super.playerHealth, super.playerMagic);
		activePlayer = e;
		UI.println(enemies.size());

		this.pickRandomEnemy();
		String actionTaken = ""; int def = 0, damage = 0;
		while (activeEnemy.check() && activePlayer.check()) {
			//player's move
			actionTaken = UI.askString("[A]ttack! [D]efend [R]etreat");

			if (actionTaken.equalsIgnoreCase("a")) {
				UI.setColor(Color.red);
				UI.drawString("ATTACK!", 300, 50);
				damage = (int) (Math.random() * (activePlayer.getAttackPower())) + 1;
				UI.drawString(Integer.toString(damage), 300, 200);
				activeEnemy.takeHit(damage);
				UI.setColor(Color.black);
			}
			else if (actionTaken.equalsIgnoreCase("d")) {
				UI.setColor(Color.blue);
				UI.drawString("DEFEND", 300, 50);
				def = 2;
				UI.drawString(Integer.toString(def), 50, 180);
				UI.setColor(Color.black);
			}
			else if (actionTaken.equalsIgnoreCase("r")) {
				UI.setColor(Color.blue);
				UI.drawString("RUN...", 300, 50);
				int runChance = (int)(Math.random() * 100);
				if (runChance > 25 && runChance < 50) {
					this.backToMain();
				} else {
					UI.setColor(Color.DARK_GRAY);
					UI.drawString("FAILED", 300, 50);
				}
				UI.setColor(Color.black);
			} else {
				UI.setColor(Color.black);
				UI.drawString("wrong action", 300, 50);
			}

			try
			{
				Thread.sleep(1200);
			}
			catch(InterruptedException ex)
			{
				Thread.currentThread().interrupt();
			}

			//enemy's move
			int enemyMove = (int)(Math.random() * 100);
			if (enemyMove > 15 && enemyMove < 50) {
				UI.setColor(Color.red);
				UI.drawString("ATTACK!", 50, 400);
				damage = (int) (Math.random() * (activeEnemy.getAttackPower())) + 1;
				damage -=def;
				if(damage < 0) { damage = 0; }
				def = 0;
				UI.drawString(Integer.toString(damage), 50, 200);
				activePlayer.takeHit(damage);
				UI.setColor(Color.black);
			} else {
				UI.setColor(Color.blue);
				UI.drawString("DEFEND", 50, 400);
				def = activeEnemy.getAttackPower() / 3;
				UI.drawString(Integer.toString(def), 250, 400);
				UI.setColor(Color.black);
			}

			try
			{
				Thread.sleep(1200);
			}
			catch(InterruptedException ex)
			{
				Thread.currentThread().interrupt();
			}
			UI.clearPanes();
			this.updateStat();
		}
		this.backToMain();
	}
	
	private void backToMain() {
		if (!activeEnemy.check() && activePlayer.check()) { 
			super.score += 20;
			super.playerHealth = activePlayer.health();
		} else if (!activePlayer.check() && activeEnemy.check()) {
			super.isAlive = false;
			UserInterface ui = new UserInterface();
			ui.finishGame();
		}
		super.isEnemy = false;
		//new UserInterface();
	}
	
	private void pickRandomEnemy() {
		
		int	ran = (int)(Math.random() * 100);
		
		if (ran > 10 && ran < 60) { activeEnemy = enemies.get(0); }
		else if (ran > 60 && ran < 90) { activeEnemy = enemies.get(1); }
		else if (ran > 90 && ran < 100) { activeEnemy = enemies.get(2); }
		else { activeEnemy = enemies.get(0); }
		
		UI.drawString("Oh ow.. ambushed!!", 275, 275);
		this.updateStat();
	}
	
	private void updateStat() {
		activePlayer.drawImage(0, 0, 5);
		activePlayer.displayHBar(50, 155);
		activeEnemy.drawImage(50, 50, 5);
		activeEnemy.displayHBar(300, 400);
	}
	
}

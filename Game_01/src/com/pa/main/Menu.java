package com.pa.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {

	public String[] options = {"novo jogo","carregar jogo", "sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			}else if (options[currentOption] == "sair") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		//g.setColor(Color.black);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,245));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(new Color(191,44,154));
		g.setFont(new Font ("arial", Font.BOLD, 36));
		g.drawString("A Estrela do Deserto", (Game.WIDTH*Game.SCALE)/2 -185, (Game.HEIGHT*Game.SCALE)/2 -125);
		
		// Op��es de Menu
		g.setColor(Color.white);
		g.setFont(new Font ("arial", Font.BOLD, 24));
		if(pause == false) {
		g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE)/2 -75, (Game.HEIGHT*Game.SCALE)/2 -55);}
		else {g.drawString("Continuar", (Game.WIDTH*Game.SCALE)/2 -72, (Game.HEIGHT*Game.SCALE)/2 -55);}
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE)/2 -95, (Game.HEIGHT*Game.SCALE)/2 -15);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE)/2 - 38, (Game.HEIGHT*Game.SCALE)/2 +30);
		
		if(options[currentOption] == "novo jogo") {
			g.drawString("-", (Game.WIDTH*Game.SCALE)/2 -100, (Game.HEIGHT*Game.SCALE)/2 -55);
		}else if (options[currentOption] == "carregar jogo") {
			g.drawString("-", (Game.WIDTH*Game.SCALE)/2 -120, (Game.HEIGHT*Game.SCALE)/2 -15);
		}else if (options[currentOption] == "sair") {
			g.drawString("-", (Game.WIDTH*Game.SCALE)/2 -65, (Game.HEIGHT*Game.SCALE)/2 +30);
		}
	}
}
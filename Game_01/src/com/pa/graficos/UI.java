package com.pa.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.pa.entities.Player;
import com.pa.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(180, 10, 50, 8);
		g.setColor(Color.green);
		g.fillRect(180, 10, (int)((Game.player.life/Game.player.maxlife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxlife,193,17);
	}
}

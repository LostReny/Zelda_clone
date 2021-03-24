package com.pa.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.pa.graficos.Spritesheet;
import com.pa.main.Game;
import com.pa.main.Sound;
import com.pa.world.Camera;
import com.pa.world.World;

public class Player extends Entity {
	
	public boolean right,up,left,down;
	public int right_dir = 0, left_dir = 1, up_dir = 3, down_dir = 4;
	public int dir = down_dir;
	public double speed = 1.2;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage playerDamage;
	
	private boolean arma = false;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public int ammo = 0;
	
	public boolean shoot = false, mouseShoot = false;
	
	public double life = 100, maxlife= 100;
	
	public int mx,my;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage [4];
		upPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
	
		for(int i = 0; i <4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
			}
		for(int i = 0; i <4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
			}
		for(int i = 0; i<4; i++) {
			downPlayer [i] = Game.spritesheet.getSprite(32 + (i*16), 32, 16, 16);
			}	
		for(int i =0; i <4; i++) {
			upPlayer [i] = Game.spritesheet.getSprite(32 + (i*16), 48, 16, 16);
			}
		}
		
	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			dir = up_dir;
			moved = true;
			y-=speed;
		} else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			dir = down_dir;
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		}
		
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 3) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			//criar bala e atirar
			shoot = false;
			if(arma && ammo > 0) {
			ammo --;
			//System.out.println("Atirando");
			int dx = 0;
			int px = 0;
			int py = 0;
			if(dir == right_dir) {
				px = 20;
				py = 6;
				dx = 1;
			}else if (dir == left_dir){
				px= -8;
				py = 6;
				dx = -1;
			}
			int dy = 0;
			if (dir == up_dir) {
				px = 1;
				py = 6;
				dy = -1;
			}else if (dir == down_dir) {
				px = 10;
				py = 18;
				dy = 1;
			}
			
			BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 3,3, null, dx, dy);
			Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			// Arrumar o teste de atirar com o mouse
			
			if(arma && ammo > 0) {
			ammo --;
			//System.out.println("Atirando");
			int px = 0;
			int py = 8;
			double angle = 0;
			
			if(dir == right_dir) {
				px = 20;
				//py = 6;
				angle = Math.toDegrees(Math.atan2( my - (this.getY()+py- Camera.y),mx - (this.getX()+px- Camera.x)));
			
			}else if (dir == left_dir){
				px= -8;
				//py = 6;
				angle = Math.toDegrees(Math.atan2( my - (this.getY()+py- Camera.y),mx - (this.getX()+px- Camera.x)));
				
			}
			if (dir == up_dir) {
				px = 1;
				//py = 6;
				angle = Math.toDegrees(Math.atan2( my - (this.getY()+py- Camera.y),mx - (this.getX()+px- Camera.x)));
				
			}else if (dir == down_dir) {
				px = 10;
				//py = 18;
				angle = Math.toDegrees(Math.atan2( my - (this.getY()+py- Camera.y),mx - (this.getX()+px- Camera.x)));
			}
			
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			
			BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 3,3, null, dx, dy);
			Game.bullets.add(bullet);
			}
		}
		
		
		if(life <= 0) {
			/*if(Game.player.life <= 0) {
					//Game Over
					System.exit(1);
				}*/
			//GAME OVER
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0 ; i < Game.entities.size(); i++ ) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual)) {
					ammo+=15;
					//System.out.println("Muni��o = " + ammo);
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	
	public void checkCollisionLifePack() {
		for(int i = 0 ; i < Game.entities.size(); i++ ) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isColliding(this, atual)) {
					life+=10;
					if(life > 100)
						life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionGun() {
		for(int i = 0 ; i < Game.entities.size(); i++ ) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual)) {
					arma = true;
					//System.out.println("Pegou a arma do ch�o");
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	
	public void render(Graphics g) {
		if(! isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					//desenhar arma para direita
					g.drawImage(Entity.GUN_RIGHT, this.getX() +11 - Camera.x,this.getY()-2 -Camera.y,null);
				}
			}else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					//desenhar arma para esquerda
					g.drawImage(Entity.GUN_LEFT, this.getX() -11 - Camera.x,this.getY()-2 -Camera.y,null);
				}
			}
			if(dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					//desenhar arma para cima
					g.drawImage(Entity.GUN_UP, this.getX()-5 - Camera.x,this.getY() +4  -Camera.y,null);
				}
			}else if (dir == down_dir) {
				g.drawImage(downPlayer[index],this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					//desenhar arma para baixo
					
					g.drawImage(Entity.GUN_DOWN, this.getX()+4 - Camera.x,this.getY() +8 -Camera.y,null);
				}
			}
		} else {
			g.drawImage(playerDamage, this.getX() -Camera.x, this.getY() -Camera.y, null);
			if(arma) {
				//desenhar arma para direita
				if(arma) {
					if(dir == left_dir) {
						g.drawImage(Entity.GUN_DAMAGE_LEFT, this.getX() -11 - Camera.x,this.getY()-2 -Camera.y,null);
					}else if (dir == right_dir) {
						g.drawImage(Entity.GUN_DAMAGE_RIGHT, this.getX() +11 - Camera.x,this.getY()-2 -Camera.y,null);
					}else if (dir == up_dir) {
						g.drawImage(Entity.GUN_DAMAGE_UP, this.getX()-5 - Camera.x,this.getY() +4  -Camera.y,null);
					}else {
						g.drawImage(Entity.GUN_DAMAGE_DOWN, this.getX()+4 - Camera.x,this.getY() +8 -Camera.y,null);
					}
				}
			}
		}
		
		}
	}

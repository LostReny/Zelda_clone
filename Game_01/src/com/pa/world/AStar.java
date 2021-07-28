package com.pa.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AStar {

	// Node classe criada agora para guardar posi��es - existe um no java, mas n�o � esse 
	
	public static double lastTime = System.currentTimeMillis();
	private static Comparator<Node> nodeSorter = new Comparator<Node>() {
		
		
		@Override
		
		public int compare(Node n0, Node n1) {
			if(n1.fCost < n0.fCost)
				return +1;
			if(n1.fCost > n0.fCost)
				return -1;
			return 0;
		}
	};
	
	public static boolean clear() {
		if (System.currentTimeMillis() - lastTime >= 1000) {
			return true;
		}
		return false;
	}
	
	public static List<Node> findPath(World world, Vector2i start, Vector2i end){
		lastTime = System.currentTimeMillis();
		List<Node> openList = new ArrayList<Node>();
		List<Node> closeList = new ArrayList<Node>();
		
		Node current = new Node(start, null, 0, getDistance(start, end));
		
		openList.add(current);
		while(openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if(current.tile.equals(end)) {
				//Chegamos no ponto final
				//Basta retornar o valor
				List<Node> path = new ArrayList<Node>();
				while(current.parent !=null) {
					path.add(current);
					current = current.parent;
				}
				//otimiza��o, limpando as listas - n�o ficar guardado na mem�ria
				openList.clear();
				closeList.clear();
				return path;
			}
			
			openList.remove(current);
			closeList.add(current);
			
			// CORA��O ALGORITMO
			// pegar todas posi��es vizinhas do inimigo e ver pra onde ele pode andar - caderno
			for(int i = 0; i < 9; i++) {
				// posi��o 4 � onde o inimigo est�
				if (i == 4) continue;
				// looping que acontece uma vez
				 int x = current.tile.x;
				 int y = current.tile.y;
				 int xi = (i%3) - 1;
				 int yi = (i/3) - 1;
				 //verifica se � um espa�o livre- open list, ou s�lido - uma parede
				 Tile tile = World.tiles[x+xi+((y+yi)*World.WIDTH)];
				 if(tile == null) continue;
				 if(tile instanceof WallTile) continue; 
				 if(i == 0) {
					 // diagonais 
					 // necess�rio para inimigo conseguir andar nas diagonais, al�m das verticais e horizontais
					 Tile test = World.tiles[x+xi+1 +((y+yi) * World.WIDTH)];
					 Tile test2 = World.tiles[x+xi+1 +((y+yi) * World.WIDTH)];
					 if(test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 else if(i == 2) {
					 Tile test = World.tiles[x+xi+1 +((y+yi) * World.WIDTH)];
					 Tile test2 = World.tiles[x+xi +((y+yi) * World.WIDTH)];
					 if(test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 
				 else if(i == 6) {
					 Tile test = World.tiles[x+xi +((y+yi-1) * World.WIDTH)];
					 Tile test2 = World.tiles[x+xi+1 +((y+yi) * World.WIDTH)];
					 if(test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 else if(i == 8) {
					 Tile test = World.tiles[x+xi +((y+yi-1) * World.WIDTH)];
					 Tile test2 = World.tiles[x+xi-1 +((y+yi) * World.WIDTH)];
					 if(test instanceof WallTile || test2 instanceof WallTile) {
						 continue;
					 }
				 }
				 
				 // achar o caminho encontrando o ponto, e pegando o caminho +curto
				 Vector2i a = new Vector2i(x+xi, y+yi);
				 double gCost = current.gCost + getDistance(current.tile,a);
				 double hCost = getDistance(a, end);
				 
				 Node node = new Node(a,current,gCost,hCost);
				 
				 // se a posi��o j� existir na lista - continua, se n�o existe - abre lista e dps remove = otimiza��o
				 if(vecInList(closeList,a) && gCost >= current.gCost) continue;
				 if(!vecInList(openList,a )) {
					 openList.add(node);
				 } else if(gCost < current.gCost) {
					 openList.remove(current);
					 openList.add(node);
				 }
			}
		}
		closeList.clear();
		return null;
	}
	
	
	private static boolean vecInList(List<Node> list, Vector2i vector) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).tile.equals(vector)) {
				return true;
			}
		} return false;
	}
	
	private static double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt(dx*dx + dy*dy);
		
	}
	
}

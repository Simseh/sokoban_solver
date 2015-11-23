import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class SokobanSolver {
	
	private HashSet<Coordinate> walls;
	private HashSet<Coordinate> goals;
	private HashSet<Coordinate> diamonds;
	
	private Coordinate player;
	private Problem prob;
	private Heuristics h;
	
	private int row;
	private int col;
	private int diamondAmount;
	
	public SokobanSolver() {
	}
	
	public int loadFile(String filename, char hChoice) throws FileNotFoundException, 
			NumberFormatException, NoSuchElementException {
		
		int numPlayer = 0;
		walls = new HashSet<Coordinate>();
		goals = new HashSet<Coordinate>();
		diamonds = new HashSet<Coordinate>();
		Scanner s = new Scanner(new File(filename));
		col = Integer.parseInt(s.nextLine());
		row = Integer.parseInt(s.nextLine());
		diamondAmount = Integer.parseInt(s.nextLine());
		for (int i=0; i<row; i++) {
			String next = s.nextLine();
//			for (int j=0; j<next.length(); j++) {
//				char c = next.charAt(j);
//				if (c=='#') //walls
//					walls.add(new Coordinate(i, j));
//				if (c == '@' || c == '+') { //player
//					player = new Coordinate(i, j);
//					numPlayer++;
//				}
//				if (c == '.' || c == '+' || c == '*') //goals
//					goals.add(new Coordinate(i, j));
//				if (c == '$' || c == '*') //diamonds
//					diamonds.add(new Coordinate(i,j));
//			}
			for (int j=0; j<next.length(); j++) {
				char c = next.charAt(j);
				if (c=='X') //walls
					walls.add(new Coordinate(i, j));
				if (c == 'M') { //player
					player = new Coordinate(i, j);
					numPlayer++;
				}
				if (c == 'G') //goals
					goals.add(new Coordinate(i, j));
				if (c == 'J') //diamonds
					diamonds.add(new Coordinate(i,j));
			}
			if (next.length() > col)
				col = next.length();
		}
		prob = new Problem(walls, new State(diamonds, player), goals);
		h = new Heuristics(goals, hChoice);
		System.out.println("row: " + row + ", col: " + col + ", diamonds: " + diamondAmount);
		return numPlayer;
	}
	
	public String solve(char method) {
		Search s = new Search(h);
		switch(method) {
		// breadth-first
		case 'b':
			return s.bfs(prob);
		// depth-first
		case 'd':
			return s.dfs(prob);
		default:
			return "Invalid search method, please choose something valid.";
		}
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}

	public HashSet<Coordinate> getWalls() {
		return walls;
	}
	
	public HashSet<Coordinate> getDiamonds() {
		return diamonds;
	}
	
	public HashSet<Coordinate> getGoals() {
		return goals;
	}
	
	public Coordinate getPlayer() {
		return player;
	}
}

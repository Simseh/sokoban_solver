import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class Problem {

	State initialState;
	HashSet<Coordinate> walls;
	HashSet<Coordinate> goals;
	HashMap<Coordinate, Coordinate> blocked;

	public Problem(HashSet<Coordinate> walls, State initialState, HashSet<Coordinate> goals) {
		this.initialState = initialState;
		this.walls = walls;
		this.goals = goals;
	}

	public boolean goalTest(State state) {
		for(Coordinate diamond : state.diamonds)
			if (!goals.contains(diamond))
				return false;
		return true;
	}
	

	public boolean deadlockTest(State state) {
		for (Coordinate diamond : state.diamonds) {
			int row = diamond.row;
			int col = diamond.col;
			if (!setContains(goals, row, col)) {
				if (setContains(walls, row-1, col)&&setContains(walls, row, col-1))
					return true; 
				if (setContains(walls, row-1, col)&&setContains(walls, row, col+1))
					return true; 
				if (setContains(walls, row+1, col)&&setContains(walls, row, col-1))
					return true; 
				if (setContains(walls, row+1, col)&&setContains(walls, row, col+1))
					return true; 

				if (setContains(walls, row-1, col-1)&&setContains(walls, row-1, col)&&
						setContains(walls, row-1, col+1)&&setContains(walls, row, col-2)&&
						setContains(walls, row, col+2)&&(!setContains(goals, row, col-1))&&
								!setContains(goals, row, col+1))
					return true;
				if (setContains(walls, row+1, col-1)&&setContains(walls, row+1, col)&&
						setContains(walls, row+1, col+1)&&setContains(walls, row, col-2)&&
						setContains(walls, row, col+2)&&(!setContains(goals, row, col-1))&&
								(!setContains(goals, row, col+1)))
					return true;
				if (setContains(walls, row-1, col-1)&&setContains(walls, row, col-1)&&
						setContains(walls, row+1, col-1)&&setContains(walls, row-2, col)&&
						setContains(walls, row+2, col)&&(!setContains(goals, row-1, col))&&
								(!setContains(goals, row+1, col)))
					return true; 
				if (setContains(walls, row-1, col+1)&&setContains(walls, row, col+1)&&
						setContains(walls, row+1, col+1)&&setContains(walls, row-2, col)&&
						setContains(walls, row+2, col)&&(!setContains(goals, row-1, col))&&
								(!setContains(goals, row+1, col)))
					return true; 
			}
		}
		return false;
	}


	public ArrayList<String> actions(State state) {
		ArrayList<String> actionList = new ArrayList<String>();
		int row = state.player.row;
		int col = state.player.col;
		HashSet<Coordinate> diamonds = state.diamonds;
		
		Coordinate newPlayer = new Coordinate(row-1,col);
		Coordinate newDiamond = new Coordinate(row-2, col);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newDiamond)||walls.contains(newDiamond)))
				;
			else
				actionList.add("u");
		newPlayer = new Coordinate(row,col+1);
		newDiamond = new Coordinate(row, col+2);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newDiamond)||walls.contains(newDiamond)))
				;
			else
				actionList.add("r");
		newPlayer = new Coordinate(row+1,col);
		newDiamond = new Coordinate(row+2, col);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newDiamond)||walls.contains(newDiamond)))
				;
			else
				actionList.add("d");
		newPlayer = new Coordinate(row,col-1);
		newDiamond = new Coordinate(row, col-2);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newDiamond)||walls.contains(newDiamond)))
				;
			else
				actionList.add("l");
		return actionList;
	}

	private boolean setContains(HashSet<Coordinate> set, int row, int col) {
		if (set.contains(new Coordinate(row, col)))
			return true;
		return false;
	}
	
}

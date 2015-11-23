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
		for(Coordinate box : state.diamonds)
			if (!goals.contains(box))
				return false;
		return true;
	}
	

	public boolean deadlockTest(State state) {
		for (Coordinate box : state.diamonds) {
			int row = box.row;
			int col = box.col;
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
		Coordinate newBox = new Coordinate(row-2, col);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newBox)||walls.contains(newBox)))
				;
			else
				actionList.add("u");
		newPlayer = new Coordinate(row,col+1);
		newBox = new Coordinate(row, col+2);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newBox)||walls.contains(newBox)))
				;
			else
				actionList.add("r");
		newPlayer = new Coordinate(row+1,col);
		newBox = new Coordinate(row+2, col);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newBox)||walls.contains(newBox)))
				;
			else
				actionList.add("d");
		newPlayer = new Coordinate(row,col-1);
		newBox = new Coordinate(row, col-2);
		if (!walls.contains(newPlayer))
			if (diamonds.contains(newPlayer)&&(diamonds.contains(newBox)||walls.contains(newBox)))
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

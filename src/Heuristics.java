import java.util.HashSet;

/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class Heuristics {
	
	private HashSet<Coordinate> goals;
	double[][] cost;
	char hChoice;
	
	public Heuristics(HashSet<Coordinate> goals, char hChoice) {
		this.goals = goals;
		this.hChoice = hChoice;
		this.cost = new double[goals.size()][goals.size()];
	}

	private int manhattan(Coordinate c1, Coordinate c2) {
		return Math.abs(c1.row-c2.row) + Math.abs(c1.col-c2.col);
	}

	private double euclidean(Coordinate c1, Coordinate c2) {
		return Math.sqrt((double)((c1.row-c2.row)*(c1.row-c2.row)+(c1.col-c2.col)*(c1.col-c2.col)));
	}

	public double calculate(State s, String method) {
		HashSet<Coordinate> diamonds = s.diamonds;
		double sum = 0;
		
		Coordinate player = s.player;
		double playerMin = getDist(player, diamonds, method);
		sum+= playerMin;
		
		for (Coordinate b : diamonds) {
			double boxMin = getDist(b, goals, method);
			sum += boxMin;
		}
		
		return sum;
	}

	private double getDist(Coordinate obj, HashSet<Coordinate> sets, String method) {
		double minDist = 1000000;
		
		//For each coordinate in a set, calculate the distance according to given heuristic choice
		for (Coordinate c : sets) {
			double dist;
			if (method.equals("m"))
				dist = manhattan(obj, c);
			else
				dist = euclidean(obj, c);
			if (dist < minDist)
				minDist = dist;
		}
		
		return minDist;
	}
	
	public double getHeuristic(State state) {
		
		if (hChoice == 'm')
			return calculate(state, "m");
		if (hChoice == 'e')
			return calculate(state, "e");
		
		int i=0;
		for (Coordinate box : state.diamonds) {
			int j=0;
			double playerCost = manhattan(state.player, box);
			for (Coordinate goal : goals) {
				cost[i][j] = manhattan(box, goal);
				cost[i][j] += playerCost;
				j++;
			}
			i++;
		}
		
		double max = 0;
		if (hChoice == 'h')
			return max;
		
		return Math.max(Math.max(calculate(state, "m"), calculate(state, "e")), max);
	}

}
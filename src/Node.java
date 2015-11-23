
/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class Node {

	public Node parent;
	public State state;
	public int cost;
	public String move;
	
	public Node(State state, Node parent, int cost, String move) {
		this.state = state;
		this.parent = parent;
		this.cost = cost;
		this.move = move;
	}
	
	@Override
	public boolean equals(Object n) {
		return (this.state == ((Node) n).state);
	}

}

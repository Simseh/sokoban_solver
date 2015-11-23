import java.util.HashSet;

/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class State {

	HashSet<Coordinate> diamonds;
	Coordinate player;
	
	public State(HashSet<Coordinate> diamonds, Coordinate player) {
		this.diamonds = diamonds;
		this.player = player;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		for (Coordinate b : diamonds) {
			result = 37 * result + b.hashCode();
		}
		result = 37 * result + player.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object object){
		
	    if (object == null) return false;
	    if (object == this) return true;
	    if (this.getClass() != object.getClass()) return false;
	    State s = (State)object;
	    if(this.hashCode()== s.hashCode()) return true;
	    if((this.diamonds == s.diamonds) && (this.player == s.player)) return true;
	    return false;
	}
	
}
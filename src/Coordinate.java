/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class Coordinate {
	
	public int row;
	public int col;
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public int hashCode() {
		return row*1000 + col;
	}
	
	@Override
	public boolean equals(Object object){
		if (object == null) return false;
	    if (object == this) return true;
	    if (this.getClass() != object.getClass()) return false;
		Coordinate c = (Coordinate) object;
		if(this.hashCode()== c.hashCode()) return true;
	    return ((this.row == c.row) && (this.col == c.col));
	}
	
}

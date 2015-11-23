import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class represents a "panel" that displays a grid of colored squares.
 * The class also include a main() routine that creates a window containing
 * a panel of this type.
 */
public class Grid extends JPanel implements MouseListener {

	
	public static void main(String[] args) {
		JFrame window; 
		window = new JFrame("Grid");
		Grid content = new Grid(10,10,30);
		window.setContentPane( content );
		window.pack(); // Set the size of the window based on the panel's preferred size.
		Dimension screenSize; // A simple object containing the screen's width and height.
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int top, left;  // position for top left corner of the window
		left = ( screenSize.width - window.getWidth() ) / 2;
		top = ( screenSize.height - window.getHeight() ) / 2;
		window.setLocation(left,top);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	public static JPanel createGrid(int rows, int cols, int squareSize){
		JPanel content = new Grid(rows, cols, squareSize);		
		return content;
	}

	//--------------- The remainder of this class definition is the "non-static" part. ------------
	private ColorEnum colorEnum;
	private int gridRows; // Number of rows of squares.
	private int gridCols; // Number of columns of squares.
	private Color[][] gridColor; /* gridColor[r][c] is the color for square in row r, column c; 
	                                 if it  is null, the square has the panel's background color.*/
	private Color lineColor; // Color for lines drawn between squares; if null, no lines are drawn.

	
	/**
	 * This constructor creates a panel with a specified number of rows and columns
	 * of squares.  It sets the preferred size of the panel depending on the
	 * preferred square size.  It also wires up mouse event handling.
	 * @param rows  The number of rows of squares in the panel.
	 * @param columns  The number of columns of squares in the panel.
	 * @param preferredSquareSize  The desired size, in pixels, for the squares.  This will
	 *     be used to compute the preferred size of the panel.  Depending on the context
	 *     in which it is used, the actual size of the panel might be different.  The
	 *     main() routine in this class will respect the preferred size.  (Note that the
	 *     "squares" might become rectangles if the preferred size is not respected.)
	 */
	public Grid(int rows, int columns, int preferredSquareSize) {
		gridColor = new Color[rows][columns]; // Create the array that stores square colors.
		gridRows = rows;
		gridCols = columns;
		lineColor = Color.BLACK;
		setPreferredSize( new Dimension(preferredSquareSize*columns, 
				preferredSquareSize*rows) );
		setBackground(Color.WHITE); // Set the background color for this panel.
		addMouseListener(this);     // Mouse actions will call methods in this object.
	}
	
	
	/**
	 * Converts the y-coordinate of a pixel into a row number for the grid of squares.
	 * @param pixelY a pixel y-coordinate. The number of pixels from the top of the panel.
	 * @return The row number corresponding to pixelY. Which row of squares contains that pixel?
	 */
	private int findRow(int pixelY) {
		return (int)(((double)pixelY)/getHeight()*gridRows);
	}
	

	/**
	 * Converts the x-coordinate of a pixel into a column number for the grid of squares.
	 * @param pixelX a pixel x-coordinate. The number of pixels from the left edge of the panel.
	 * @return The column number corresponding to pixelY. Which col of squares contains that pixel?
	 */
	private int findColumn(int pixelX) {
		return (int)(((double)pixelX)/getWidth()*gridCols);
	}
	
	public void mousePressed(MouseEvent evt) {
		int row, col; // the row and column in the grid of squares where the user clicked.
		row = findRow( evt.getY() );
		col = findColumn( evt.getX() );
		gridColor[row][col] = new Color( (int)(225*Math.random()),
				(int)(225*Math.random()),(int)(225*Math.random()) );
		repaint(); // Causes the panel to be redrawn, by calling the paintComponent method.
	}
	public void colorGridCoordinate(int row, int col, ColorEnum color){
		gridColor[row][col] = new Color(color.r, color.g, color.b);
		repaint();
	}
	

	/**
	 * Draws the grid of squares.  Also draws lines separating the squares if lineColor
	 * is not null.  The technique used here will exactly fill the panel with colored
	 * rectangles, whether the panel has its preferred size or not.
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0,0,getWidth(),getHeight());
		int row, col;
		double cellWidth = (double)getWidth() / gridCols;
		double cellHeight = (double)getHeight() / gridRows;
		for (row = 0; row < gridRows; row++) {
			for (col = 0; col < gridCols; col++) {
				if (gridColor[row][col] != null) {
					int x1 = (int)(col*cellWidth);
					int y1 = (int)(row*cellHeight);
					int x2 = (int)((col+1)*cellWidth);
					int y2 = (int)((row+1)*cellHeight);
					g.setColor(gridColor[row][col]);
					g.fillRect( x1, y1, (x2-x1), (y2-y1) );
				}
			}
		}
		if (lineColor != null) {
			g.setColor(lineColor);
			for (row = 1; row < gridRows; row++) {
				int y = (int)(row*cellHeight);
				g.drawLine(0,y,getWidth(),y);
			}
			for (col = 1; col < gridRows; col++) {
				int x = (int)(col*cellWidth);
				g.drawLine(x,0,x,getHeight());
			}
		}
	}
	

	// The following are unneeded mouse event methods.  The definitions 
	// must be here even though they are not used, to satisfy the
	// "MouseListener" interface.
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) {	}
	public void mouseExited(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) { }

	
} // end class Grid
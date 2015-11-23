import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import static javax.swing.ScrollPaneConstants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private SokobanSolver solver;
	private JButton submit, next, prev;
	private JTextArea answerText;
	private JLabel loadingLabel, stepLabel;
	private JComboBox searchMenu, heuristicsMenu; 
	private char hChoice = ' ';
	private JPanel questionPanel;
	private JPanel mainPanel = (JPanel) getContentPane();
	private JPanel gridPanel = new JPanel();
	private Grid grid;
	private boolean gridExist = false;
	
	private JLabel questionLabel1 = new JLabel();
	private JLabel questionLabel2 = new JLabel();
	private JTextField questionField1 = new JTextField();
	
	private int numRow, numCol, currentStep;
	private String questionSelected = " ";
	private String solution = "";
	private String[] steps;
	
	
	
	private HashSet<Coordinate> walls;
	private HashSet<Coordinate> goals;
	private HashSet<Coordinate> diamonds;
	private Coordinate player;
	private String[] puzzleStates; 

	private String[] choices = {"Breadth-First", "Depth-First"}; 
	
	private String[] hChoices = {"Manhattan", "Euclidean", 
			"Hungarian", "Max{h1, h2, h3}"};
	
	
	
	/**
	 * @throws IOException
	 */
	public MainFrame() throws IOException {
		init();
		updateQuestion("default");
		setSize(700, 800);
		setTitle("Sokoban Solver");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		solver = new SokobanSolver();
	}

	/**
	 * @throws IOException
	 */
	private void init() throws IOException {
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getTopPanel(), BorderLayout.NORTH);		
		mainPanel.add(getAnswerPanel(), BorderLayout.WEST);		
		mainPanel.add(getLoadingPanel(), BorderLayout.SOUTH);	
		
		addListeners();
	}
	

	/**
	 * @return loadingPanel
	 */
	private JPanel getLoadingPanel() {
		JPanel loadingPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		loadingPanel.setLayout(layout);
		loadingPanel.setPreferredSize(new Dimension(700,50));
		
		loadingLabel = new JLabel();
		loadingLabel.setText(" ");
		
		stepLabel = new JLabel();
		stepLabel.setText("Show steps:");
		stepLabel.setVisible(false);
		
		prev = new JButton("Previous");
		prev.setVisible(false);
		next = new JButton("Next");
		next.setVisible(false);
		
		loadingPanel.add(loadingLabel);
		loadingPanel.add(stepLabel);
		loadingPanel.add(prev);
		loadingPanel.add(next);
		
		layout.putConstraint(SpringLayout.WEST, loadingLabel,
                10, SpringLayout.WEST, loadingPanel);
		layout.putConstraint(SpringLayout.NORTH, loadingLabel,
                15, SpringLayout.NORTH, loadingPanel);
		layout.putConstraint(SpringLayout.EAST, next, -10, 
				SpringLayout.EAST, loadingPanel);
		layout.putConstraint(SpringLayout.NORTH, next, 10, 
				SpringLayout.NORTH, loadingPanel);
		layout.putConstraint(SpringLayout.EAST, prev, 10, 
				SpringLayout.WEST, next);
		layout.putConstraint(SpringLayout.NORTH, prev, 10, 
				SpringLayout.NORTH, loadingPanel);
		layout.putConstraint(SpringLayout.EAST, stepLabel, 0, 
				SpringLayout.WEST, prev);
		layout.putConstraint(SpringLayout.NORTH, stepLabel, 15, 
				SpringLayout.NORTH, loadingPanel);
		
		return loadingPanel;
	}
	
	private JPanel getGrid(){
		grid = new Grid(numRow, numCol, 30);
		gridPanel = grid; 
		gridPanel.setLayout(new BorderLayout());
		gridPanel.setSize(new Dimension(300,300));
		gridPanel.setBorder(BorderFactory.createTitledBorder("Grid"));
		System.out.println(gridPanel);
		return gridPanel;
	}	
	
	
	private JPanel getAnswerPanel() {
		JPanel answerPanel = new JPanel();
		answerPanel.setLayout(new BorderLayout());
		answerText = new JTextArea();
		answerText.setText("");
		answerText.setSize(new Dimension(350, 100));
		answerText.setEditable(false); 
		answerText.setLineWrap(true); 
		Font font = new Font("Monaco", Font.PLAIN, 12);
        answerText.setFont(font);               
		
		answerPanel.add(answerText);
		answerPanel.setSize(new Dimension(700,250));
		//answerPanel.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		answerPanel.setBorder(BorderFactory.createTitledBorder("Answer"));
		return answerPanel;
	}

	private JPanel getTopPanel() throws IOException {
		JPanel topPanel = new JPanel();
		topPanel.setSize(new Dimension(700, 100));
		topPanel.setLayout(new GridLayout(2, 1));
		
		Image logo = ImageIO.read(new File("img/sokoban_logo.png"));
		Image resizedLogo = logo.getScaledInstance(700, 50, Image.SCALE_SMOOTH); 
		JLabel picLabel = new JLabel(new ImageIcon(resizedLogo));
		picLabel.setSize(700,50);
		topPanel.add(picLabel);
		
		questionPanel = new JPanel();
		questionPanel.setLayout(new BorderLayout());
		questionPanel.setSize(700, 50);
		
		searchMenu = new JComboBox(choices);
		searchMenu.setSize(80, 40);
		searchMenu.setEditable(false);
		
		heuristicsMenu = new JComboBox(hChoices);
		heuristicsMenu.setSize(80, 40);
		heuristicsMenu.setEditable(false);
		heuristicsMenu.setVisible(false);
		
		submit = new JButton("Solve");
		
		SpringLayout layout = new SpringLayout();
		JPanel labelPanel = new JPanel(layout);
		labelPanel.add(questionLabel1);
		labelPanel.add(questionField1);
		labelPanel.add(questionLabel2);
		labelPanel.add(heuristicsMenu);
		setLayoutBounds(layout, labelPanel); 
		
		questionPanel.add(searchMenu, BorderLayout.WEST);	
		questionPanel.add(labelPanel, BorderLayout.CENTER);
		questionPanel.add(submit, BorderLayout.EAST);
		topPanel.add(questionPanel);
		return topPanel;
	}
	
	private void addListeners() {
		searchMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox comboBox = (JComboBox) event.getSource();
				questionSelected = comboBox.getSelectedItem().toString();
				updateQuestion(questionSelected.toLowerCase());
			}
		});

		heuristicsMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox comboBox = (JComboBox) event.getSource();
				String selected = comboBox.getSelectedItem().toString();
				if (selected.contains("Max")) {
					hChoice = 'x';
				}
				else
					hChoice = selected.charAt(0);
			}
		});
		
		submit.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
				if (submit.isEnabled()) {
					stepLabel.setVisible(false);
					prev.setVisible(false);
					next.setVisible(false);
					displaySolvingMessage(questionSelected);
				}
			}
		});
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

				try {
					int numPlayer = solver.loadFile(questionField1.getText(), hChoice);
					if (numPlayer != 1) {
						displayMessage("There can only be one player");
					}
					else {
						numRow = solver.getRow();
						numCol = solver.getCol();
						goals = solver.getGoals();
						walls = solver.getWalls();
						diamonds = solver.getDiamonds();
						player = solver.getPlayer();
						currentStep = 0;
						char method = Character.toLowerCase(questionSelected.charAt(0));
						String answer = solver.solve(method);
						System.out.println(answer);
						String[] lines = answer.split("\\r?\\n");
						solution = lines[1];
						steps = solution.split(" ");
						puzzleStates = new String[steps.length+1];
						String totalSteps = lines[2];
						answerText.setText("Solution: " + solution + " " + totalSteps);
						String runtime = answer.substring(answer.indexOf("units")+7);
						String message = questionSelected + " search. Total runtime : " + runtime;
						if(gridExist)
							mainPanel.remove(grid);
						mainPanel.add(getGrid(), BorderLayout.EAST);
						gridExist = true;
						if (answer.contains("Failed")) {
							displayMessage("No solution found using " + message);
							repaint();
						}
						else {
							displayMessage("Solution found using " + message);
							stepLabel.setVisible(true);
							prev.setVisible(true);
							next.setVisible(true);
							updatePuzzle();
						}
					}
				} catch (NumberFormatException e) {
					displayMessage("File not correctly formatted. First line needs to " +
							"contain total number of rows in the puzzle");
				} catch (FileNotFoundException e) {
					displayMessage("File: \"" + questionField1.getText() + "\" not found!");
				} catch (NoSuchElementException e) {
					displayMessage("File not correctly formatted. First line needs to " +
							"contain total number of rows in the puzzle");
				}
			}
		});	
		
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentStep>0) {
					currentStep -= 1;
					updatePuzzle();
				}
			}
		});
		
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentStep<steps.length) {
					if (puzzleStates[currentStep+1]==null) {
						int row = player.row;
						int col = player.col;
						if (steps[currentStep].equals("u")) {
							Coordinate checkDiamond = new Coordinate(row-1, col);
							if (diamonds.contains(checkDiamond)) {
								diamonds.remove(checkDiamond);
								diamonds.add(new Coordinate(row-2, col));
							}
							player = new Coordinate(row-1, col);
						}
						else if (steps[currentStep].equals("d")) {
							Coordinate checkDiamond = new Coordinate(row+1, col);
							if (diamonds.contains(checkDiamond)) {
								diamonds.remove(checkDiamond);
								diamonds.add(new Coordinate(row+2, col));
							}
							player = new Coordinate(row+1, col);
						}
						else if (steps[currentStep].equals("l")) {
							Coordinate checkDiamond = new Coordinate(row, col-1);
							if (diamonds.contains(checkDiamond)) {
								diamonds.remove(checkDiamond);
								diamonds.add(new Coordinate(row, col-2));
							}
							player = new Coordinate(row, col-1);
						}
						else if (steps[currentStep].equals("r")) {
							Coordinate checkDiamond = new Coordinate(row, col+1);
							if (diamonds.contains(checkDiamond)) {
								diamonds.remove(checkDiamond);
								diamonds.add(new Coordinate(row, col+2));
							}
							player = new Coordinate(row, col+1);
						}
					}
					currentStep += 1;
					updatePuzzle();
				}
			}
		});
	
	}
	

	private void updatePuzzle() {
		int totalSteps = steps.length;
		String output = "Solution: " + solution + "(total of " + totalSteps + " steps)\n\n";
		output += "Showing step " + currentStep + ":\n";
		if (puzzleStates[currentStep] == null) {
			String position = "";
			for (int i=0; i<numRow; i++) {
				for (int j=0; j<numCol; j++) {
					Coordinate c = new Coordinate(i, j);
					if (player.equals(c)){
						grid.colorGridCoordinate(i, j, ColorEnum.RED);
						position += "@";
					}else if (diamonds.contains(c)){
						grid.colorGridCoordinate(i, j, ColorEnum.BLUE);
						position += "$";
					}else if (goals.contains(c)){
						grid.colorGridCoordinate(i, j, ColorEnum.GREEN);
						position += ".";
					}else if (walls.contains(c)){
						grid.colorGridCoordinate(i, j, ColorEnum.GREY);
						position += "#";
					}else{
						grid.colorGridCoordinate(i, j, ColorEnum.YELLOW);
						position += " ";
					}
				}
				position += "\n";
			}
			output += position;
			puzzleStates[currentStep] = position;
		}
		else
			output += puzzleStates[currentStep];
		answerText.setText(output);
		repaint();
	}
	
	private void displaySolvingMessage(String message) {
		loadingLabel.setText("Solving the puzzle using " + message + " search...");
		repaint();
	}

	private void displayMessage(String message) {
		loadingLabel.setText(message);
		repaint();
	}
	

	private void setLayoutBounds(SpringLayout layout, JPanel labelPanel) {
		layout.putConstraint(SpringLayout.WEST, questionLabel1,
                5, SpringLayout.WEST, labelPanel);
		layout.putConstraint(SpringLayout.NORTH, questionLabel1,
                17, SpringLayout.NORTH, labelPanel);
		layout.putConstraint(SpringLayout.WEST, questionField1,
                1, SpringLayout.EAST, questionLabel1);
		layout.putConstraint(SpringLayout.NORTH, questionField1,
                15, SpringLayout.NORTH, labelPanel);
		layout.putConstraint(SpringLayout.WEST, questionLabel2,
                1, SpringLayout.EAST, questionField1);
		layout.putConstraint(SpringLayout.NORTH, questionLabel2,
                17, SpringLayout.NORTH, labelPanel);
		layout.putConstraint(SpringLayout.WEST, heuristicsMenu,
                1, SpringLayout.EAST, questionLabel2);
		layout.putConstraint(SpringLayout.NORTH, heuristicsMenu,
                12, SpringLayout.NORTH, labelPanel);
	}

	private void updateQuestion(String selected) {
		questionLabel1.setText("Enter the filepath: ");
		questionField1.setText("");
		questionField1.setPreferredSize(new Dimension(100, 20));
		questionField1.setVisible(true);
		submit.setEnabled(true);
		if (selected.equals("breadth-first")||selected.equals("depth-first")) {
			heuristicsMenu.setVisible(false);
		}
		else {
			questionLabel1.setText("Select a search method from the left.");
			questionField1.setVisible(false);
			submit.setEnabled(false);
		}
		repaint();
	}
}
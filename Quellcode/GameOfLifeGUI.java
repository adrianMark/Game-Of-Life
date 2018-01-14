import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class is the visualization of the game board. It extends the JFrame
 * class and is adapted with several GUI elements.
 * 
 * @author Adrian Mark
 */
public class GameOfLifeGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	// GUI elements that need to be accessible outside of the constructor
	private JPanel contentPane;
	private JPanel panelBoard = new JPanel();
	private JButton btnReset;
	private JButton btnFlow;
	private JButton btnStop;
	private JLabel lblGeneration;
	private JLabel lblGeneration2;

	// Class needs an instance of itself to hand it over to a FlowThread which
	// needs to access the GUI elements
	private GameOfLifeGUI self = this;

	/**
	 * GridLayout that ensures that the Cells(JLabels) are arranged in a grid
	 * Initialized with a 10x10 fields grid.
	 */
	private GridLayout gl_panelBoard = new GridLayout(10, 11);
	/**
	 * This instance of the {@link Board} class performs the actual computation
	 * of the generations. It is initialized with the dimensions of 10x10 Fields
	 */
	private Board board = new Board(10, 10);
	/**
	 * This integer value indicates at what pace the {@link FlowThread} is
	 * supposed to compute the generations
	 */
	private int flowSpeed = 5;

	// Constructor
	public GameOfLifeGUI() {

		// Build frame
		setIconImage(Toolkit.getDefaultToolkit().getImage(GameOfLifeGUI.class.getResource("icon.png"))); // load the image to be displayed
		setTitle("Game of Life - Code Competition September 2017");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null); //Ensure that application is located in the middle of the screen

		// Build container for the game board
		panelBoard.setBorder(new LineBorder(new Color(0, 0, 0))); 
		panelBoard.setBackground(Color.WHITE);
		panelBoard.setBounds(10, 10, 640, 640);
		panelBoard.setLayout(gl_panelBoard);

		// Btn to manually compute the next generation
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(674, 254, 89, 23);
		contentPane.add(btnNext);

		// Btn to reset the board
		btnReset = new JButton("Reset");
		btnReset.setBounds(674, 345, 89, 23);
		contentPane.add(btnReset);

		// Btn to start computing generations perpetually
		btnFlow = new JButton("Flow >>");
		btnFlow.setBounds(674, 88, 89, 23);
		contentPane.add(btnFlow);

		// Btn to stop computing generations perpetually
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.setBounds(674, 122, 89, 23);
		contentPane.add(btnStop);

		// Add slider to set the pace of computation
		JSlider sliderSpeed = new JSlider();
		sliderSpeed.setMinimum(1);
		sliderSpeed.setValue(5);
		sliderSpeed.setPaintTicks(true);
		sliderSpeed.setMajorTickSpacing(1);
		sliderSpeed.setMaximum(9);
		sliderSpeed.setBounds(660, 164, 124, 26);
		contentPane.add(sliderSpeed);

		// Add labels to show the selected pace; show value of 'sliderSpeed'
		JLabel lblSpeed = new JLabel("Speed: ");
		lblSpeed.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSpeed.setBounds(690, 201, 46, 14);
		contentPane.add(lblSpeed);
		JLabel labelSpeed2 = new JLabel("5");
		labelSpeed2.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelSpeed2.setBounds(740, 201, 27, 14);
		contentPane.add(labelSpeed2);

		// Add labels to show the number of generations that survived
		lblGeneration = new JLabel("Generation: ");
		lblGeneration.setHorizontalAlignment(SwingConstants.CENTER);
		lblGeneration.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblGeneration.setBounds(660, 589, 124, 14);
		contentPane.add(lblGeneration);
		lblGeneration2 = new JLabel("0");
		lblGeneration2.setHorizontalAlignment(SwingConstants.CENTER);
		lblGeneration2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblGeneration2.setBounds(660, 608, 124, 14);
		contentPane.add(lblGeneration2);

		// Add spinner to set the size of the game board
		JSpinner spinnerSizeOfBoard = new JSpinner();
		spinnerSizeOfBoard.setModel(new SpinnerNumberModel(10, 3, 90, 1));
		spinnerSizeOfBoard.setBounds(694, 473, 56, 20);
		contentPane.add(spinnerSizeOfBoard);

		// Label for the spinner
		JLabel lblSize = new JLabel("Side Length");
		lblSize.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSize.setHorizontalAlignment(SwingConstants.CENTER);
		lblSize.setBounds(660, 448, 124, 14);
		contentPane.add(lblSize);
		JCheckBox chckbxShowGrid = new JCheckBox("Show Grid");
		chckbxShowGrid.setFont(new Font("Tahoma", Font.PLAIN, 10));

		// checkbox to select whether a grid is shown or not
		chckbxShowGrid.setBounds(685, 500, 89, 23);
		contentPane.add(chckbxShowGrid);

		// Initialize the board with the size of 10x10 fields
		setSizeOfBoard(10, 10, false);

		// Listeners for the GUI elements

		// If 'Next' is clicked compute the next generation and update the
		// changes on the GUI
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				board.nextGeneration();
				updateVisibleBoard(true);
				lblGeneration2.setText(String.valueOf(board.getGeneration()));
			}
		});

		// If the status of the slider is changed change the pace of the
		// FlowThread accordingly
		sliderSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				labelSpeed2.setText(String.valueOf(sliderSpeed.getValue()));
				flowSpeed = sliderSpeed.getValue();
			}
		});

		// Stop the thread when clicked. Enable 'Flow' Button and
		// and field size option
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStop.setEnabled(false);
				btnFlow.setEnabled(true);
				spinnerSizeOfBoard.setEnabled(true);
			}
		});

		// start the FlowThread, enable the stop button and disable this button
		// as well as the field size option
		btnFlow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				spinnerSizeOfBoard.setEnabled(false);
				btnStop.setEnabled(true);
				btnFlow.setEnabled(false);
				FlowThread thread = new FlowThread(self);
				thread.start();
			}
		});

		// Reset the board, the number of generations and cleanse the GUI
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnStop.isEnabled()) {
					btnStop.doClick();
				}
				for (ArrayList<Cell> row : board.getBoard()) {
					for (Cell c : row) {
						c.setAlive(false);
						c.setGenerationsSurvived(0);
					}
				}
				board.setGeneration(0);
				lblGeneration2.setText("0");
				updateVisibleBoard(false);
			}
		});

		// Change the size of the game board and reset the generation information
		spinnerSizeOfBoard.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				board.setGeneration(0);
				lblGeneration2.setText("0");
				int length = (int) spinnerSizeOfBoard.getValue();
				setSizeOfBoard(length, length, chckbxShowGrid.isSelected());
			}
		});

		// show/hide the grid that surrounds each cell 
		chckbxShowGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// get all the CellPanels from the container
				Component panels[] = panelBoard.getComponents();
				// iterate over the panels and draw a LineBorder
				for (int i = 0; i < panels.length; i++) {
					CellPanel p = (CellPanel) panels[i];
					if (chckbxShowGrid.isSelected()) {
						p.setBorder(new LineBorder(Color.BLACK));
					}else{
						p.setBorder(BorderFactory.createEmptyBorder());
					}
				}
			}
		});

	}

	// #######################################################################
	// Getters and Setters
	public JLabel getLblGeneration2() {
		return lblGeneration2;
	}

	public void setLblGeneration2(JLabel lblGeneration2) {
		this.lblGeneration2 = lblGeneration2;
	}

	public Board getBoard() {
		return board;
	}

	public int getFlowSpeed() {
		return flowSpeed;
	}

	public void setFlowSpeed(int flowSpeed) {
		this.flowSpeed = flowSpeed;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public JButton getBtnFlow() {
		return btnFlow;
	}

	public void setBtnFlow(JButton btnFlow) {
		this.btnFlow = btnFlow;
	}

	public JButton getBtnStop() {
		return btnStop;
	}

	public void setBtnStop(JButton btnStop) {
		this.btnStop = btnStop;
	}

	// ################################################################

	/**
	 * This method changes the color of the cell depending on it's matureness to
	 * a certain shade of blue
	 * 
	 * @param p
	 *            the {@link CellPanel} that is supposed to change it's color
	 */
	public void highLightCell(CellPanel p) {
		Cell c = board.getCell(p.getPositionX(), p.getPositionY());
		if (c.getGenerationsSurvived() == 0) {
			// CadetBlue2
			p.setBackground(new Color(142, 229, 238));
		} else if (c.getGenerationsSurvived() == 1) {
			// DeepSkyBlue
			p.setBackground(new Color(0, 191, 255));
		} else if (c.getGenerationsSurvived() == 2) {
			// DeepSkyBlue3
			p.setBackground(new Color(0, 154, 205));
		} else if (c.getGenerationsSurvived() == 3) {
			// DodgerBlue3
			p.setBackground(new Color(24, 116, 205));
		} else {
			// DodgerBlue4
			p.setBackground(new Color(16, 78, 139));
		}
	}

	/**
	 * This method changes the background color of a selected {@link CellPanel}
	 * to the default white
	 * 
	 * @param p
	 *            - The cell which is supposed to change it's color
	 */
	public void lowLightCell(CellPanel p) {
		p.setBackground(Color.WHITE);
	}

	/**
	 * This method highlights the {@link CellPanel} and revives the {@link Cell}
	 * at the respective position on the {@link Board} in the background
	 * 
	 * @param p
	 *            - The {@link CellPanel} that represents the {@link Cell} that
	 *            is revived
	 */
	public void setCellAlive(CellPanel p) {
		highLightCell(p);
		board.getCell(p.getPositionX(), p.getPositionY()).setAlive(true);
		repaint();
	}

	/**
	 * This method sets the color of the {@link CellPanel} to white and kills
	 * the {@link Cell} at the respective position on the {@link Board} in the
	 * background
	 * 
	 * @param p
	 *            - The {@link CellPanel} that represents the {@link Cell} that
	 *            is killed
	 */
	public void setCellDead(CellPanel p) {
		lowLightCell(p);
		Cell cellInPanel = board.getCell(p.getPositionX(), p.getPositionY());
		cellInPanel.setAlive(false);
		repaint();
	}

	/**
	 * This method applies the changes that were computed in the {@link Board}
	 * class in the background to the visible board on the {@link GameOfLifeGUI}
	 * .
	 * 
	 * @param showExtinctionMessage
	 *            - Indicates whether a message is desired if the number of
	 *            living cells is zero
	 */

	public void updateVisibleBoard(boolean showExtinctionMessage) {
		// get all the CellPanels from the container
		Component panels[] = panelBoard.getComponents();
		int livingCells = 0;

		/*
		 * iterate over the panels and change the coloring according to the
		 * status of the cell at that position
		 */
		for (int i = 0; i < panels.length; i++) {
			CellPanel p = (CellPanel) panels[i];
			if (board.getCell(p.getPositionX(), p.getPositionY()).isAlive()) {
				highLightCell(p);
				// count the living cells
				livingCells++;
			} else {
				lowLightCell(p);
			}

		}
		// If all the cells are dead display a message (if desired) and stop the
		// FlowThread if active.
		if (livingCells == 0) {
			if (btnStop.isEnabled()) {
				btnStop.doClick();
				// avoid displaying bug
				repaint();
			}
			if (showExtinctionMessage) {
				JOptionPane.showMessageDialog(this,
						"The population extincted after " + lblGeneration2.getText() + " generations.");
			}
			board.setGeneration(0);
			lblGeneration2.setText("0");
		}
		repaint();
	}

	/**
	 * This method changes the dimensions of both, the visible board on the GUI
	 * and the {@link Board} in the background.
	 * 
	 * @param sizeX
	 *            - new width of the board
	 * @param sizeY
	 *            - new height of the board
	 * @param wishesGrid
	 *            - if true, show a box around each cell
	 */
	public void setSizeOfBoard(int sizeX, int sizeY, boolean wishesGrid) {
		// change the dimensions of the GridLayout
		gl_panelBoard.setColumns(sizeX + 1);
		gl_panelBoard.setRows(sizeY);

		// cleanse the board
		panelBoard.removeAll();

		// rebuilt the board according to the new size
		for (int x = 0; x < sizeX * sizeY; x++) {
			int row = x / (gl_panelBoard.getColumns() - 1);
			int column = x % (gl_panelBoard.getRows());
			CellPanel p = new CellPanel(column, row);
			p.setBackground(Color.WHITE);
			if (wishesGrid) {
				p.setBorder(new LineBorder(Color.BLACK));
			}
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					CellPanel pan = (CellPanel) e.getSource();
					if (!pan.getBackground().equals(Color.WHITE)) {
						setCellDead(pan);
					} else {
						setCellAlive(p);
					}
				}
			});
			panelBoard.add(p);
		}
		contentPane.add(panelBoard);
		// change the dimensions of the board in the background
		board.setDimensions(sizeX, sizeY);
		this.repaint();
		this.validate();
	}

	// toString
	public String toString() {
		return "This class represents the visible board of the game. The size is " + board.getSizeX() + " x "
				+ board.getSizeY() + " fields";
	}
}

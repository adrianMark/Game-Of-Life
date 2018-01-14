/** This class extends the {@link Thread} class. 
 * It is started when the 'FlowButton' on the {@link GameOfLifeGUI} is clicked.
 * The thread computes the generations of cells on the {@link Board} and hence requires 
 * and object of the {@link GameOfLifeGUI} as a constructor parameter
 * @author Adrian Mark
 * @see GameOfLifeGUI*/
public class FlowThread extends Thread {
	
	/** Required to manipulate the visible board*/
	private GameOfLifeGUI gui;

	//Constructor
	public FlowThread(GameOfLifeGUI gui) {
		this.gui = gui; 
	}

	/** Override the run method in order to perpetually update the {@link Board} and the 
	 * {@link GameOfLifeGUI} */
	@Override
	public void run() {
		while (gui.getBtnStop().isEnabled()) { //If not enabled --> Stop button was clicked!
			gui.getBoard().nextGeneration(); //compute next generation
			gui.updateVisibleBoard(true); //update the game board
			gui.getLblGeneration2().setText(String.valueOf(gui.getBoard().getGeneration())); //update the number of generations on the GUI
			try {
				Thread.sleep(5000 / gui.getFlowSpeed()); //Time of interruption depends on the speed that was selected with the speed slider
			} catch (InterruptedException e) {
				System.out.println("An Error occured: " + e.getMessage());
			}
		}
	}
	
	//toString
	public String toString(){
		return "Subclass of Thread. Perpetually updates the board and the GUI ";
	}

}

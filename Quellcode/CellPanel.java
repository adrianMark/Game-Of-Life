import javax.swing.JPanel;
/** This class is deduced from the {@link JPanel} class. It additionally holds to integer values
 * that reflect the positioning on the {@link GameOfLifeGUI}.
 * @author Adrian Mark
 * */
public class CellPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/** Integer that saves the position on the x-axis of the game board*/
	private int positionX;
	/** Integer that saves the position on the y-axis of the game board*/
	private int positionY;
	
	//Constructor
	public CellPanel(int positionX, int positionY){
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	//############################
	// Getters and Setters
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	//######################################
	
	//toString
	public String toString(){
		return "Instance of a CellPanel; Position ("+positionX+","+positionY+")";
	}
	
}

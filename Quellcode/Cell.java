import java.util.ArrayList;

/**
This class represents the cell. It contains booleans for the present status of the cell (alive or dead) and for the status in the next generation. 
The latter is based on the outcome of the 'predictStatus' method and will updated to become the present status ('isAlive') after each round of the game.
Additionally, the cell contains integer values (positionX, positionY) to locate it on the game board.
@author Adrian Mark
@see Board
*/
public class Cell {

	/**The boolean that states whether the cell is presently alive or not*/
	private boolean isAlive = false;
	/**Based on the neighbors of the cell this boolean states whether the cell will be alive next round*/
	private boolean isAliveNextRound;

	/** Integer that saves the position on the x-axis of the game board*/
	private int positionX;
	
	/** Integer that saves the position on the y-axis of the game board*/
	private int positionY;
	
	/** Integer value that reflects the number of consecutive generations that this Cell has survived.
	 * It is used to compute the color of the respective {@link CellPanel} on the {@link GameOfLifeGUI}*/
	private int generationsSurvived = 0;
	
	//Constructor of a cell
	public Cell(int positionX, int positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}

	//########################################	
	//Getters and Setters
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

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

	public boolean isAliveNextRound() {
		return isAliveNextRound;
	}

	public void setAliveNextRound(boolean isAliveNextRound) {
		this.isAliveNextRound = isAliveNextRound;
	}
	
	public int getGenerationsSurvived() {
		return generationsSurvived;
	}

	public void setGenerationsSurvived(int generationsSurvived) {
		this.generationsSurvived = generationsSurvived;
	}
	
	//########################################################
	
	//########################################################
	//Methods
	
	/**This method takes the cells neighbors as input and decides whether the cell will be alive in the next generation
	 * 
	 * @param neighbors - The cells that surround this cell on the game board
	 */
	public void predictStatus(ArrayList<Cell> neighbors) {
		int neighborsAlive = 0; //Status of this cell depends on its neighbors

		//count the living neighbors
		for (Cell c : neighbors) {
			if (c.isAlive) { 
				neighborsAlive++; 
			}
		}
		
		if (this.isAlive == true && (neighborsAlive < 2 || neighborsAlive > 3)) { // living cell with less than two or more than three living neighbors? --> Dead next round due to overpopulation
			this.setAliveNextRound(false);
		} else if (this.isAlive == false && neighborsAlive == 3) { //dead cell with exactly three living neighbors --> Alive next round
			this.setAliveNextRound(true);
		} else if(this.isAlive == true && (neighborsAlive == 2 || neighborsAlive == 3)){ //living cell with two or three living neighbors --> stays alive
			setAliveNextRound(true);
		} else{ //If nothing of the above applies, the cell dies
			setAliveNextRound(false);
		}

	}

	/** This method is called when the next generation starts and the predicted status of the cell needs to be transferred from 'isAliveNextRound' to 'isAlive' */
	public void updateStatus() { //Apply predicted status to the present status. Two steps needed so that calculation of other cells can be done with the old status
			boolean wasAlive = isAlive; //save the old status
			setAlive(isAliveNextRound); //update the status
			if(isAlive && wasAlive){ //if the cell was alive and is still alive, it survived the generation
				generationsSurvived++;
			}else{ //else the counter needs to be reseted
				generationsSurvived = 0;
			}
	}
	
	//#################################################################

	//toString
	public String toString(){
		return "Cell Object: position (+"+positionX+","+positionY+")\npresently alive: "+isAlive+" \nalive next round: "+isAliveNextRound;
	}

}

import java.util.ArrayList;

/** This class represents the game board of the 'Game of Life' it saves two integer values for the dimensions (sizeX and sizeY) 
 * and an ArrayList that contains rows (ArrayList) of {@link Cell} Objects.
 * @author Adrian Mark
 * @see Cell
 */
public class Board {
	
	private int sizeX = 10;
	private int sizeY = 10;
	private int generation = 0;
	private ArrayList<ArrayList<Cell>> board = new ArrayList<>();
	
	//Constructor
	public Board(int sizeX, int sizeY) {
		if (sizeY >= 3) {
			this.sizeY = sizeY;
		}
		if (sizeX >= 3) {
			this.sizeX = sizeX;
		}
		
		setDimensions(sizeX, sizeY);
		
	}

	
	//#######################################################
	//Getters & Setters
	
	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public ArrayList<ArrayList<Cell>> getBoard() {
		return board;
	}

	public void setBoard(ArrayList<ArrayList<Cell>> board) {
		this.board = board;
	}

	//#########################################################

	/** This method returns a {@link Cell} at a given location on the game board
	 * In order to fulfill the requirement that the game continues on the opposite side of the board if one end is reached, the method ensures that 
	 * if the index given with x or y is higher than the maximum index of the ArrayList or if it is negative that the respective Cell on the other side
	 * of the board is returned
	 * @param x - The position on the x - axis
	 * @param y - The position on the y - axis  
	 *  */
	public Cell getCell(int x, int y) {
		if (x < 0) { //Exit board on the left hand side!
			x = this.sizeX - 1;
		}else if (x > this.sizeX - 1) {//Exit board on the right hand side
			x = 0;
		}
		if (y < 0) { //Exit board at the top
			y = this.sizeY - 1;
		}else if (y > this.sizeY-1) { //Exit board at the bottom
			y = 0;
		}
		return board.get(y).get(x); //return the cell at the desired position OR where it enters the board if limit was exceeded
	}
	

	/** This method generates the status of the board and the cells for the next generation. It calls the predictStatus method of each cell on the board. 
	 * Beforehand, the getNeighbors method is evoked for each cell.
	 *  */
	public void nextGeneration() {
		for (ArrayList<Cell> row : board) {
			for (Cell c : row) {
				c.predictStatus(getNeighbours(c)); //Predict whether the cell is alive or not in the next generation based on it's neighbors
			}
		}
		
		//After the next round is computed, update the present status with the status that was predicted
		for (ArrayList<Cell> row : board) {
			for (Cell c : row) {
				c.updateStatus(); 
			}
		}
		generation++; //Increase the value of generations by 1
	}

	/** This method gathers all the neighboring {@link Cell} of the input {@link Cell} (using the getCell method).  
	 * The neighbors are returned as an ArrayList.
	 * @param c - The cell for which the neighbors are supposed to be gathered
	 * */
	public ArrayList<Cell> getNeighbours(Cell c) {
		ArrayList<Cell> neighbours = new ArrayList<>();
		int x = c.getPositionX();
		int y = c.getPositionY();
		
		//declare the eight neighbors
		Cell top;
		Cell bottom;
		Cell left;
		Cell right;
		Cell bottomLeft;
		Cell bottomRight;
		Cell topLeft;
		Cell topRight;

		//Make use of the getCell functionality that automatically returns the cell on the other side of the board if the limit is exceeded
		top = getCell(x, y - 1);
		topRight = getCell(x + 1, y - 1);
		right = getCell(x + 1, y);
		bottomRight = getCell(x + 1, y + 1);
		bottom = getCell(x, y + 1);
		bottomLeft = getCell(x - 1, y + 1);
		left = getCell(x - 1, y);
		topLeft = getCell(x - 1, y - 1);

		neighbours.add(topLeft);
		neighbours.add(top);
		neighbours.add(topRight);
		neighbours.add(right);
		neighbours.add(bottomRight);
		neighbours.add(bottom);
		neighbours.add(bottomLeft);
		neighbours.add(left);
		return neighbours; //return the neighbors
	}
	
	/** This method changed the size of the game board. It changes the dimensions of the ArrayList that contains the rows which contain the actual cells 
	 * @param sizeX - Desired width of the board
	 * @param sizeY - Desired height of the board
	 * */
	public void setDimensions(int sizeX, int sizeY){
		setSizeX(sizeX);
		setSizeY(sizeY);
		board = new ArrayList<>();
		for (int y = 0; y < sizeY; y++) {
			ArrayList<Cell> row = new ArrayList<>();
			for (int x = 0; x < sizeX; x++) {
				row.add(new Cell(x, y));
			}
			this.board.add(row);
		}
	}
	
	//################################################
	// toString
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (ArrayList<Cell> row : board) {
			for (Cell c : row) {
				if (c.isAlive()) {
					sb.append("1");
				} else {
					sb.append("0");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}

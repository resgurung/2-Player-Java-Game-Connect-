package Server;

/**
 * package Server, Class name: GameSession
 */
import java.io.*;
import java.net.*;
import java.awt.*;
/**
 * 
 * @author Resham gurung
 * 
 *  Handling a new thread by implementing Runnable to start the game session
 * 
 *
 */

class GameSession implements Runnable, GameConstants {
/**
 * initialize Socket for player one and two 
 */
	  private Socket player1;
	  private Socket player2;
/**
 * multidimensional array initialization for cell 7*7 and initialize cells
 */
	
	  private Color[][] cell =  new Color[7][7];
/**
 * Initialization of datainputstream and dataoutputstream
 */
	  private DataInputStream fromPlayer1;
	  private DataOutputStream toPlayer1;
	  private DataInputStream fromPlayer2;
	  private DataOutputStream toPlayer2;
/**
 *  Continue to play,boolean only return true or false
 */
	
	  private boolean continueToPlay = true;


/**
 * Constructor for GameSession
 * @param player1, Socket setup, this.socket initialize in this class
 * @param player2  Socket setup, this.socket initialize in this class
 */
	  public GameSession(Socket player1, Socket player2) {

	    this.player1 = player1;
	    this.player2 = player2;

/**
 * Initialize cells with a blank character
 */
	    for (int i = 0; i < 7; i++)
	      for (int j = 0; j < 7; j++)
	       cell[i][j] = null;
	  }

/**
 * This method gets invoke when runnable is implemented by start() method for eg. thread.start();	  
 */

	  public void run() {

	    try {
 
/**
 * Create data input and output streams
 */
		    fromPlayer1 = new DataInputStream(player1.getInputStream());
		    toPlayer1 = new DataOutputStream(player1.getOutputStream());
		    fromPlayer2 = new DataInputStream(player2.getInputStream());
		    toPlayer2 = new DataOutputStream(player2.getOutputStream());

	     
/**
 * Writing to notify client or player to start or Server should always start first
 */
	      toPlayer1.writeInt(CONTINUE);

	      // Continuously serve the players and determine and report
	      // the game status to the players
/**
 * While loops when the condition is true but in this case condition is true
 */
	      while (true) {

	        
/**
 * Receive a move from player 1
 */
	        int row = fromPlayer1.readInt();
	        int column = fromPlayer1.readInt();
/**
 * colour the row with blue 
 */
	        cell[row][column] = Color.blue;

	       
/**
 * if else statement used to check if Player 1 has won
 */
	        if (isWon(Color.blue)) {
	          toPlayer1.writeInt(PLAYER1_WON);
	          toPlayer2.writeInt(PLAYER1_WON);
	          sendMove(toPlayer2, row, column);
	          break; // Break the loop
	        }
	        /**
	         *  Check if all cells are filled
	         */
	        else if (isFull()) { 
	          toPlayer1.writeInt(DRAW);
	          toPlayer2.writeInt(DRAW);
	          sendMove(toPlayer2, row, column);
	          break;
	        }
	        else {

	         
/**
 * Notify player two of their turn whereas the gui will be disable until the turn comes
 */
	          toPlayer2.writeInt(CONTINUE);
/**
 * Send player 1's selected row and column to player 2
 */
	     
	          sendMove(toPlayer2, row, column);
	       }
/**
 *  Receive a move from Player 2
 */
	        
	        row = fromPlayer2.readInt();
	        column = fromPlayer2.readInt();
/**
 *  Colour the cell red
 */
	        cell[row][column] = Color.red;

	/**
	 *  Check if Player 2 wins
	 */
	        if (isWon(Color.red)) {
	          toPlayer1.writeInt(PLAYER2_WON);
	          toPlayer2.writeInt(PLAYER2_WON);
	          sendMove(toPlayer1, row, column);
	          break;
	        }
	        else {
	          // Notify player 1 to take the turn
	          toPlayer1.writeInt(CONTINUE);

	          // Send player 2's selected row and column to player 1
	          sendMove(toPlayer1, row, column);
	        }
	      }
	    }
	    catch(IOException ex) {
	      System.err.println(ex);
	    }
	  }

	  /** Send the move to other player */
	  private void sendMove(DataOutputStream out, int row, int column) throws IOException {

	    out.writeInt(row); // Send row index
	    out.writeInt(column); // Send column index
	  }

	  /** Check if the cells are all occupied if occupied it returns true else false */

	  private boolean isFull() {

	    for (int i = 0; i < 7; i++)
	      for (int j = 0; j < 7; j++)
	        if (cell[i][j] == null)
	          return false; 

	    
	    return true;
	  }

	  /** Determine if the player with the specified token wins */

	  private boolean isWon(Color c) {



		/**
		 * Algorithm to check all the rows if true return true else false
		 */
	        for (int y = 0; y < 7; y++)
	            for (int x = 0; x < 4; x++)
	                if (cell[x][y]==c   &&
	                    cell[x+1][y]==c &&
	                    cell[x+2][y]==c &&
	                    cell[x+3][y]==c)
	                    	
	                    
	                    return true;

	    
	       // return false;
	    


		/**
		 * 	check columns if true return true else false
		 */
	        for (int x = 0; x < 7; x++)
	            for (int y = 0; y < 4; y++)
	                if (cell[x][y]==c  &&
	                    cell[x][y+1]==c &&
	                    cell[x][y+2]==c &&
	                    cell[x][y+3]==c)
	                    return true;
	    


	    /**
	     *   checks if the diagonal cell are full return true else false
	     */
	  

	        for (int x = 0; x < 4; x++)
	            for (int y = 0; y < 4; y++)
	                
	                if (
	               
	                    (cell[x][y]==c     &&
	                     cell[x+1][y+1]==c &&
	                     cell[x+2][y+2]==c &&
	                     cell[x+3][y+3]==c
	                    )
	                ||  
	          
	                    (cell[x+3][y]==c  &&
	                     cell[x+2][y+1]==c &&
	                     cell[x+1][y+2]==c &&
	                     cell[x][y+3]==c
	                    )
	                ) return true; 
	        
	      
	        return false;
	    }

	  
	  
}

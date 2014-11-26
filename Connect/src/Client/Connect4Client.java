package Client;
/**
 * package Client
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.net.*;
/**
 * 
 * @author resham gurung:  
 *  Client class for connect4
 * implements Runnable, actionlistener to lishen button pressed or mouse movement.
 *
 */


public class Connect4Client extends JFrame implements Runnable ,ActionListener,GameConstants {


	 /**
	 * eclipse serial
	 */
	private static final long serialVersionUID = 1L;
/**
 *  Indicate whether the player has the turn which is set to false
 */
	
	  private boolean myTurn = false;
/**
 *   // The token for this first player
 */
		  private Color myColor; 
/**
 * 	 The token for the other player which will join after the first player
 */

	  private Color otherColor;
/**
 *  Multidimension array to create and initialize inner cell
 */
	 
	  private Cell[][] cell =  new Cell[7][7]; 

 
	  /**
	   * Create and initialize a jlabel
	   */
	  private JPanel sidePanel, sidePanel1, infoPanel, mainPanel;
	  private JLabel statusLbl;
	  private JTextArea  nameLabel;
	  private JButton newGame;
	  
	/**
	 *  Selected row and column by the current move
	 */
	  private int rowSelected;
	  private int columnSelected;

	  /**
	   * Input and output streams from/to server
	   */
	  private DataInputStream fromServer;
	  private DataOutputStream toServer;

	/**
	 *  Boolean for Continue to play which is set true
	 */
	  private boolean continueToPlay = true;
/**
 *  Wait for the player to mark a cell
 */
	
	  private boolean waiting = true;

	  // Indicate if it runs as application
	 // private boolean isStandAlone = false;

	 /**
	  *  String Host name or ip name
	  */
	  private String host = "localhost";
	  //username for player
	  String username;
    //iniatilize thread
	 Thread thread;
	  
	  
	/**
	 * constructor for connect4
	 * inherits from superclass the string title
	 * @param title
	 */
	  
	  public Connect4Client(String title) {
		super(title);	
	/**
	 * 	create a input box for player to input string character i.e. name
	 */
		username = JOptionPane.showInputDialog(" Please enter Your name");
        createGUI();
        connectToServer();     
	  }

	  /**
	   * graphicial user interface for client side
	   */
	 
		public void createGUI() {
		
		/**
		 *  Panel to hold cells, buttons 
		 */
		  	mainPanel = new JPanel();
		    infoPanel = new JPanel();
		    sidePanel = new JPanel();
			newGame = new JButton( "NEW GAME!" );
			
			
			statusLbl= new JLabel();
			nameLabel = new JTextArea(50,10);
			
		    mainPanel.setLayout(new GridLayout(7, 7, 0, 0));
/**
 * multidimension array cell[i][j]
 */
		    for (int i = 0; i < 7; i++)
		      for (int j = 0; j < 7; j++)
		       mainPanel.add(cell[i][j] = new Cell(i, j, this)); 

		    // Set properties for labels and borders for labels and panel
		    mainPanel.setBorder(new LineBorder(Color.cyan, 4));
		    sidePanel.setBorder(new LineBorder(Color.cyan, 4));
		    infoPanel.setBorder(new LineBorder(Color.cyan, 4));

		    
		    nameLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
		
		    statusLbl.setFont(new Font("SansSerif", Font.BOLD, 12));

		    
		
		    nameLabel.setWrapStyleWord(true);
		    nameLabel.setEditable(false);
		    nameLabel.setBackground(Color.white);
		    /**
		     *  Place the panel and the labels to the frame
		     *  borderlayout
		     */

		    setLayout(new BorderLayout()); 

		  
		    add(mainPanel, BorderLayout.CENTER);
		    infoPanel.add(statusLbl);
			add(infoPanel, BorderLayout.SOUTH);
/**
 * colour the backround of the panel
 */
			sidePanel.setBackground( new Color(150,100,170) );
			infoPanel.setBackground( new Color(170,100,170));
			mainPanel.setBackground(new Color(150,100,170));
	/**
	 * set side pane to the east of the frame		
	 */
		    add(sidePanel,  BorderLayout.EAST);
		    
		    sidePanel.add(newGame);
		    sidePanel.add(nameLabel);
		    sidePanel.setLayout(new GridLayout(7, 7, 0, 5));
		    nameLabel.setBackground(new Color(150,100,170));
		    newGame.setEnabled(false);
/**
 *  the action performed method that lishen to the click of the button
 */
		    newGame.addActionListener(this); 
		    
/**
 * size of the jframe
 */
		    setSize(600, 600);
		 	setVisible(true);
	      }


/**
 * connection class to server
 */
	
	
	  private void connectToServer() {

	    try {
	  /**
	   * inialization of Socket  
	   */

	      Socket socket;
/**
 *   Methods to throws unknownhost exception and io exception
 */
	      socket = extracted();

	   /**
	    *   Creates an input stream to receive data from the server
	    */
	      fromServer = new DataInputStream(socket.getInputStream());

	 /**
	  *    Creates an output stream to send data to the server
	  */
	      toServer = new DataOutputStream(socket.getOutputStream());
	    }
	    /**
	     * To catch the exception
	     */
	    catch (Exception ex) {
	      System.err.println(ex);
	    } finally {
		}
/**
 *  Thread starts
 */
	  
	    thread = new Thread(this);
	    thread.start();
	  }


/**
 * 
 * @return socket
 * @throws UnknownHostException
 * @throws IOException
 */
	private Socket extracted() throws UnknownHostException, IOException {
		Socket socket;
		socket = new Socket(host, 7000);
		return socket;
	}
	/**
	 *   to overide the method decleration in supertype// run method in runnable 
	 */
	  @Override 
	  public void run() {

	    try {

	/**
	 *   Get notification from the server
	 */

	      int player = fromServer.readInt();

	/**
	 *  decision of a player
	 */

	      if (player == PLAYER1) {

	        setMyColor(Color.blue);
	        setOtherColor(Color.red);
	       
	        setPlayerNameMessage("Player( " + username + " ): Blue " );
	        
	        setStatusMessage("Waiting for player 2 to join");

	/**
	 *   Receive startup notification from the server
	 */

	        fromServer.readInt(); 
/**
 * CONTINUE message from the server to start or continue
 */
	 /**
	  *   The other player has joined
	  */
	        setStatusMessage( "Player 2 has joined. I start first");

/**
 * 	It is my turn
 */
	        setMyTurn(true);
	      }
/**
 * if user is player two then set the colour to red and set player one colour to blue
 */
	      else if (player == PLAYER2) {

	        setMyColor(Color.red);
	        setOtherColor(Color.blue);
	  /**
	   * tell player their username and colour   
	   */
	        setPlayerNameMessage("Player( "  + username+ " ): RED " );
	        setStatusMessage("Waiting for player 1 to move");
	      }
	      
	      	

/**
 * Continue to play
 */

	      while (continueToPlay) {
	    

	        if (player == PLAYER1) {
	        	/**
	        	 *  Wait for player 1 to move
	        	 */
	          waitForPlayerAction(); 
	          /**
	           *  Send the move to the server
	           */
	          sendMove(); 
	          /**
	           * Receive info from the server
	           */
	          receiveInfoFromServer(username); 
	        }
	        else if (player == PLAYER2) {
	         /**
	          * Receive info from the server
	          */
	        	receiveInfoFromServer(username); 
	          
	          /**
	           *  // Wait for player 2 to move
	           */
	          waitForPlayerAction();
	         /**
	          *  Send player 2's move to the server
	          */
	          sendMove(); 
	        }
	      }
	    }
	    /**
	     * catch Exception
	     */
	    catch (Exception ex) {
	    	 System.err.println(ex);
	    }
	  }
/**
 * thread sleeping for 100 milisecond 
 * @throws InterruptedException
 */
	  private synchronized void waitForPlayerAction() throws InterruptedException {

	    while (isWaiting()) { 
		
	      Thread.sleep(100);
	    }

	   /**
	    * selected a board position, so proceed and send on the move to the server side
	    */

	    setWaiting(true);
	  }
/**
 * send move method to send the row and column selected
 * @throws IOException
 */
	  private void sendMove() throws IOException {

	    toServer.writeInt(getRowSelected()); // Send the selected row
	    toServer.writeInt(getColumnSelected()); // Send the selected column
	  }
/**
 * to receive information from server
 * @param username
 * @throws IOException
 */
	  private void receiveInfoFromServer(String username) throws IOException {

	 /**
	  * Reads from server
	  */
	    int status = fromServer.readInt();

	    if (status == PLAYER1_WON) {
	   /**
	    *  if player one wins them stop playing
	    */
	      	setGameInProgress(false);
	      if (getMyColor() == Color.blue) {
	        setStatusMessage("Congratulations , " + username + " You have Won!  (BLUE)");
	     
	
	     }
	      /**
	       * if statement to show text message that you have lost 
	       */
	      else if (getMyColor() == Color.red) {
	        setStatusMessage("Sorry, " + username + " You have Lost(RED)");
	        receiveMove();
	      }
	    }
	    else if (status == PLAYER2_WON) {
	      /**
	       *  stop playing when player 2 wins
	       */
	     	setGameInProgress(false);
	      if (getMyColor() == Color.red) {
	      //	cell[getRowSelected()][getRowSelected()].setBorder(new LineBorder(Color.black, 1));
	        setStatusMessage("Congratulations, " + username + " You have Won! (Red)");
	       
	 
	      }
	      else if (getMyColor() == Color.blue) {
	        setStatusMessage("Sorry ," + username + " You have Lost (BLUE)");
	        receiveMove();
	      }
	    }
	    else if (status == DRAW) {
	     /**
	      *  No winner, game is over
	      */
	      setGameInProgress(false);
	      setStatusMessage(" Game is over, no winner!");

	      if (getMyColor() == Color.red) {
	        receiveMove();
	      }
	    }
	    else {
	    	/**
	    	 * game in progess carry on playing
	    	 */
	      setGameInProgress(true);
	    
	      receiveMove();
	      setStatusMessage( "Turn: " + username);
	      /**
	       * // set turn
	       */
	      setMyTurn(true); 
	    }
	  }
	  
	  

	  private void receiveMove() throws IOException {
	    // Get the other payer's move
	    int row = fromServer.readInt();
	    int column = fromServer.readInt();
	    cell[row][column].setColor(otherColor);
	  }
	  

	 /**
	  * invokes when acton occurs i.e. starts new game
	  */
	   	@Override 
	   public void actionPerformed(ActionEvent evt) {
	     	
	            //
	      		 String command = evt.getActionCommand();
	              
	              
	             	if (command.equals("NEW GAME!") ){
	             
	             	mainPanel.setVisible(false);
	             	infoPanel.setVisible(false);
	             	sidePanel.setVisible(false);
	             	//setGameInProgress(true);
	            	createGUI();
	            	setGameInProgress(true);
	            	
	            
	             
	            //	new Connect4Client("Connect4 Client");
	             }

		 }


		public class Cell extends JPanel  {

	 /**
	  * Indicate the row and column of this cell in the board
	  */
	    private int row;
	    private int column;
	    
	   /**
	    * Multidimension array 7*7
	    */
		private char[][] cell =  new char[7][7];
	   /**
	    * Token used for this cell
	    */
	    private Color myColor ;

	    private Connect4Client parent;

	 	
	    public Cell(int row, int column, Connect4Client gui) {

	      this.row = row;
	      this.column = column;
	      this.parent = gui;

	
	   	  setBackground(Color.CYAN);
	   	  /**
	   	   * listener to the click of the mouse
	   	   */
	      addMouseListener(new ClickListener());     
	    }

	    /** Return token */
	    public Color getColor() {
	      return myColor;
	    }
	    


	    /** Set a new token */
	    public void setColor(Color c) {
	      myColor = c;
	      repaint();
	    }

	    /** Paint the cell */

	 protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            int size = Math.min(getWidth(), getHeight());
	            int offset = (int) ((double) size / 12);
	            size = size - 2 * offset;
	            
	            if (myColor == null) {
	               g.setColor(Color.black);
	               g.drawOval(offset, offset, size, size);
	             //g.setColor(Color.white);
	            } 
	           	else {
	                g.setColor(myColor);
	                g.fillOval(offset, offset, size, size);
	            }

	        }
	    
	              
	  
	    /** Handle mouse click on a cell */
		private class ClickListener extends MouseAdapter {

		    public void mouseClicked(MouseEvent e) {

		    /**
		     * If cell is not occupied and the player has the turn
		     */

		        if ((myColor == null) && parent.getMyTurn()) {
/**
 * Set the player's token in the cell
 */
		          setColor(parent.getMyColor());   
		          parent.setMyTurn(false);
		          parent.setRowSelected(row);
		          parent.setColumnSelected(column);
		          parent.setStatusMessage("Waiting for the other player to move");
		          parent.setWaiting(false);        

		        }
		    }
		 }
	}
	//end of inner classs


	  public void setMyTurn(boolean b) {
		  myTurn = b;
	  }
	
	  public boolean getMyTurn() {
		  return myTurn;
	  }

	  public Color getMyColor() {
		  return myColor;
	  }


	  public void setMyColor(Color c) {
		  myColor = c;
	  }

	
	  public Color getOtherColor() {
		  return otherColor;
	  }
	  

	  public void setOtherColor(Color c) {
		  otherColor = c;
	  }


	  public void setRowSelected(int r) {
		  rowSelected = r;
	  }

	
	  public int getRowSelected() {
		  return rowSelected;
	  }

	  public void setColumnSelected(int c) {
		  columnSelected = c;
	  }


	  public int getColumnSelected() {
		  return columnSelected;
	  }

	  public void setWaiting(boolean b) {
		  waiting = b;
	  }
	
	  public boolean isWaiting() {
		  return waiting;
	  }

	  public void setStatusMessage(String msg) {
	      statusLbl.setText(msg);
	  }


	  public void setPlayerNameMessage(String msg) {
	     nameLabel.setText(msg);
	  }

	  
	 public void setGameInProgress(boolean inProgress) {
	                
	           continueToPlay =inProgress;
	             if ( continueToPlay){
	                newGame.setEnabled(false);
	                
	             }
	             else {
	                newGame.setEnabled(true);
	             }
	          }

	/**
	 * main method in Client which should be compile after server main class is compile
	 * @param args
	 */
	  public static void main(String[] args) {

	    Connect4Client game = new Connect4Client("Connect_4_Client");

	  }
	
  }


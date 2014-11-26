package Server;
/**
 * package Server with the main method
 */
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
/**
 * 
 * @author resham gurung
 * connect4Server class extends jframe to implement gui
 *
 */
public class Connect4Server extends JFrame implements GameConstants {

	/**
	 * serial from eclipse ide
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * iniatilization of serverSocket,
	 * player one and two socket and creating accessing gameSession through game
	 */
	private ServerSocket serverSocket;
    private Socket player1;
    private Socket player2;
    private GameSession game;

	/**
	 * Constructor which implements creates object of jtextarea
	 * scrollpane allows to add more text inside the box with scroll bar which hold textarea
	 */
    public Connect4Server() {

        JTextArea serverLog = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(serverLog);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);//jframe size
        setTitle("Connect_4_Server");
        setVisible(true);

        try {
/**
 * creates server socket with port no 7000 and writes a text in the text area with current date
 */
            serverSocket = new ServerSocket(7000);

            serverLog.append(new Date() + ": Server started at socket 7000\n");

       /**
        *  Number a session
        */
            int sessionNo = 1;


         /**
          *  while loop keep the loop going until invoke by a method, to create a session for every two players
          */

            while (true) {

                serverLog.append(new Date() + ": Waiting for Players " + sessionNo + '\n');

                // Connect to player 1
                player1 = serverSocket.accept();

                serverLog.append(new Date() + ": Player 1 joined session " + sessionNo + '\n');

                serverLog.append(new Date() +"Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');

                // Notify that the player is Player 1

                new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1);

                // Connect to player 2
                player2 = serverSocket.accept();

                serverLog.append(new Date() + ": Player 2 joined session " + sessionNo + '\n');

                serverLog.append(new Date() + "Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');

                // Notify that the player is Player 2

                new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2);

                // Display this session and increment session number

                serverLog.append(new Date() + ": Thread session started " + sessionNo++ + '\n');

/**
 *  Create a new thread for this session of two players
 */
               

                game = new GameSession(player1, player2);

                // Start the new thread
                new Thread(game).start();

          }
            /**
             * EOFException- signal end of file or terminated unexceptively
             */
        }catch(EOFException ex){
        	ex.addSuppressed(ex);
        }
        /**
         * input output exception
         */
        catch(IOException ex) {
            System.err.println(ex);
        }
    }
/**
 * main method 
 * @param args
 */
    public static void main(String[] args) {

        Connect4Server frame = new Connect4Server();
    }

}
package socketclient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class chatClient {
	
	private static final String nameCode = "}{[]@#$!";
	private static final String exitCode = "}{[]@#!$";
	private static chatClientGUI gui;
	private static String userName = "";
	private static PrintWriter out;
	private static Socket SOCK;
	private static final String serverName = "strobefi.com";
	private static final int port = 8080;
	
	public static void main(String[] args) {
		initialize();
		connect();
		
		Scanner input = new Scanner(System.in);
		
		while(true) {
			out.println(input.nextLine());
			out.flush();
		}
	}
	
	public static void connect() {	
		try {	
			SOCK = new Socket(serverName, port);			
			out = new PrintWriter(SOCK.getOutputStream());
			
			//send username to server
			out.println(nameCode + userName);
			out.flush();

			//open thread to receive data from server
			dataReceiver dr = new dataReceiver(SOCK);
			Thread t = new Thread(dr);
			t.start();		
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void disconnect() {
		try {
			out.println(exitCode);
			out.flush();
			if(!SOCK.isClosed()) {
				SOCK.close(); //close connection socket
			}
			gui.dispose(); //close GUI
			System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void messageReceived(String msg) {
		gui.appendChatArea(msg + "\n");
	}
	
	public static void initialize() {
		gui = new chatClientGUI();
		userName = JOptionPane.showInputDialog("Enter a username.");	
		gui.setVisible(true);
		gui.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	            disconnect();
	        }
	    });	
	}
	
	public static void updateUsers(String[] users) {
		gui.updateUserList(users);
	}

	public static void newMessage() {
		String msg = gui.messageArea.getText();
		gui.messageArea.setText(null);
		
		out.println(msg);
		out.flush();
	}
}

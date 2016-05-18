package socketclient;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class dataReceiver implements Runnable {

	private final String usersCode= "}{[]@$!#";
	private Scanner input;
	private Socket SOCK;
	private String msg = "";

	public dataReceiver(Socket sock) {
		this.SOCK = sock;
		
		try {
			input = new Scanner(SOCK.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		while (true) {
			if (!input.hasNext()) {
				return;
			}
			msg = input.nextLine();

			if (msg.startsWith(usersCode)) {
				updateUsers();
			} else {
				chatClient.messageReceived(msg);
			}
		}
	}

	public void updateUsers() {
		
		//this msg should be an integer with how many connected users there are
		msg = input.nextLine();
		
		int arraySize = Integer.parseInt(msg);
		
		String[] users = new String[Integer.parseInt(msg)];

		for(int i = 0; i < users.length; i++) {
			if (!input.hasNext()) {
				return;
			}
			msg = input.nextLine();
			users[i] = msg;				
		}
		
		chatClient.updateUsers(users);
	}
}

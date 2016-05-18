package socketclient;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class chatClientGUI extends JFrame {
	
	private JTextArea chatArea = new JTextArea();
	private JScrollPane chatScrollPane = new JScrollPane();
	private JScrollBar vertical = chatScrollPane.getVerticalScrollBar();
	private JList userList = new JList();
	private DefaultListModel userListModel = new DefaultListModel();
	private KeyStroke keyStroke;
	JTextArea messageArea = new JTextArea();
	
	public chatClientGUI() {
		
		Action msgSenderAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                chatClient.newMessage();
            }
        };
		
		String sName = "localhost";
		Scanner userInput = new Scanner(System.in);
		int port = 8080;
			
		setTitle("Sketchy Chat");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		getContentPane().setLayout(new MigLayout("", "[100::,grow][100!]", "[grow][60!]"));
		
		getContentPane().add(chatScrollPane, "cell 0 0 2 1,grow");

		JScrollPane userScrollPane = new JScrollPane();
		getContentPane().add(userScrollPane, "cell 1 1, grow");
		userScrollPane.setViewportView(userList);
		
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		chatScrollPane.setViewportView(chatArea);
		
		
		getContentPane().add(messageArea, "cell 0 1,grow");
		
		keyStroke = KeyStroke.getKeyStroke("ENTER"); //detect enter key for sending msg
        Object actionKey = messageArea.getInputMap(
                JComponent.WHEN_FOCUSED).get(keyStroke);
        messageArea.getActionMap().put(actionKey, msgSenderAction);
	}
	
	public void appendChatArea(String msg) {
		msg = addTimestamp(msg);
		
		chatArea.append(msg);
		vertical.setValue(vertical.getMaximum()); //move the scrollbar to the bottom each time new message received
	}
	
	public String addTimestamp(String msg) {		
		String temp[] = msg.split(":", 2); //split string where username ends
		
		String dateFormat = "h:mm:ss a"; // hour:minute:second AM/PM
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		
		String timeStamp = sdf.format(new Date()); //create the time stamp
		
		String timedMsg = temp[0] + " (" + timeStamp + "):" +temp[1]; //combine it all and format nicely
		
		return timedMsg;
	}
	
	public void updateUserList(String[] users) {
		userListModel.clear(); //empty user list
		
		for(int i = 0; i < users.length; i++) { //create new user list
			userListModel.addElement(users[i]);
		}
		
		userList.setModel(userListModel); //update GUI
	}
}

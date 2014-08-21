package jz34_lw23.view;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import comp310f13.rmiChat.IUser;

import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatRoomGUI extends JPanel {
	private static final long serialVersionUID = -6075466661842528175L;
	private final JSplitPane splitPane = new JSplitPane();
	private final JSplitPane splitPaneConversation = new JSplitPane();
	private final JScrollPane scrollPaneListen = new JScrollPane();
	private final JTextArea textReceive = new JTextArea();
	private final JSplitPane splitPaneTalk = new JSplitPane();
	private final JTextArea textSend = new JTextArea();
	private final JButton btnSend = new JButton("Send");

	private IMiniModelAdaptor adaptor;
	private final JSplitPane splitPaneUserAndLeave = new JSplitPane();
	/*
	 * Since we are not selecting IUser from the room, we can just display
	 * string
	 */
	private final JList<String> listUsers = new JList<String>();
	private final JButton btnLeave = new JButton("Leave");
	private final JPanel panelName = new JPanel();
	private final JLabel lblName = new JLabel("Chat Room");

	/* The container: the tabbedPane on the main GUI. */
	private Container container;
	private Component thisObject = this;

	/**
	 * Create the panel
	 * 
	 * @param adaptor
	 *            the miniView-to-miniModel adaptor
	 * @param container
	 *            the container that contains this GUI.
	 */
	public ChatRoomGUI(IMiniModelAdaptor adaptor, Container container) {
		this.adaptor = adaptor;
		this.container = container;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		textSend.setToolTipText("Input what you want to say here.");
		textSend.setLineWrap(true);
		textSend.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// shift enter writes new line
					if (e.isShiftDown()) {
						textSend.setText(textSend.getText() + "\n");
					}
					// enter without shift sends message
					else {
						send();
					}
					// done processing event
					e.consume();
				}
			}
		});
		splitPane.setResizeWeight(0.3);

		add(splitPane, BorderLayout.CENTER);
		splitPaneConversation.setResizeWeight(0.9);
		splitPaneConversation.setOrientation(JSplitPane.VERTICAL_SPLIT);

		splitPane.setRightComponent(splitPaneConversation);
		scrollPaneListen.setViewportBorder(new TitledBorder(null, "Message",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		splitPaneConversation.setLeftComponent(scrollPaneListen);
		textReceive.setToolTipText("Received message");
		textReceive.setLineWrap(true);
		textReceive.setEditable(false);

		scrollPaneListen.setViewportView(textReceive);
		splitPaneTalk.setResizeWeight(0.9);

		splitPaneConversation.setRightComponent(splitPaneTalk);

		splitPaneTalk.setLeftComponent(textSend);
		btnSend.addActionListener(new ActionListener() {
			/**
			 * Sends the typed message to the everyone in the chatroom
			 */
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		btnSend.setToolTipText("Send message to selected user");

		splitPaneTalk.setRightComponent(btnSend);
		splitPaneUserAndLeave.setResizeWeight(0.92);
		splitPaneUserAndLeave.setOrientation(JSplitPane.VERTICAL_SPLIT);

		splitPane.setLeftComponent(splitPaneUserAndLeave);
		listUsers.setToolTipText("List of users in this chat room");
		listUsers.setBorder(new TitledBorder(null, "User List",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		splitPaneUserAndLeave.setLeftComponent(listUsers);
		btnLeave.setToolTipText("Leave the room");
		btnLeave.addActionListener(new ActionListener() {
			/**
			 * Leaves the chatroom
			 */
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});

		splitPaneUserAndLeave.setRightComponent(btnLeave);

		add(panelName, BorderLayout.NORTH);

		panelName.add(lblName);
		lblName.setText(adaptor.getRoomName());
	}

	/**
	 * Start the chatroom GUI
	 */
	public void start() {
		this.setVisible(true);
	}

	/**
	 * Leave the chatRoom
	 */
	public void stop() {
		adaptor.stop();
		container.remove(thisObject);
	}

	/**
	 * If I have things to say (non-empty text area), send it to everyone in the
	 * room
	 */
	private void send() {
		if (textSend.getText().length() > 0) {
			String message = textSend.getText();
			textSend.setText("");
			adaptor.send(message);
		}
	}

	/**
	 * What others said to you, append it to this GUI
	 * 
	 * @param message
	 *            other's message.
	 */
	public void append(String message) {
		textReceive.append(message);
		textReceive.setCaretPosition(textReceive.getText().length());
	}

	/**
	 * If other user asks you to add a component, add it to the same container
	 * that holds the chat room GUI.
	 * 
	 * @param name
	 *            the name of the component to be added.
	 * @param comp
	 *            the component to be added
	 */
	public void addComponent(String name, Component comp) {
		container.add(name, comp);
	}

	/**
	 * Repaint the User List. Called when someone join or leaves the room.
	 * 
	 * @param users the current list of IUser stub in the room.
	 */
	public void refreshUserList(Iterable<IUser> users) {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		listUsers.setModel(listModel);
		for (IUser user : users) {
			try {
				listModel.addElement(user.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

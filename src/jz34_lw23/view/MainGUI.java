package jz34_lw23.view;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * MainGUI of the ChatApp
 * 
 * @author Jiao Zhang
 * 
 */
public class MainGUI extends JFrame {
	private static final long serialVersionUID = -2949893662803045333L;
	private JPanel contentPane;

	private IModelAdaptor adaptor;
	private final JPanel panelControl = new JPanel();
	private final JTextField textName = new JTextField();
	private final JButton btnLogin = new JButton("Login");
	private final JTextField textRemoteHost = new JTextField();
	private final JButton btnConnect = new JButton("Connect");
	private final JTextField textNewRoom = new JTextField();
	private final JButton btnCreateRoom = new JButton("Create Room");
	private final JPanel panelMyRooms = new JPanel();
	private final JPanel panelHosts = new JPanel();
	private final JScrollPane scrollPaneHosts = new JScrollPane();
	private final JList<IHost> listHosts = new JList<IHost>();
	private final JButton btnInvite = new JButton("Invite");
	private final JScrollPane scrollPaneMyRooms = new JScrollPane();
	private final JList<IChatRoom> listMyRooms = new JList<IChatRoom>();
	private final JSplitPane splitPaneMainView = new JSplitPane();
	private final JTabbedPane tabbedPaneChatRooms = new JTabbedPane(
			JTabbedPane.TOP);
	private final JScrollPane scrollPaneSystem = new JScrollPane();
	private final JTextArea textAreaSystem = new JTextArea();

	/**
	 * Create the frame.
	 */
	public MainGUI(IModelAdaptor adaptor) {
		this.adaptor = adaptor;
		initGUI();
	}

	private void initGUI() {
		addWindowListener(new WindowAdapter() {
			@Override
			/**
			 * When close button is clicked, leave all the rooms before stopping the MainModel.
			 */
			public void windowClosing(WindowEvent arg0) {
				for (Component c : tabbedPaneChatRooms.getComponents()) {
					if (c instanceof ChatRoomGUI) {
						((ChatRoomGUI) c).stop();
					}
				}
				adaptor.stop();
			}
		});
		setTitle("ChatRoom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 824, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		textNewRoom.setToolTipText("Input the name of the new chat room you want to create.");
		textNewRoom.setColumns(10);
		textRemoteHost.setToolTipText("The IP address of the remote host you want to connect to");
		textRemoteHost.setColumns(10);
		textName.setToolTipText("Input your name");
		textName.setColumns(10);
		contentPane.add(panelControl, BorderLayout.NORTH);

		panelControl.add(textName);
		btnLogin.setToolTipText("Log in with the name. Once this is done, you cannot change your name");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Log in, set the name of the local user, and enable all the
				 * other functions.
				 */
				setName();
			}
		});

		panelControl.add(btnLogin);

		panelControl.add(textRemoteHost);
		btnConnect.setToolTipText("Connect to a remote host using his IP");
		btnConnect.setEnabled(false);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textRemoteHost.getText().length() > 0) {
					/**
					 * connect to a remote host.
					 */
					adaptor.connect(textRemoteHost.getText());
				}
			}
		});

		panelControl.add(btnConnect);

		panelControl.add(textNewRoom);
		btnCreateRoom.setToolTipText("Create a new chat room");
		btnCreateRoom.setEnabled(false);
		btnCreateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textNewRoom.getText().length() > 0) {
					/**
					 * Create a name with non-empty name.
					 */
					adaptor.createRoom(textNewRoom.getText());
				}
			}
		});

		panelControl.add(btnCreateRoom);
		panelMyRooms.setBorder(new TitledBorder(null, "My Rooms",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		contentPane.add(panelMyRooms, BorderLayout.WEST);
		panelMyRooms.setLayout(new MigLayout("", "[120px,grow]",
				"[190px,grow][]"));

		panelMyRooms.add(scrollPaneMyRooms, "cell 0 0,grow");
		listMyRooms.setToolTipText("The list of chat rooms you are in.");
		listMyRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPaneMyRooms.setViewportView(listMyRooms);
		panelHosts.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Connected Hosts",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		contentPane.add(panelHosts, BorderLayout.EAST);
		panelHosts
				.setLayout(new MigLayout("", "[120px,grow]", "[190px,grow][]"));

		panelHosts.add(scrollPaneHosts, "cell 0 0,grow");
		listHosts.setToolTipText("The list of hosts you are connected to.");
		listHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPaneHosts.setViewportView(listHosts);
		btnInvite.setToolTipText("Select a room and a remote host to invite.");
		btnInvite.setEnabled(false);
		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * When a host and a room is selected, invite that host to the
				 * room.
				 */
				if (listHosts.getSelectedValue() != null
						&& listMyRooms.getSelectedValue() != null) {
					adaptor.invite(listMyRooms.getSelectedValue(),
							listHosts.getSelectedValue());
				}
			}
		});

		panelHosts.add(btnInvite, "cell 0 1");
		splitPaneMainView.setResizeWeight(0.9);
		splitPaneMainView.setOrientation(JSplitPane.VERTICAL_SPLIT);

		contentPane.add(splitPaneMainView, BorderLayout.CENTER);
		tabbedPaneChatRooms.setToolTipText("The main area to show you chat rooms and other added component");

		splitPaneMainView.setLeftComponent(tabbedPaneChatRooms);
		scrollPaneSystem.setAutoscrolls(true);

		splitPaneMainView.setRightComponent(scrollPaneSystem);
		textAreaSystem.setToolTipText("System messages.");
		scrollPaneSystem.setViewportView(textAreaSystem);
	}

	/**
	 * Log in, set the name, enable all the other functions, and don't allow the
	 * user to change his name again.
	 */
	private void setName() {
		if (textName.getText().length() > 0) {
			adaptor.login(textName.getText());
		}
		btnLogin.setEnabled(false);
		btnConnect.setEnabled(true);
		btnCreateRoom.setEnabled(true);
		btnInvite.setEnabled(true);
	}

	/**
	 * Start the main GUI. set it to be visible.
	 */
	public void start() {
		this.setVisible(true);
	}

	/**
	 * Make a new ChatRoom GUI, and add it to the main GUI.
	 * 
	 * @param adaptor
	 *            the miniModelAdaptor that is used by this new ChatRoom GUI
	 * @return the newly created & added ChatRoom GUI.
	 */
	public ChatRoomGUI addChatRoomGUI(IMiniModelAdaptor adaptor) {
		ChatRoomGUI room = new ChatRoomGUI(adaptor, tabbedPaneChatRooms);
		tabbedPaneChatRooms.add(adaptor.getRoomName(), room);
		tabbedPaneChatRooms.setSelectedComponent(room);
		room.start();
		return room;
	}

	/**
	 * Append the a text message onto the System message text area.
	 * @param message the text message to be appended.
	 */
	public void appendSystemMessage(String message) {
		textAreaSystem.append(message);
		textAreaSystem.setCaretPosition(textAreaSystem.getText().length());
	}

	/**
	 * Refresh the list of rooms the local user is in.
	 * @param rooms the current list of rooms the local user is in.
	 */
	public void refreshRoomList(List<IChatRoom> rooms) {
		DefaultListModel<IChatRoom> listModel = new DefaultListModel<IChatRoom>();
		listMyRooms.setModel(listModel);
		listMyRooms.setCellRenderer(new RoomListRenderer());
		for (IChatRoom room : rooms) {
			try {
				listModel.addElement(room);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Refresh the list of remote hosts the local user is connected to
	 * @param hosts the current list of hosts the local user is connected to
	 */
	public void refreshHostList(List<IHost> hosts) {
		DefaultListModel<IHost> listModel = new DefaultListModel<IHost>();
		listHosts.setModel(listModel);
		listHosts.setCellRenderer(new HostListRenderer());
		for (IHost host : hosts) {
			listModel.addElement(host);
		}
	}
}

/**
 * The list-cell-renderer for the list of remote hosts
 * @author Chao
 *
 */
class HostListRenderer extends DefaultListCellRenderer {
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 6606799736510116345L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Component renderer = super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		if (renderer instanceof JLabel && value instanceof IHost) {
			try {
				((JLabel) renderer).setText(((IHost) value).getName());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return renderer;
	}
}

/**
 * The list-cell-renderer for the list of chat rooms.
 * @author Chao
 *
 */
class RoomListRenderer extends DefaultListCellRenderer {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 8394787714710518326L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Component renderer = super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		if (renderer instanceof JLabel && value instanceof IChatRoom) {
			((JLabel) renderer).setText(((IChatRoom) value).getName());
		}
		return renderer;
	}
}
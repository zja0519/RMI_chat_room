package jz34_lw23.model;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import jz34_lw23.model.impl.ChatRoom;
import jz34_lw23.model.impl.Host;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import provided.util.IVoidLambda;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;

/**
 * The main model that backs up the whole application.
 * 
 * @author Jiao Zhang
 * 
 */
public class MainModel {
	/**
	 * The Model-to-View Adaptor
	 */
	private IViewAdaptor adaptor;
	/**
	 * The IHost instance that represents the local user.
	 */
	private Host myHost;
	/**
	 * The utilities of rmi. Info status will be appended onto the view.
	 */
	private RMIUtils rmi = new RMIUtils(new IVoidLambda<String>() {
		@Override
		public void apply(String... messages) {
			for (String message : messages) {
				adaptor.append(message);
			}
		}
	});
	/**
	 * The registry that will hold my IHost stub
	 */
	private Registry registry;

	/**
	 * A reference to this model, saved for future use.
	 */
	private MainModel thisModel = this;

	/**
	 * Constructor
	 * 
	 * @param adaptor
	 *            the MainModel-to-MainView adaptor.
	 */
	public MainModel(IViewAdaptor adaptor) {
		this.adaptor = adaptor;
	}

	/**
	 * Sets the person's name
	 * 
	 * @param name
	 *            the name the user wants to present to others.
	 */
	public void setName(String name) {
		myHost.setName(name);
		adaptor.append("You logged in as \"" + name + "\"\n");
	}

	/**
	 * Gets the local user's name
	 * 
	 * @return the local user's name
	 */
	public String getName() {
		try {
			return myHost.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			return "A host without name";
		}
	}

	/**
	 * Actively creates a room and builds the mini-MVC
	 * 
	 * @param roomName
	 *            the name of the room to be built.
	 */
	public void createRoom(String roomName) {
		IChatRoom room = new ChatRoom(roomName);
		try {
			myHost.addToChatRoom(room);
		} catch (RemoteException e) {
			adaptor.append("Failed to create room.\n");
			e.printStackTrace();
		}
	}

	/**
	 * Give a name to join, build the mini-MVC
	 * 
	 * @param room
	 *            the ChatRoom to join
	 */
	public void joinRoom(IChatRoom room) {
		try {
			myHost.addToChatRoom(room);
		} catch (RemoteException e) {
			adaptor.append("Failed to join room.\n");
			e.printStackTrace();
		}
	}

	/**
	 * Connect to a remote host
	 * 
	 * @param remoteIP
	 *            the IP address of the remote host.
	 */
	public void connect(final String remoteIP) {
		new Thread() {

			@Override
			public void run() {
				try {
					Registry remoteRegistry = rmi.getRemoteRegistry(remoteIP);
					adaptor.append("Connected to remote host: " + remoteIP
							+ "\n");
					IHost remoteStub = (IHost) remoteRegistry
							.lookup(IHost.BOUND_NAME);
					IHost localStub = (IHost) rmi.getLocalRegistry().lookup(
							IHost.BOUND_NAME);
					remoteStub.sendLocalHostStub(localStub);
					// If not connecting to self.
					if (!(remoteStub.getUUID().equals(myHost.getUUID()))) {
						myHost.sendLocalHostStub(remoteStub);
					}
					adaptor.append("Connection successful to " + remoteIP
							+ ".\n");
				} catch (Exception e) {
					adaptor.append("Connection failed to " + remoteIP + ".\n");
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Invite a remote host to a particular chat room
	 * 
	 * @param room
	 *            the chat room you want to invite the remote host to
	 * @param remoteHost
	 *            the remote host to be invited.
	 */
	public void invite(final IChatRoom room, final IHost remoteHost) {
		new Thread() {

			@Override
			public void run() {
				try {
					adaptor.append("Try to invite remote Host: "
							+ remoteHost.getName() + "\n");
					String chatroomInfo = myHost.getName()
							+ " invites you to room: " + room.getName();
					boolean accept = remoteHost.sendInvite(chatroomInfo);
					if (accept) {
						if (remoteHost.addToChatRoom(room)) {
							adaptor.append("Successfully invited remoteHost: "
									+ remoteHost.getName() + "\n");
						}
					} else {
						adaptor.append(remoteHost.getName()
								+ " declined your invitation!\n");
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Given a chatroom (either actively instantiated or obtained by joining),
	 * builds the corresponding mini-MVC
	 * 
	 * @param room
	 */
	public void miniMVC(final IChatRoom room) {
		new Thread() {

			@Override
			public void run() {
				try {
					ChatRoomModel roomModel = new ChatRoomModel(room);
					roomModel.setMainModel(thisModel);
					roomModel.setAdaptor(adaptor
							.createMiniViewAdaptor(roomModel));
					roomModel.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Start the model: start the Registry, build the IHost Stub, and register
	 * the Stub in the Registry.
	 */
	public void start() {
		rmi.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);
		try {
			myHost = new Host(thisModel);
			adaptor.append("Started a new Host!\n");
			IHost hostStub = (IHost) UnicastRemoteObject.exportObject(myHost,
					IHost.CONNECTION_PORT);
			registry = rmi.getLocalRegistry();
			registry.rebind(IHost.BOUND_NAME, hostStub);
			adaptor.append("Binds host to locally registry successfully!\n");
		} catch (Exception e) {
			System.err.println("Failed to start the Model:");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Stop the Main Model by unbinding the IHost stub from registry and stop
	 * the class file server.
	 */
	public void stop() {
		try {
			registry.unbind(IHost.BOUND_NAME);
			System.out.println("IHost object has been unbounded: "
					+ IHost.BOUND_NAME + "\n");
			rmi.stopRMI();
			System.out.println("Quit....\n");
		} catch (Exception e) {
			System.err.println("Error unbinding IHost object: "
					+ IHost.BOUND_NAME + "\n");
			System.exit(-1);
		}
	}

	/**
	 * When receive an invitation, handles it.
	 * 
	 * @param chatroomInfo
	 *            the invitation message.
	 * @return from the MainGUI, the user's decision to join or not.
	 */
	public boolean handleInvite(String chatroomInfo) {
		return adaptor.handleInvite(chatroomInfo);
	}
	
	/**
	 * When the list of connected remote hosts changes, refresh it on the MainView 
	 */
	public void refreshRemoteHostList() {
		adaptor.refreshHostList(myHost.getRemoteHosts());
	}
	
	/**
	 * When the list of chat rooms "I" am in changes, refresh it on the MainView.
	 */
	public void refreshRoomList() {
		adaptor.refreshRoomList(myHost.getChatRooms());
	}

	public void removeRoom(IChatRoom room) {
		myHost.removeChatRoom(room);
		refreshRoomList();
	}
}

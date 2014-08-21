package jz34_lw23.view;

import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;

/**
 * The interface that defines the contract of the MainView-to-MainModel adaptor
 * 
 * @author Jiao Zhang
 * 
 */
public interface IModelAdaptor {
	/**
	 * Create a new room
	 * 
	 * @param roomName
	 *            the name of the room to be created.
	 */
	public void createRoom(String roomName);

	/**
	 * Log in, sets the name of the local user.
	 * 
	 * @param name
	 *            name of the local user.
	 */
	public void login(String name);

	/**
	 * Invite a remote host to a room.
	 * 
	 * @param room
	 *            the room to invite another people to join
	 * @param remoteHost
	 *            the remote host to invite.
	 */
	public void invite(IChatRoom room, IHost remoteHost);

	/**
	 * Connect to a remote host
	 * 
	 * @param remoteIP
	 *            the IP of the remote host.
	 */
	public void connect(String remoteIP);

	/**
	 * Stop the MainModel.
	 */
	public void stop();
}

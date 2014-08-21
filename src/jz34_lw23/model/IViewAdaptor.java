package jz34_lw23.model;

import java.util.List;

import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;

/**
 * The interface defining the contract of the MainModel-to-MainView adaptor
 * 
 * @author Jiao Zhang
 * 
 */
public interface IViewAdaptor {
	/**
	 * Append a text string somewhere on the MainView
	 * 
	 * @param str
	 *            the string to be appended
	 */
	public void append(String str);

	/**
	 * Create a MiniModel-to-MiniView adaptor instance for a particular
	 * MiniModel
	 * 
	 * @param roomModel
	 *            the MiniModel that needs a new adaptor
	 * @return a MiniModel-to-MiniView adaptor.
	 */
	public IMiniViewAdaptor createMiniViewAdaptor(ChatRoomModel roomModel);

	/**
	 * When receives an invitation from other user, present and let user handle
	 * such invitation in the MainView
	 * 
	 * @param chatroomInfo
	 *            the invitation message
	 * @return whether or not the user accpets this invitation.
	 */
	public boolean handleInvite(String chatroomInfo);

	/**
	 * When the list of remote hosts "I" am connected to changes, refresh the
	 * list on the MainView
	 * 
	 * @param hosts
	 *            the list of connected remote hosts
	 */
	public void refreshHostList(List<IHost> hosts);

	/**
	 * When the list of rooms "I" am in changes (either through creation of
	 * join), refresh the list on the MainView
	 * 
	 * @param rooms
	 *            the list of rooms "I" am in.
	 */
	public void refreshRoomList(List<IChatRoom> rooms);
}

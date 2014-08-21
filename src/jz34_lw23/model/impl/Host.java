package jz34_lw23.model.impl;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import jz34_lw23.model.MainModel;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;

/**
 * The local implementation of IHost
 * 
 * @author Jiao Zhang
 * 
 */
public class Host implements IHost {

	/**
	 * The remote hosts that this host is connected to.
	 */
	private List<IHost> remoteHosts;
	/**
	 * The chat rooms this host is in.
	 */
	private List<IChatRoom> myChatRooms;
	/**
	 * The MainModel that holds this host.
	 */
	private MainModel model;
	/**
	 * The name of this host;
	 */
	private String name;
	/**
	 * The UUID.
	 */
	private UUID uuid;

	public Host(MainModel model) {
		remoteHosts = new LinkedList<IHost>();
		myChatRooms = new LinkedList<IChatRoom>();
		this.model = model;
		uuid = UUID.randomUUID();
	}

	@Override
	public void sendLocalHostStub(IHost localHostStub) throws RemoteException {
		remoteHosts.add(localHostStub);
		model.refreshRemoteHostList();
	}

	@Override
	public boolean sendInvite(String chatroomInfo) throws RemoteException {
		return model.handleInvite(chatroomInfo);
	}

	@Override
	public boolean addToChatRoom(IChatRoom localChatRoom)
			throws RemoteException {
		/**
		 * Return false when this Host is already in a room with same name. So
		 * locally, I don't allow myself in two rooms of the same name.
		 */
		for (IChatRoom room : myChatRooms) {
			if (localChatRoom.getName().equals(room.getName())) {
				return false;
			}
		}

		myChatRooms.add(localChatRoom);
		model.miniMVC(localChatRoom);
		model.refreshRoomList();
		return true;
	}

	@Override
	public String getName() throws RemoteException {
		if (name == null) {
			return "A Host without name";
		} else {
			return name;
		}
	}

	@Override
	public UUID getUUID() throws RemoteException {
		return uuid;
	}

	/**
	 * Get the chatRooms this host is in
	 * 
	 * @return a list of chatRooms this host is in.
	 */
	public List<IChatRoom> getChatRooms() {
		return myChatRooms;
	}

	/**
	 * Get a list of remote hosts this host is connected to
	 * 
	 * @return a list of remote hosts this host is connected to.
	 */
	public List<IHost> getRemoteHosts() {
		return remoteHosts;
	}

	/**
	 * Set the name of this host.
	 * 
	 * @param name
	 *            the name of the host to be set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Remove a chat room from the room list when the user leaves.
	 * 
	 * @param room
	 *            the room to leave
	 */
	public void removeChatRoom(IChatRoom room) {
		myChatRooms.remove(room);
	}

	/**
	 * add a chat room from the room list when the user creates or joins a room.
	 * 
	 * @param room
	 *            the room to add to the room list.
	 */
	public void addChatRoom(IChatRoom room) {
		myChatRooms.add(room);
	}
}

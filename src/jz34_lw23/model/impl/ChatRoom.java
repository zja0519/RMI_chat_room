package jz34_lw23.model.impl;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import provided.datapacket.ADataPacket;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IUser;

/**
 * The local implementation of an IChatRoom object. 
 * @author Jiao Zhang
 *
 */
public class ChatRoom implements IChatRoom {
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -6940496899514381206L;
	/**
	 * Name of the chat room
	 */
	private String name;
	/**
	 * List of user stubs in the chat room
	 */
	private List<IUser> users;
	
	/**
	 * Constructs a chat room
	 * @param name name of the chat room
	 */
	public ChatRoom(String name){
		this.name = name;
		users = new LinkedList<IUser>();
	}
	
	/**
	 * Returns the name of the chat room
	 * @return name of chat room
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns string representation of chat room
	 * @return name of chat room
	 */
	public String toString(){
		return getName();
	}

	@Override
	public void addLocalUser(IUser newUserStub) {
		users.add(newUserStub);
	}

	@Override
	public void removeLocalUser(IUser userStub) {
		users.remove(userStub);
	}

	@Override
	public Iterable<IUser> getUsers() {
		return users;
	}

	@Override
	public Iterable<ADataPacket> sendMessage(ADataPacket dp) {
		List<ADataPacket> result = new LinkedList<ADataPacket>();
		try {
			for (IUser user : users)
			{
				result.add(user.receiveData(dp));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
}

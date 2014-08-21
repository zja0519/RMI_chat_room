package jz34_lw23.model.impl;

import java.rmi.RemoteException;
import java.util.UUID;

import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacketAlgo;
import comp310f13.rmiChat.IUser;

/**
 * Local implementation of the IUser implementation
 * @author Jiao Zhang
 *
 */
public class User implements IUser {
	
	/**
	 * The name of the User
	 */
	private String name;
	/**
	 * Identifier of the user: the UUID
	 */
	private UUID uuid;
	/**
	 * a visitor so the user knows how to handle various types of messages.
	 */
	private DataPacketAlgo<ADataPacket, Void> algorithm;
	
	/**
	 * Construct a user object
	 * @param name the name of the user
	 * @param algorithm the visitor that user uses to handle various types of messages.
	 */
	public User(String name, DataPacketAlgo<ADataPacket, Void> algorithm)
	{
		this.name = name;
		this.algorithm = algorithm;
		uuid = UUID.randomUUID();
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public UUID getUUID() throws RemoteException {
		return uuid;
	}
	
	@Override
	public ADataPacket receiveData(ADataPacket dp) throws RemoteException {
		return dp.execute(algorithm);
	}
	
}

package jz34_lw23.model;

import java.rmi.RemoteException;
import java.awt.Component;
import java.rmi.server.UnicastRemoteObject;

import jz34_lw23.model.datapacket.cmd.CmdAddCmd;
import jz34_lw23.model.datapacket.cmd.CmdAddUser;
import jz34_lw23.model.datapacket.cmd.CmdDefault;
import jz34_lw23.model.datapacket.cmd.CmdRemoveUser;
import jz34_lw23.model.datapacket.cmd.CmdRequestCmd;
import jz34_lw23.model.datapacket.cmd.CmdStatusFail;
import jz34_lw23.model.datapacket.cmd.CmdStatusOk;
import jz34_lw23.model.datapacket.cmd.CmdTextMessage;
import jz34_lw23.model.impl.User;
import jz34_lw23.model.msg_type.AddUser;
import jz34_lw23.model.msg_type.RemoveUser;
import jz34_lw23.model.msg_type.TextMessage;
import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IAddCmd;
import comp310f13.rmiChat.IAddUser;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IRemoveUser;
import comp310f13.rmiChat.IRequestCmd;
import comp310f13.rmiChat.IStatusFail;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.ITextMessage;
import comp310f13.rmiChat.IUser;

/**
 * The model for ChatRooms. Every chatroom will have its own instance of this
 * class. This is part of the mini-MVC.
 * 
 * @author Jiao Zhang
 * 
 */
public class ChatRoomModel {
	/**
	 * The ChatRoom object
	 */
	private IChatRoom room;
	/**
	 * The View adaptor to the ChatRoomGUI
	 */
	private IMiniViewAdaptor adaptor;
	/**
	 * the IUser stub that represents myself in this room
	 */
	private IUser localuser;
	/**
	 * The visitor which handles DataPacket
	 */
	private DataPacketAlgo<ADataPacket, Void> algorithm;
	/**
	 * The command-to-miniModel adaptor.
	 */
	private ICmd2ModelAdapter cmdAdaptor;
	/**
	 * The reference to the main model that holds this mini-model so this
	 * ChatRoom model can talk back to the main model.
	 */
	private MainModel mainModel;

	/**
	 * Constructor of the ChatRoom model. Build the central visitor and the
	 * command-to-ChatRoomModel adaptor.
	 * 
	 * @param room
	 *            the ChatRoom object this model is for.
	 */
	public ChatRoomModel(IChatRoom room) {
		this.room = room;
		// instantiate cmdAdaptor
		cmdAdaptor = new ICmd2ModelAdapter() {

			@Override
			public IUser getLocalUserStub() {
				return localuser;
			}

			@Override
			public void append(String s) {
				adaptor.append(s + "\n");
			}

			@Override
			public void addComponent(String name, Component newComp) {
				adaptor.addComponent(name, newComp);
			}
		};

		algorithm = new DataPacketAlgo<ADataPacket, Void>(null);
		/**
		 * instantiate and install commands
		 */
		// default command
		CmdDefault cmdDefault = new CmdDefault(algorithm);
		cmdDefault.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setDefaultCmd(cmdDefault);
		// add command command
		CmdAddCmd cmdAddCmd = new CmdAddCmd(algorithm);
		cmdAddCmd.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setCmd(IAddCmd.class, cmdAddCmd);
		// add user command
		CmdAddUser cmdAddUser = new CmdAddUser(this, room);
		cmdAddUser.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setCmd(IAddUser.class, cmdAddUser);
		// remove user command
		CmdRemoveUser cmdRemoveUser = new CmdRemoveUser(this, room);
		cmdRemoveUser.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setCmd(IRemoveUser.class, cmdRemoveUser);
		// request command command
		CmdRequestCmd cmdRequestCmd = new CmdRequestCmd(algorithm);
		cmdRequestCmd.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setCmd(IRequestCmd.class, cmdRequestCmd);
		// status fail command
		CmdStatusFail cmdStatusFail = new CmdStatusFail();
		cmdStatusFail.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setCmd(IStatusFail.class, cmdStatusFail);
		// status ok command
		CmdStatusOk cmdStatusOk = new CmdStatusOk();
		cmdStatusOk.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setCmd(IStatusOk.class, cmdStatusOk);
		// text message command
		CmdTextMessage cmdTextMessage = new CmdTextMessage();
		cmdTextMessage.setCmd2ModelAdpt(cmdAdaptor);
		algorithm.setCmd(ITextMessage.class, cmdTextMessage);
	}

	/**
	 * Set the mini-view-adaptor of this ChatRoomModel
	 * 
	 * @param adaptor
	 *            the mini-view-adaptor
	 */
	public void setAdaptor(IMiniViewAdaptor adaptor) {
		this.adaptor = adaptor;
	}

	/**
	 * Set the local User: myself in this room
	 * 
	 * @param user
	 *            the local user stub.
	 */
	public void setLocalUser(IUser user) {
		this.localuser = user;
	}

	/**
	 * Get the chatroom of this mini-model
	 * 
	 * @return the chatRoom in this mini-model
	 */
	public IChatRoom getChatRoom() {
		return room;
	}

	/**
	 * To start the chatRoom model, make an IUser stub of myself and, send an
	 * IAddUser message to everyone in the room, and add myself to this room.
	 */
	public void start() {
		IUser localUserObject = new User(adaptor.getUserName(), algorithm);
		try {
			localuser = (IUser) UnicastRemoteObject.exportObject(
					localUserObject, IUser.CONNECTION_PORT);
			IAddUser addMeMsg = new AddUser(localuser);
			ADataPacket packet = new DataPacket<IAddUser>(IAddUser.class,
					localuser, addMeMsg);
			room.sendMessage(packet);
			room.addLocalUser(localuser);
			refreshUserList();
		} catch (Exception e) {
		}
	}

	/**
	 * Send a text message to everyone in the room.
	 * 
	 * @param text
	 *            the text to be sent.
	 */
	public void send(String text) {
		try {
			ITextMessage message = new TextMessage(localuser.getName(), text);
			ADataPacket packet = new DataPacket<ITextMessage>(
					ITextMessage.class, localuser, message);
			room.sendMessage(packet);
		} catch (RemoteException e) {
			adaptor.append("The following message: \""
					+ text
					+ "\" might be missing and didn't get through to everyone.\n");
			e.printStackTrace();
		}
	}

	/**
	 * When the user list changes in this room, refresh the user list in the
	 * mini-GUI.
	 */
	public void refreshUserList() {
		adaptor.refreshUserList(room.getUsers());
	}

	/**
	 * Stop the ChatRoom model when "I" leave. Inform everyone else that I will
	 * leave.
	 */
	public void stop() {
		room.removeLocalUser(localuser);
		IRemoveUser message = new RemoveUser(localuser);
		ADataPacket packet = new DataPacket<IRemoveUser>(IRemoveUser.class,
				localuser, message);
		room.sendMessage(packet);
		mainModel.removeRoom(room);
	}
	
	/**
	 * Set the reference to the Main Model that is holding this mini-model
	 * @param model the main model that is holding this mini-model
	 */
	public void setMainModel(MainModel model) {
		mainModel = model;
	}
}
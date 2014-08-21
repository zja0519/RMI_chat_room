package jz34_lw23.model.datapacket.cmd;

import java.rmi.RemoteException;

import jz34_lw23.model.ChatRoomModel;
import jz34_lw23.model.msg_type.StatusOk;
import comp310f13.rmiChat.IAddUser;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.IUser;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * A command to handle the packet where another user ask me to add an IUser
 * stub.
 * 
 * @author wlm
 * 
 */
public class CmdAddUser extends ADataPacketAlgoCmd<ADataPacket, IAddUser, Void> {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -8933527735983921340L;
	/**
	 * the command-to-miniModel adaptor
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * The chatRoom where a new user is added.
	 */
	private transient IChatRoom room;
	/**
	 * The model of the chatRoom where a new user is added.
	 */
	private transient ChatRoomModel model;

	/**
	 * Constructor of this command.
	 * 
	 * @param model
	 *            the model of the chatRoom where a new user is added
	 * @param room
	 *            the chatroom where a new user is added
	 */
	public CmdAddUser(ChatRoomModel model, IChatRoom room) {
		this.model = model;
		this.room = room;
	}

	@Override
	public ADataPacket apply(Class<?> index, DataPacket<IAddUser> host,
			Void... params) {
		IUser user = host.getData().getUser();
		room.addLocalUser(user);
		model.refreshUserList();
		try {
			cmd2ModelAdpt.append(user.getName() + " has joined the chatroom.");
		} catch (RemoteException e) {
			cmd2ModelAdpt
					.append("A user has joined the chatroom.");
			e.printStackTrace();
		}
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new StatusOk());
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}

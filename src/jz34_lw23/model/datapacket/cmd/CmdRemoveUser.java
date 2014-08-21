package jz34_lw23.model.datapacket.cmd;

import java.rmi.RemoteException;

import jz34_lw23.model.ChatRoomModel;
import jz34_lw23.model.msg_type.StatusOk;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IRemoveUser;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.IUser;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * Command to be used when a packet containing message asking me to remove a user.
 * @author wlm
 *
 */
public class CmdRemoveUser extends ADataPacketAlgoCmd<ADataPacket, IRemoveUser, Void> {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 668617040769836375L;
	/**
	 * The command-to-miniModel adaptor.
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * The chat room where a user will be removed
	 */
	private IChatRoom room;
	/**
	 * The model of the chat room where a user will be removed.
	 */
	private transient ChatRoomModel model;
	
	/**
	 * The constructor of the command to remove a user.
	 * @param model the model of the chat room where a user will be removed.
	 * @param room the chat room where a user will be removed.
	 */
	public CmdRemoveUser(ChatRoomModel model, IChatRoom room)
	{
		this.model = model;
		this.room = room;
	}

	@Override
	public ADataPacket apply(Class<?> index, DataPacket<IRemoveUser> host,
			Void... params) {
		IUser user = host.getData().getUser();
		room.removeLocalUser(user);
		model.refreshUserList();
		try {
			cmd2ModelAdpt.append(user.getName() + " has left the chatroom.");
		} catch (RemoteException e) {
			cmd2ModelAdpt.append("A user has left the chatroom.");
			e.printStackTrace();
		}
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new StatusOk());
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}

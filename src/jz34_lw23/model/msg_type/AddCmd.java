package jz34_lw23.model.msg_type;

import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import comp310f13.rmiChat.IAddCmd;

/**
 * Message type that ask a user to install a command.
 * @author Jiao Zhang
 *
 */
public class AddCmd implements IAddCmd {
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 4429320672696397720L;
	/**
	 * The type of data this command handles
	 */
	private Class<?> id;
	/**
	 * The actual command wrapped inside.
	 */
	private ADataPacketAlgoCmd<ADataPacket, ?, ?> cmd;

	/**
	 * Constructs an AddCmd message
	 * 
	 * @param id
	 *            the type of the data the command handles
	 * @param cmd
	 *            the actual command wrapped inside.
	 */
	public AddCmd(Class<?> id, ADataPacketAlgoCmd<ADataPacket, ?, ?> cmd) {
		this.id = id;
		this.cmd = cmd;
	}
	
	@Override
	public Class<?> getID() {
		return id;
	}

	@Override
	public ADataPacketAlgoCmd<ADataPacket, ?, ?> getNewCmd() {
		return cmd;
	}

}

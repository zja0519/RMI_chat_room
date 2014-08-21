package jz34_lw23.model.datapacket.cmd;

import jz34_lw23.model.msg_type.AddCmd;
import comp310f13.rmiChat.IAddCmd;
import comp310f13.rmiChat.IRequestCmd;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * The command to use when a message that requests a command is received.
 * 
 * @author wlm
 * 
 */
public class CmdRequestCmd extends
		ADataPacketAlgoCmd<ADataPacket, IRequestCmd, Void> {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -1126989366861935728L;
	/**
	 * The command-to-model adapter
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * The central visitor
	 */
	private transient DataPacketAlgo<ADataPacket, Void> algorithm;

	/**
	 * Constructs a command to handle message that requests a command.
	 * 
	 * @param algorithm
	 *            the central visitor.
	 */
	public CmdRequestCmd(DataPacketAlgo<ADataPacket, Void> algorithm) {
		this.algorithm = algorithm;
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Returns an IAddCmd package.
	 */
	public ADataPacket apply(Class<?> index, DataPacket<IRequestCmd> host,
			Void... params) {
		IRequestCmd reqcmd = host.getData();
		Class<?> idx = reqcmd.getID();
		return new DataPacket<IAddCmd>(IAddCmd.class,
				cmd2ModelAdpt.getLocalUserStub(), new AddCmd(idx,
						(ADataPacketAlgoCmd<ADataPacket, ?, ?>) algorithm
								.getCmd(idx)));
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}

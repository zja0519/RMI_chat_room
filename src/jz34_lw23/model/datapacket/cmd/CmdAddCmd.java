package jz34_lw23.model.datapacket.cmd;

import jz34_lw23.model.msg_type.StatusOk;
import comp310f13.rmiChat.IAddCmd;
import comp310f13.rmiChat.IStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * When another users sends to me a packet that tells me to install a command,
 * this command is used.
 * 
 * @author wlm
 * 
 */
public class CmdAddCmd extends ADataPacketAlgoCmd<ADataPacket, IAddCmd, Void> {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -730084975100685809L;
	/**
	 * The command-to-miniModel adaptor
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * A reference to the central visitor, when a new command will be installed.
	 */
	private transient DataPacketAlgo<ADataPacket, Void> algorithm;

	/**
	 * Constructor of this command
	 * @param algorithm the central visitor when a new command will be installed.
	 */
	public CmdAddCmd(DataPacketAlgo<ADataPacket, Void> algorithm) {
		this.algorithm = algorithm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ADataPacket apply(Class<?> index, DataPacket<IAddCmd> host,
			Void... params) {
		IAddCmd addcmd = host.getData();
		ADataPacketAlgoCmd<ADataPacket, ?, Void> newCmd = (ADataPacketAlgoCmd<ADataPacket, ?, Void>) addcmd
				.getNewCmd();
		// Attach to the local view.
		newCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		algorithm.setCmd(addcmd.getID(), newCmd);
		cmd2ModelAdpt.append("Installed command for " + index.getSimpleName()
				+ " message type!");
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new StatusOk());
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}

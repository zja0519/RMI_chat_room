package jz34_lw23.model.datapacket.cmd;

import comp310f13.rmiChat.IStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * The command to use when another user sends a packet to tell me a transmission
 * is OK. Do nothing.
 * 
 * @author wlm
 * 
 */
public class CmdStatusOk extends
		ADataPacketAlgoCmd<ADataPacket, IStatusOk, Void> {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -6942966705302166853L;
	/**
	 * The command-to-miniModel adaptor
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;

	@Override
	public ADataPacket apply(Class<?> index, DataPacket<IStatusOk> host,
			Void... params) {
		// Not really append anything
		cmd2ModelAdpt.append("");
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}

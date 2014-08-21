package jz34_lw23.model.datapacket.cmd;

import comp310f13.rmiChat.IStatusFail;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * A command to use when another user tells me a transmission is failed.
 * Display this fail-message.
 * 
 * @author wlm
 * 
 */
public class CmdStatusFail extends
		ADataPacketAlgoCmd<ADataPacket, IStatusFail, Void> {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -4698517875928945929L;
	/**
	 * the command-to-miniModel adaptor.
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;

	@Override
	public ADataPacket apply(Class<?> index, DataPacket<IStatusFail> host,
			Void... params) {
		cmd2ModelAdpt.append("Transmission FAILED: " + host.getData().getMsg());
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}

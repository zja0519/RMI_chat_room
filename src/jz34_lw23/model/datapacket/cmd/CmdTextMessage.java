package jz34_lw23.model.datapacket.cmd;

import java.util.Date;

import jz34_lw23.model.msg_type.StatusOk;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.ITextMessage;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * Command to use when a packet containing a piece of text message is received.
 * Append it to my miniView.
 * 
 * @author wlm
 * 
 */
public class CmdTextMessage extends
		ADataPacketAlgoCmd<ADataPacket, ITextMessage, Void> {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -5292893293525800136L;
	/**
	 * The command-to-miniModel adaptor.
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;

	@Override
	/**
	 * Display the sender, the time, and the message.
	 */
	public ADataPacket apply(Class<?> index, DataPacket<ITextMessage> host,
			Void... params) {
		String message = host.getData().getMsg();
		Date time = host.getData().getTime();
		String sender = host.getData().getName();
		cmd2ModelAdpt.append(sender + "(" + time.toString() + "):" + message);
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new StatusOk());
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}

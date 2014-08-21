package jz34_lw23.model.msg_type;

import provided.datapacket.ADataPacket;
import comp310f13.rmiChat.IStatusFail;

/**
 * The message type to tell another user a transimission fails.
 * 
 * @author Jiao Zhang
 * 
 */
public class StatusFail implements IStatusFail {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -8947335562832168981L;
	/**
	 * The message the sender sends with this Fail Status.
	 */
	private String message;
	/**
	 * The failed packet
	 */
	private ADataPacket packet;

	/**
	 * Constructs an IStatusFail message
	 * 
	 * @param message
	 *            a String that the user sends along with the Fail Status
	 * @param packet
	 *            the failed packet.
	 */
	public StatusFail(String message, ADataPacket packet) {
		this.message = message;
		this.packet = packet;
	}

	@Override
	public String getMsg() {
		return message;
	}

	@Override
	public ADataPacket getDataPacket() {
		return packet;
	}

}

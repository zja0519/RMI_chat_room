package jz34_lw23.model.msg_type;

import comp310f13.rmiChat.IRequestCmd;

/**
 * The message type to request a command from another user.
 * @author Jiao Zhang
 *
 */
public class RequestCmd implements IRequestCmd {
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 8336262855990797333L;
	/**
	 * The type of data that the sender is requesting a command to handle.
	 */
	private Class<?> id;

	/**
	 * Constructs an IRequestCmd message
	 * 
	 * @param id
	 *            the type of data that the sender is requesting a command to
	 *            handle.
	 */
	public RequestCmd(Class<?> id) {
		this.id = id;
	}

	@Override
	public Class<?> getID() {
		return id;
	}

}

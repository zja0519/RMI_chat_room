package jz34_lw23.model.msg_type;

import comp310f13.rmiChat.IAddUser;
import comp310f13.rmiChat.IUser;

/**
 * The message type to ask a user to add a user stub.
 * @author Jiao Zhang
 *
 */
public class AddUser implements IAddUser {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -4338549445628374864L;
	/**
	 * The IUser stub wrapped inside.
	 */
	private IUser user;

	/**
	 * Constructs an IAddUser message
	 * 
	 * @param user
	 *            the user stub that is wrapped inside.
	 */
	public AddUser(IUser user) {
		this.user = user;
	}

	@Override
	public IUser getUser() {
		return user;
	}

}

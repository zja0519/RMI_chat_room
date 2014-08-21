package jz34_lw23.model.msg_type;

import comp310f13.rmiChat.IRemoveUser;
import comp310f13.rmiChat.IUser;

/**
 * The message type to ask a user to remove a user stub.
 * 
 * @author Jiao Zhang
 * 
 */
public class RemoveUser implements IRemoveUser {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 7596083993432298894L;
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
	public RemoveUser(IUser user) {
		this.user = user;
	}

	@Override
	public IUser getUser() {
		return user;
	}

}

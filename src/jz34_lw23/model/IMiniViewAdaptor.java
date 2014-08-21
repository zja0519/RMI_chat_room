package jz34_lw23.model;

import java.awt.Component;

import comp310f13.rmiChat.IUser;

/**
 * The interface that defines the contract of the miniModel-to-miniView adaptor
 * 
 * @author Jiao Zhang
 * 
 */
public interface IMiniViewAdaptor {
	/**
	 * Append a text message somewhere on the miniView
	 * 
	 * @param message
	 *            the message to be displayed.
	 */
	public void append(String message);

	/**
	 * Add a component somewhere on the miniView
	 * 
	 * @param name
	 *            the name of the component to be added
	 * @param comp
	 *            the component to be added.
	 */
	public void addComponent(String name, Component comp);

	/**
	 * Get the name of the user.
	 * 
	 * @return the name of the user.
	 */
	public String getUserName();

	/**
	 * Refresh the user List on the miniView.
	 * 
	 * @param userList
	 *            the list of users.
	 */
	public void refreshUserList(Iterable<IUser> userList);
}

package jz34_lw23.model.msg_type;

import java.util.Date;

import comp310f13.rmiChat.ITextMessage;

/**
 * The message type that wraps a piece of text message.
 * 
 * @author Jiao Zhang
 * 
 */
public class TextMessage implements ITextMessage {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 4984403369997022531L;
	/**
	 * The name of the user who sends the text message.
	 */
	private String userName;
	/**
	 * The time the message was created.
	 */
	private Date time;
	/**
	 * The text message.
	 */
	private String message;

	/**
	 * Constructs and ITextMessage
	 * 
	 * @param userName
	 *            the name of the user who sends the text message
	 * @param message
	 *            the text message.
	 */
	public TextMessage(String userName, String message) {
		this.time = new Date();
		this.userName = userName;
		this.message = message;
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public Date getTime() {
		return time;
	}

	@Override
	public String getMsg() {
		return message;
	}

}

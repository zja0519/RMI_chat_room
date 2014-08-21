package jz34_lw23.view;

/**
 * Interface that defines the contract of the miniView-to-miniModel adaptor
 * 
 * @author Jiao Zhang
 * 
 */
public interface IMiniModelAdaptor {

	/**
	 * Sends out a text message to others.
	 * 
	 * @param text
	 */
	public void send(String text);

	/**
	 * get the name of the chat room
	 * 
	 * @return
	 */
	public String getRoomName();

	/**
	 * Stop the chatroom model.
	 */
	public void stop();
}

package jz34_lw23.controller;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JOptionPane;

import jz34_lw23.model.ChatRoomModel;
import jz34_lw23.model.IMiniViewAdaptor;
import jz34_lw23.model.IViewAdaptor;
import jz34_lw23.model.MainModel;
import jz34_lw23.view.ChatRoomGUI;
import jz34_lw23.view.IMiniModelAdaptor;
import jz34_lw23.view.IModelAdaptor;
import jz34_lw23.view.MainGUI;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;
import comp310f13.rmiChat.IUser;

/**
 * The controller for the main model, where everything starts
 * 
 * @author Jiao Zhang
 * 
 */
public class MainController {
	/**
	 * The main model to back up the application
	 */
	private MainModel model;
	/**
	 * The main view.
	 */
	private MainGUI view;

	/**
	 * Constructor for the main controller. Instantiate the adapters between
	 * main model and main view.
	 */
	public MainController() {
		view = new MainGUI(new IModelAdaptor() {

			@Override
			public void createRoom(String roomName) {
				model.createRoom(roomName);
			}

			@Override
			public void login(String name) {
				model.setName(name);
			}

			@Override
			public void invite(IChatRoom room, IHost remoteHost) {
				model.invite(room, remoteHost);
			}

			@Override
			public void connect(String remoteIP) {
				model.connect(remoteIP);
			}

			@Override
			public void stop() {
				model.stop();
			}
		});

		model = new MainModel(new IViewAdaptor() {

			@Override
			public IMiniViewAdaptor createMiniViewAdaptor(
					final ChatRoomModel roomModel) {
				final ChatRoomGUI roomGUI = view
						.addChatRoomGUI(new IMiniModelAdaptor() {

							@Override
							public void send(String text) {
								roomModel.send(text);
							}

							@Override
							public String getRoomName() {
								return roomModel.getChatRoom().getName();
							}

							@Override
							public void stop() {
								roomModel.stop();
							}

						});
				return new IMiniViewAdaptor() {

					@Override
					public void append(String message) {
						roomGUI.append(message);
					}

					@Override
					public void addComponent(String name, Component comp) {
						roomGUI.addComponent(name, comp);
					}

					@Override
					public String getUserName() {
						return model.getName();
					}

					@Override
					public void refreshUserList(Iterable<IUser> users) {
						roomGUI.refreshUserList(users);
					}
				};
			}

			@Override
			public void append(String str) {
				view.appendSystemMessage(str);
			}

			@Override
			public boolean handleInvite(String chatroomInfo) {
				Component parent = view.getParent();
				int acceptResult = JOptionPane.showConfirmDialog(parent,
						"Accept this invitation? " + chatroomInfo,
						"Please handle the invitation",
						JOptionPane.YES_NO_OPTION);
				return acceptResult == JOptionPane.YES_OPTION;
			}

			@Override
			public void refreshHostList(List<IHost> hosts) {
				view.refreshHostList(hosts);
			}

			@Override
			public void refreshRoomList(List<IChatRoom> rooms) {
				view.refreshRoomList(rooms);
			}

		});
	}

	/**
	 * Start the main model and the main view.
	 */
	public void start() {
		view.start();
		model.start();
	}

	/**
	 * main method of the controller. The entry point of the application.
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				new MainController().start();
			}
			
		});
	}
}

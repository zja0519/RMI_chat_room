package jz34_lw23.model.datapacket.cmd;

import java.rmi.RemoteException;

import jz34_lw23.model.msg_type.RequestCmd;
import jz34_lw23.model.msg_type.StatusFail;
import comp310f13.rmiChat.IRequestCmd;
import comp310f13.rmiChat.IStatusFail;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.ICmd2ModelAdapter;

/**
 * The default command, to be used when a packet containing unknown-type message
 * is received.
 * 
 * @author wlm
 * 
 */
public class CmdDefault extends ADataPacketAlgoCmd<ADataPacket, Object, Void> {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -6942966705302166853L;
	/**
	 * The command-to-model adaptor
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * The reference to the central visitor.
	 */
	private transient DataPacketAlgo<ADataPacket, Void> algorithm;

	/**
	 * The constructor of the default command
	 * 
	 * @param algorithm
	 *            the central visitor.
	 */
	public CmdDefault(DataPacketAlgo<ADataPacket, Void> algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public ADataPacket apply(Class<?> index, DataPacket<Object> host,
			Void... params) {
		// do not process null commands
		if (host.getData() != null) {
			try {
				// request command
				ADataPacket addcmdpkt = host.getSender().receiveData(
						new DataPacket<IRequestCmd>(IRequestCmd.class,
								cmd2ModelAdpt.getLocalUserStub(),
								new RequestCmd(index)));
				// process returned data packet in case it contains the add
				// command
				addcmdpkt.execute(algorithm, params);
			} catch (RemoteException e) {
				e.printStackTrace();
				return new DataPacket<IStatusFail>(IStatusFail.class,
						cmd2ModelAdpt.getLocalUserStub(), new StatusFail(
								"Failed to send IRequestCmd message", host));
			}
		}
		// installation is complete, now execute the original data
		// packet
		// with the new command
		return host.execute(algorithm, params);
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}

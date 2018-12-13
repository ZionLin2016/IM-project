package cn.bio.server.chat;

import java.net.Socket;
import java.util.ArrayList;

import cn.bio.server.model.Membership;
import cn.bio.server.parser.BasicPacketParser;
import cn.bio.server.protocol.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHub {

	private static final Logger logger = LoggerFactory.getLogger(ClientHub.class);
	private static String TAG = "ClientHub";

	private static ClientHub instance = null;

	synchronized public static ClientHub getInstance() {
		if (instance == null) {
			instance = new ClientHub();
		}

		return instance;
	}

	public Membership membership = new Membership();

	public void onGuideBegin(Client guide) {
		membership.guideCreateOrRejoinGroup(guide);
	}
	

	public void closeGroup(Integer groupID) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Client> onGuideList() {
		return membership.getAllGuide();
	}

	// 添加发送信息发送给別的客户端
	public void sendMsgToGroupExceptSelf(Client sender, BasicPacketParser data) {

		membership.printMappingGuide2Tst();

		for (Client each : membership.getAllInSameGroup(sender)) {
			logger.debug(TAG, "all member in same group = " + each.getUserName() + " id=" + each.getClientID() + " ip="
					+ each.getUserIP() + " port = " + each.getUserPort());
			if (!each.equals(sender)) {
				sendMsgToClient(each, data);
			}
		}
	}

	public void sendMsgToClient(Client sender, BasicPacketParser data) {
		if (sender == null) {
			return;
		}

		try {
			logger.debug(TAG,
					"Send data to Client " + sender.getUserName() + " its queue size=" + sender.getSizeOfQueueToSend());
			sender.sendMessage(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClientDisconnect(Client client) {
		membership.removeClient(client);
	}

	public void onClientConnect(Client client, Socket socket) {
		// if it is a guide, try to join the same group as it exited last time
		client.onConnect(socket);
	}

	public boolean tryLoginUniquely(Client client) {
		return membership.addClient(client);
	}

	public void onClientDisconnected(Client client) {
		membership.removeClient(client);

	}

	public void disconnectClient(Client client) {
		client.stop();
	}

	public void disconnectAllClient() {
		for (Client each : membership.getAllClient()) {
			each.stop();
		}

		membership.dismissAndClearAllRelationship();
	}

}
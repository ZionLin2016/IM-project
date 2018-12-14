package cn.bio.common.chat;

import java.net.Socket;
import java.util.ArrayList;

import cn.bio.common.model.GroupModel;
import cn.bio.common.model.Membership;
import cn.bio.common.parser.BasicPacketParser;
import cn.bio.common.protocol.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHub {

	private static final Logger logger = LoggerFactory.getLogger(ClientHub.class);

	private static ClientHub instance = null;

	synchronized public static ClientHub getInstance() {
		if (instance == null) {
			instance = new ClientHub();
		}

		return instance;
	}

	public Membership membership = new Membership();

	public void onLeaderBegin(Client leader) {
		membership.leaderCreateOrRejoinGroup(leader);
	}
	

	public void closeGroup(Integer groupID) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Client> onLeaderList() {
		return membership.getAllLeaders();
	}



	public  boolean onSelectLeader(Client sender, int leaderId) {

		System.out.println("Before processing [selectGuide] : ");
		membership.printMappingLeader2Members();

		/*
		 * the Guide ID may become invalid because the guide has exited
		 */
		GroupModel group = membership.getGroupByID(leaderId);
		if (group == null)
		{
			return false;
		}

		membership.joinGroup(sender, group);

		System.out.println("After processing [selectGuide] : ");
		membership.printMappingLeader2Members();

		return true;
	}

	/**
	 * 添加发送信息发送给別的客户端
	 *
	 * @param sender
	 * @param data
	 */
	public void sendMsgToGroupExceptSelf(Client sender, BasicPacketParser data) {

		membership.printMappingLeader2Members();

		for (Client each : membership.getAllInSameGroup(sender)) {
			logger.debug( "all member in same group = " + each.getUserName() + " id=" + each.getClientID() + " ip="
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
			logger.debug("Send data to Client " + sender.getUserName() + " its queue size=" + sender.getSizeOfQueueToSend());
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
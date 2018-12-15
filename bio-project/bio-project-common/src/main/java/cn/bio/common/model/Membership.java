package cn.bio.common.model;


import cn.bio.common.client.Client;

import java.util.ArrayList;
import java.util.Iterator;


public class Membership extends BasicMembership {

	synchronized public boolean isClientOnline(Client client) {
		return allClient.contains(client);
	}

	synchronized public boolean isLeader(Client client) {
		if (client == null) {
			return false;
		}

		return allGroup.contains(client.getClientID());
	}

	synchronized public Client getLeaderInGroup(GroupModel group) {
		if (group == null) {
			return null;
		}

		Integer groupID = group.getGroupId();
		Integer guideID = groupID; // for this project, they are the same
		Client guide = mapID2Client.get(guideID);
		return guide;
	}

	synchronized public ArrayList<Client> getAllMembersInGroup(GroupModel group) {
		ArrayList<Client> re = new ArrayList<Client>();

		Client[] members = getGroupMembers(group);
		Client leader = getLeaderInGroup(group);
		if (leader == null || members.length == 0) {
			return re;
		}

		for (Client member : members) {
			if (!member.equals(leader)) {
				re.add(member);
			}
		}

		return re;

	}

	synchronized public ArrayList<Client> getAllLeaders() {

		ArrayList<Client> re = new ArrayList<Client>();

		Iterator<GroupModel> it = allGroup.iterator();
		while (it.hasNext()) {
			GroupModel group = it.next();
			Client guide = getLeaderInGroup(group);
			if (guide == null) {
				System.out.println("Found Group (" + group.getGroupId()
						+ ") where the Guide is offline");
			} else {
				System.out.println("Found Group (" + group.getGroupId()
						+ ") where the Guide (" + guide.getClientID()
						+ ") is online");
				re.add(guide);
			}

		}

		return re;
	}

	synchronized public void leaderCreateOrRejoinGroup(Client leader) {
		GroupModel group = getGroupByID(leader.getClientID());

		// if the guide has NOT yet in a group,
		if (group == null) {

			System.out.println(" the guide has NOT yet in a group");
			group = new GroupModel();
			System.out.println("guideCreateOrRejoinGroup guide.ID="
					+ leader.getClientID());
			group.setGroupId(leader.getClientID());

			addGroup(group);
		} else {
			System.out.println(" the guide has already in a group");
		}

		joinGroup(leader, group);

		printMappingLeader2Members();

	}

	/*
	 * below are some advanced methods:
	 */
	public void printMappingLeader2Members() {
		StringBuilder sb = new StringBuilder();
		sb.append("MappingGuide2Tst: ");
		for (GroupModel group : getAllGroup()) {
			Client guide = this.getLeaderInGroup(group);

			if (guide == null) {
				continue;
			}

			sb.append("[ Guide " + guide.getClientID() + " and ");

			for (Client member : getAllMembersInGroup(group)) {
				sb.append("Tst " + member.getClientID() + ", ");
			}

			sb.append("]; ");
		}

		System.out.println(sb.toString());
	}

}

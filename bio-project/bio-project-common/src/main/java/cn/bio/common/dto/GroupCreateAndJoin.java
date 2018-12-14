package cn.bio.common.dto;


public class GroupCreateAndJoin {

	/**
	 * 群主id
	 */
	private int leaderId;

	/**
	 * 群id
	 */
	private int groupId;

	/**
	 * 预留信息
	 */
	private String reserved;

	public int getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(int leaderId) {
		this.leaderId = leaderId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return "GroupCreateAndJoin{" +
				"leaderId=" + leaderId +
				", groupId=" + groupId +
				", reserved='" + reserved + '\'' +
				'}';
	}
}

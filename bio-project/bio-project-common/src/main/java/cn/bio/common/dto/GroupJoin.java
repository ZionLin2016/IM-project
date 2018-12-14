package cn.bio.common.dto;


public class GroupJoin {

	/**
	 * 用户id
	 */
	private int userId;

	/**
	 * 选择的群主id
	 */
	private int leaderId;

	/**
	 * 群id
	 */
	private int groupId;

	/**
	 * 状态信息
	 */
	private int status;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GroupJoin{" +
				"userId=" + userId +
				", leaderId=" + leaderId +
				", groupId=" + groupId +
				", status=" + status +
				'}';
	}
}

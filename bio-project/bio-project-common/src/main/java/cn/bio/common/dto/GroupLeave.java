package cn.bio.common.dto;

public class GroupLeave {

	private int leaderId;

	private int guideId;

	private boolean status;

	public int getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(int leaderId) {
		this.leaderId = leaderId;
	}

	public int getGuideId() {
		return guideId;
	}

	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GroupLeave{" +
				"leaderId=" + leaderId +
				", guideId=" + guideId +
				", status=" + status +
				'}';
	}
}

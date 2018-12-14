package cn.bio.common.dto;


public class GroupDismiss {

	private int leaderId;

	private boolean status;

	public int getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(int leaderId) {
		this.leaderId = leaderId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GroupDismiss{" +
				"leaderId=" + leaderId +
				", status=" + status +
				'}';
	}
}

package cn.bio.server.bean;

public class GroupDismiss {
	private int guideid;

	private boolean status;

	public int getGuideid() {
		return guideid;
	}

	public void setGuideid(int guideid) {
		this.guideid = guideid;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "StopGuide [guideid=" + guideid + ", status=" + status + "]";
	}

}

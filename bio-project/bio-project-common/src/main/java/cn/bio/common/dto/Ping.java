package cn.bio.common.dto;

public class Ping {

	private int pingId;
	private String reserved;

	public int getPingId() {
		return pingId;
	}

	public void setPingId(int pingId) {
		this.pingId = pingId;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return "Ping{" +
				"pingId=" + pingId +
				", reserved='" + reserved + '\'' +
				'}';
	}
}

package cn.bio.server.dto;

import java.io.Serializable;

public class Ping implements Serializable {
	private static final long serialVersionUID = 1759840235030337397L;

	private int pingid;
	private String reserved;

	public int getPingid() {
		return pingid;
	}

	public void setPingid(int pingid) {
		this.pingid = pingid;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return "Ping [pingid=" + pingid + ", reserved=" + reserved + "]";
	}

}

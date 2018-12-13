package cn.bio.server.dto;

import java.io.Serializable;

public class GroupCreateAndJoin implements Serializable {
	private static final long serialVersionUID = -5254747568099938871L;

	/**
	 * 导游id
	 */
	private int userid;

	/**
	 * 群id
	 */
	private int groupid;

	/**
	 * 预留信息
	 */
	private String reserved;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return "GuideBegin [userid=" + userid + ", groupid=" + groupid + ", reserved=" + reserved + "]";
	}

}

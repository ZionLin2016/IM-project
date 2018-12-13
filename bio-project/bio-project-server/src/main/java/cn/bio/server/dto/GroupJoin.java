package cn.bio.server.dto;

import java.io.Serializable;

public class GroupJoin implements Serializable {
	private static final long serialVersionUID = -4611208767850716612L;

	/**
	 * 用户id
	 */
	private int userid;  

	/**
	 * 选择的导游id
	 */
	private int guideid;  

	/**
	 * 群id
	 */
	private int groupid;  

	/**
	 * 状态信息
	 */
	private int status;  

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getGuideid() {
		return guideid;
	}

	public void setGuideid(int guideid) {
		this.guideid = guideid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GroupJoin [userid=" + userid + ", guideid=" + guideid + ", groupid=" + groupid + ", status=" + status
				+ "]";
	}

}

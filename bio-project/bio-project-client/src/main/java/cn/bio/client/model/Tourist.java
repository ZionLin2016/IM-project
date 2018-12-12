package cn.bio.client.model;

import java.io.Serializable;

public class Tourist implements Serializable {

	private static final long serialVersionUID = 6600705296305500480L;

	private int touristId;
	private String loginBeginTime;
	private String loginOutTime;

	public int getTouristId() {
		return touristId;
	}

	public void setTouristId(int touristId) {
		this.touristId = touristId;
	}

	public String getLoginBeginTime() {
		return loginBeginTime;
	}

	public void setLoginBeginTime(String loginBeginTime) {
		this.loginBeginTime = loginBeginTime;
	}

	public String getLoginOutTime() {
		return loginOutTime;
	}

	public void setLoginOutTime(String loginOutTime) {
		this.loginOutTime = loginOutTime;
	}
	
	//等待微信数据
	
}

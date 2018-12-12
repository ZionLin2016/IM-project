package cn.bio.client.model;

import java.io.Serializable;

public class Guide implements Serializable{

	private static final long serialVersionUID = -3803685686085304236L;
	/**
	 * 导游ID
	 */
	private int guideId;

	/**
	 * 导游名字
	 */
	private String guideName;

	/**
	 * 导游头像链接
	 */
	private String guideIcon;
	
	public int getGuideId() {
		return guideId;
	}
	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}
	public String getGuideName() {
		return guideName;
	}
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	public String getGuideIcon() {
		return guideIcon;
	}
	public void setGuideIcon(String guideIcon) {
		this.guideIcon = guideIcon;
	}
	
	
}

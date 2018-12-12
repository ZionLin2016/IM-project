package cn.bio.client.model;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable{

	private static final long serialVersionUID = 5347963886638800709L;

	/**
	 * 群ID
	 */
	private int groupId;

	/**
	 * 当前人数
	 */
	private int groupNumByCurrent;

	/**
	 * 导游id
	 */
	private int guideId;

	private List<Tourist> touristList;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getGroupNumByCurrent() {
		return groupNumByCurrent;
	}
	public void setGroupNumByCurrent(int groupNumByCurrent) {
		this.groupNumByCurrent = groupNumByCurrent;
	}
	
	public int getGuideId() {
		return guideId;
	}
	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}
	public List<Tourist> getTouristList() {
		return touristList;
	}
	public void setTouristList(List<Tourist> touristList) {
		this.touristList = touristList;
	}

	@Override
	public String toString() {
		return "Group{" +
				"groupId=" + groupId +
				", groupNumByCurrent=" + groupNumByCurrent +
				", guideId=" + guideId +
				", touristList=" + touristList +
				'}';
	}
}

package cn.bio.common.model;

import java.io.Serializable;

public class GroupModel implements Serializable, Comparable {

	private static final long serialVersionUID = -5869869783247593233L;

	private int groupId;// 群ID
	  
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Override
	public int compareTo(Object arg0) {
		return this.groupId - ((GroupModel)arg0).groupId;
	}

}

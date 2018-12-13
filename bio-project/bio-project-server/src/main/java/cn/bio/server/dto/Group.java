package cn.bio.server.dto;

import java.io.Serializable;

public class Group implements Serializable {
	private static final long serialVersionUID = 3590723443914866448L;

	private String name;
	private int groupid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", groupid=" + groupid +
                '}';
    }
}
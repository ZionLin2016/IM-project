package cn.bio.common.dto;

import java.util.List;

public class GroupListAck {

	private List<Group> content;

	public List<Group> getContent() {
		return content;
	}

	public void setContent(List<Group> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "GroupListAck{" +
				"content=" + content +
				'}';
	}
}

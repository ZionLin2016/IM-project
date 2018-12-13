package cn.bio.server.dto;

import java.io.Serializable;
import java.util.List;

public class GroupListAck implements Serializable {

	private static final long serialVersionUID = 7694159820586459113L;

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

package cn.bio.server.dto;

import java.io.Serializable;

public class Data implements Serializable {
	private static final long serialVersionUID = 1854155988149764620L;
	/**
	 * 0文本，1图片链接，2视频链接， 3命令
	 */
	private int type;

	private int pattion;

	/**
	 * 内容
	 */
	private String data;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPattion() {
		return pattion;
	}

	public void setPattion(int pattion) {
		this.pattion = pattion;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Data [pattion=" + type + ", data=" + data + "]";
	}

}

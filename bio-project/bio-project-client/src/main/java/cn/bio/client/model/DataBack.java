package cn.bio.client.model;

public class DataBack {

	/**
	 * 包头
	 */
	private int head;

	/**
	 * 版本号
	 */
	private int version;  

	/**
	 * 协议类型
	 */
	private int protocolNum;  

	/**
	 * 数据类型 0：为二进制包， 1为json包
	 */
	private int dtype;  

	/**
	 * 0文本，1语音，2视频， 3实时视频
	 */
	private int pattion;  

	/**
	 * 内容
	 */
	private String data;  

	public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getProtocolNum() {
		return protocolNum;
	}

	public void setProtocolNum(int protocolNum) {
		this.protocolNum = protocolNum;
	}

	public int getPattion() {
		return pattion;
	}

	public void setPattion(int pattion) {
		this.pattion = pattion;
	}

	public int getDtype() {
		return dtype;
	}

	public void setDtype(int dtype) {
		this.dtype = dtype;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DataBack [head=" + head + ", version=" + version + ", protocolNum=" + protocolNum + ", pattion="
				+ pattion + ", dtype=" + dtype + ", data=" + data + "]";
	}

}

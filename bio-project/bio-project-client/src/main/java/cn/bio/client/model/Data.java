package cn.bio.client.model;

public class Data {

	/**
	 * 包头
	 */
	private int head;

	/**
	 * 版本号
	 */
	private int version;

	/**
	 * 协议号
	 */
	private int protocolNum;

	/**
	 * 数据类型 1json 2字节流
	 */
	private int dtype;

	/**
	 * 标识id
	 */
	private int proid;

	/**
	 * 用户id
	 */
	private int userid;

	/**
	 * 选择的导游id
	 */
	private int guideid;

	/**
	 * 0文本，1图片，2音频，3视频
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

	public int getDtype() {
		return dtype;
	}

	public void setDtype(int dtype) {
		this.dtype = dtype;
	}

	public int getProid() {
		return proid;
	}

	public void setProid(int proid) {
		this.proid = proid;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Data [head=" + head + ", version=" + version + ", protocolNum=" + protocolNum + ", dtype=" + dtype
				+ ", proid=" + proid + ", userid=" + userid + ", guideid=" + guideid + ", pattion=" + pattion
				+ ", data=" + data + "]";
	}

}

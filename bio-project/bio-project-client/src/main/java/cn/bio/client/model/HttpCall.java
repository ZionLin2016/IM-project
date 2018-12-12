package cn.bio.client.model;

/**
 * @author admin
 *
 */
public class HttpCall {
	private int head;
	private int version;
	private int type;
	private int dtype;
	private int proid;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	@Override
	public String toString() {
		return "HttpCall [head=" + head + ", version=" + version + ", type=" + type + ", dtype=" + dtype + ", proid="
				+ proid + "]";
	}

}

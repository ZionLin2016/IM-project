package cn.bio.server.dto;

import java.io.Serializable;

public class GroupListReq implements Serializable {
    private static final long serialVersionUID = -8874454780523045927L;

    /**
	 *  预留信息
	 */
	private String reserved;

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return "AskGuideList [reserved=" + reserved + "]";
	}

}

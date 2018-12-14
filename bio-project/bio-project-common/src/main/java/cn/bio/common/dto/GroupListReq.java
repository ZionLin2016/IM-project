package cn.bio.common.dto;

public class GroupListReq {

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

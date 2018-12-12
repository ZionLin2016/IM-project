package cn.bio.server.beanparser;

public class BeanPacket {

	public int headId;
	public int size;
	public int protocolNum;
	public byte[] data = null;

	public String turnToContent;
}

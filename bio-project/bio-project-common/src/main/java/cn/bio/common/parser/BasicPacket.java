package cn.bio.common.parser;

public class BasicPacket {

	public int headId;
	public int size;
	public int protocolNum;
	public byte[] data = null;

	public String turnToContent;
}

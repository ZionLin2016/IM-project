package cn.bio.client.protocol;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;

/**
 * 整个包的组成： length 记录包的长度(4个字节：32位) head 包头标识(4个字节：32位) version
 * 协议的版本(1个字节：8位) type 协议的类型(1个字节：8位) ---------------------------以上这些不用设置
 * pattion 业务类型(1个字节：8位) dtype 数据格式(1个字节：8位) utype 角色类型(1个字节：8位) d ata 正文数据
 */
public class DataProtocol extends BasicProtocol implements Serializable {

	public static final int PROTOCOL_TYPE = 0;

	private static final int PATTION_LEN = 1;
	private static final int DTYPE_LEN = 1;
	private static final int UTYPE_LEN = 1;
	private static final int USERID_LEN = 1;

	/**
	 * 0表示文字，1表示语音，2表示视频，3表示实时视频
	 */
	private int pattion;

	/**
	 * 数据格式：0表示二进制流，1表示Json
	 */
	private int dtype;

	/**
	 * 角色类型：0表示导游，1表示游客
	 */
	private int utype;

	/**
	 * 用户id用于辨认具体客户端
	 */
	private int userid;

	/**
	 * 正文数据
	 */
	private String data;

	@Override
	public int getLength() {
		return super.getLength() + PATTION_LEN + DTYPE_LEN + UTYPE_LEN + USERID_LEN + data.getBytes().length;
	}

	@Override
	public int getProtocolType() {
		return PROTOCOL_TYPE;
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

	public int getUtype() {
		return utype;
	}

	public void setUtype(int utype) {
		this.utype = utype;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 拼接发送数据
	 * 
	 * @return
	 */
	@Override
	public byte[] genContentData() {
		byte[] base = super.genContentData();
		byte pattion = (byte) this.pattion;
		byte dtype = (byte) this.dtype;
		byte utype = (byte) this.utype;
		byte userid = (byte) this.userid;
		byte[] data = this.data.getBytes();

		ByteArrayOutputStream baos = new ByteArrayOutputStream(getLength());
		// 基础包头
		baos.write(base, 0, base.length);
		// 业务类型
		baos.write(pattion);
		// 业务数据格式
		baos.write(dtype);
		// 角色
		baos.write(utype);
		// 用户id
		baos.write(userid);
		// 业务数据
		baos.write(data, 0, data.length);
		return baos.toByteArray();
	}

	/**
	 * 解析接收数据，按顺序解析
	 * 
	 * @param data
	 * @return
	 * @throws ProtocolException
	 */
	@Override
	public int parseContentData(byte[] data) throws ProtocolException {
		int pos = super.parseContentData(data);

		// 解析pattion
		pattion = data[pos] & 0xFF;
		pos += PATTION_LEN;

		// 解析dtype
		dtype = data[pos] & 0xFF;
		pos += DTYPE_LEN;

		// 解析utype
		// utype = SocketUtil.byteArrayToInt(data, pos, UTYPE_LEN);
		dtype = data[pos] & 0xFF;
		pos += UTYPE_LEN;

		// 解析userid
		dtype = data[pos] & 0xFF;
		pos += USERID_LEN;

		// 解析data
		try {
			this.data = new String(data, pos, data.length - pos, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return pos;
	}

	@Override
	public String toString() {
		return "data: " + data;
	}
}
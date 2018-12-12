package cn.bio.client.protocol;

import cn.bio.client.util.Config;
import cn.bio.client.util.SocketUtil;

import java.io.ByteArrayOutputStream;
import java.net.ProtocolException;


/**
 * 协议类型: 0表示数据，1表示数据Ack，2表示ping，3表示pingAck
 * 
 * 长度均以字节（byte）为单位，int有4个字节
 */
public abstract class BasicProtocol {

	/**
	 * 整个基础包头的总长度4字节
	 */
	public static final int LENGTH_LEN = 4;

	/**
	 * 包头标识长度
	 */
	protected static final int HEADID_LEN = 4;

	/**
	 * 协议的版本长度（其中前3位作为预留位，后5位作为版本号）
	 */
	protected static final int VER_LEN = 1;

	/**
	 * 协议的数据类型长度
	 */
	protected static final int TYPE_LEN = 1;

	private static final int HEAD = 66666;

	/**
	 * 版本号
	 */
	private int version = Config.VERSION;

	/**
	 * 获取整条数据长度 单位：字节（byte）
	 */
	protected int getLength() {
		return LENGTH_LEN + HEADID_LEN + VER_LEN + TYPE_LEN;

	}

	public int getHead() {
		return HEAD;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * 获取协议类型，由子类实现
	 */
	public abstract int getProtocolType();

	/**
	 * 拼接发送数据，此处拼接了协议版本、协议类型和数据长度，具体内容子类中再拼接 按顺序拼接
	 */
	public byte[] genContentData() {
		byte[] length = SocketUtil.int2ByteArrays(getLength());
		byte[] head = SocketUtil.int2ByteArrays(getHead());
		byte[] version = { (byte) this.version };
		byte[] type = { (byte) getProtocolType() };

		ByteArrayOutputStream baos = new ByteArrayOutputStream(LENGTH_LEN + HEADID_LEN + VER_LEN + TYPE_LEN);
		baos.write(length, 0, LENGTH_LEN);
		baos.write(head, 0, HEADID_LEN);
		baos.write(version, 0, VER_LEN);
		baos.write(type, 0, TYPE_LEN);
		return baos.toByteArray();
	}

	/**
	 * 解析出整条数据长度
	 */
	protected int parseLength(byte[] data) {
		return SocketUtil.byteArrayToInt(data, 0, LENGTH_LEN);
	}

	// /**
	// * 解析出版本号
	// */
	// protected int parseVersion(byte[] data) {
	// byte v = data[LENGTH_LEN]; // 与预留位组成一个字节
	// return ((v << 3) & 0xFF) >> 3;
	// }
	//
	// /**
	// * 解析出协议类型
	// */
	// public static int parseType(byte[] data) {
	// byte t = data[LENGTH_LEN + VER_LEN];//
	// 前4个字节（0，1，2，3）为数据长度的int值，以及ver占一个字节
	// return t & 0xFF;
	// }

	/**
	 * 解析接收数据，此处解析了协议版本、协议类型和数据长度，具体内容子类中再解析 协议版本不一致，抛出异常
	 */
	public int parseContentData(byte[] data) throws ProtocolException {
		int head = SocketUtil.byteArrayToInt(data, 4, HEADID_LEN);
		int version = SocketUtil.byteArrayToInt(data, 8, VER_LEN);
		int protocolType = SocketUtil.byteArrayToInt(data, 9, TYPE_LEN);
		System.out.println("Head: " + head + ", Version: " + version + ", ProtocolType: " + protocolType);
//		if (version != getVersion()) {
//			throw new ProtocolException("input version is error: " + version);
//		}
		return LENGTH_LEN + HEADID_LEN + VER_LEN + TYPE_LEN;
	}

	@Override
	public String toString() {
		return "Version: " + getVersion() + ", Type: " + getProtocolType();
	}
}
package cn.bio.common.parser;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StreamPacketParser extends BasicPacketParser implements Serializable {

	private static final long serialVersionUID = 4078504671163645451L;

	/**
	 * 音频数据
	 */
	private byte[] audioData;

	@Override
	public int getLength() {
		return HEADID_LEN + PACKEAGE_LEN + PROTOCOL_LEN + audioData.length;
	}

	public byte[] getAudioData() {
		return audioData;
	}

	public void setAudioData(byte[] audioData) {
		this.audioData = audioData;
	}

	/**
	 * 拼接发送数据
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public byte[] sendAllData() throws Exception {
		byte[] base = super.sendAllData();
		byte[] data = this.getAudioData();

		ByteArrayOutputStream baos = new ByteArrayOutputStream(getLength());
		baos.write(base, 0, super.getLength());
		baos.write(data, 0, data.length);
		byte[] send = baos.toByteArray();

		return send;
	}

	/**
	 * 解析接收数据
	 *
	 * @param data
	 * @return
	 */
	@Override
	public List<Integer> parseData(byte[] data) {

		ArrayList<Integer> list = (ArrayList<Integer>) super.parseData(data);
		return list;
	}

	@Override
	public String toString() {
		return "data:" + audioData;
	}

}

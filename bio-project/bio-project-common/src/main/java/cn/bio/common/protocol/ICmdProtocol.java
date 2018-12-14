package cn.bio.common.protocol;


import cn.bio.common.parser.BasicPacket;

public interface ICmdProtocol {

	// 返回命令是否已成功处理
	boolean onCmd(BasicPacket cp);

	Client getClient();

	void onDisconnected();

}

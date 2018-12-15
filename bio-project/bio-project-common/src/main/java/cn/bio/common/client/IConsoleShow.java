package cn.bio.common.client;


import cn.bio.common.parser.BasicPacket;

public interface IConsoleShow {

	// 返回命令是否已成功处理
	boolean onConsole(BasicPacket cp);

	Client getClient();

	void onDisconnected();

}

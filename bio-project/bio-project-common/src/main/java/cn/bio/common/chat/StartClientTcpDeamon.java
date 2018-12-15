package cn.bio.common.chat;

import cn.bio.common.client.Client;
import cn.bio.common.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class StartClientTcpDeamon implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(StartClientTcpDeamon.class);
	private static boolean isStart = true;

	@Override
	public void run() {

		ServerSocket serverSocket = null;
		try {

			serverSocket = new ServerSocket(Config.SOCKET_PORT);
			while (isStart) {
				logger.debug("等待用户接入 : ");
				Socket socket = serverSocket.accept();
				logger.debug(String.format("A new client has come from %s:%d ",
						socket.getRemoteSocketAddress().toString(), socket.getPort()));

				Client newClient = new Client();
				newClient.setProcotol(new ClientProtocol(newClient));

				ClientHub.getInstance().onClientConnect(newClient, socket);
			}

			serverSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					isStart = false;
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ClientHub.getInstance().disconnectAllClient();
		}
	}

}

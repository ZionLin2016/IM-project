package cn.bio.client;

import cn.bio.common.protocol.Client;

import java.io.IOException;
import java.net.Socket;

public class StartClient2 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(Config.SOCKET_HOST, Config.SOCKET_PORT);
        new Client().onConnect(socket);
    }
}

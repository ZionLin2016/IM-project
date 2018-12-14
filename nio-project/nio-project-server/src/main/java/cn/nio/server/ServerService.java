package cn.nio.server;


import cn.nio.server.service.ServiceThread;
import cn.nio.server.utils.ChatServerConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ServerService {
    
    public static void main(String[] args) throws IOException {
        
        ServerSocketChannel listenChannel = ServerSocketChannel.open();
        
        listenChannel.configureBlocking(false);
        
        listenChannel.socket().bind(new InetSocketAddress(ChatServerConfig.PORT));
        
        Selector selector = Selector.open();
        
        listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        ServiceThread st = new ServiceThread(listenChannel, selector);
        st.start();
        
        while (st.isAlive()) {
            Thread.yield();
        }
        
    }
    
}

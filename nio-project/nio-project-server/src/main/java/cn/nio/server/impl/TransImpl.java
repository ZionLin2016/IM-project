package cn.nio.server.impl;


import cn.nio.server.UserHolder;
import cn.nio.server.utils.CharactorUtils;
import cn.nio.server.utils.ChatServerConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TransImpl extends TransAbstract {
    
    ByteBuffer buffer = ByteBuffer.allocate(ChatServerConfig.BUFFER_SIZE);
    
    public void accept(SelectionKey selectionKey) throws IOException {
        
        ServerSocketChannel listenChannel = (ServerSocketChannel) selectionKey.channel();
        
        SocketChannel clientChannel = listenChannel.accept();
        if (clientChannel == null) return;
        clientChannel.configureBlocking(false);
        clientChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(ChatServerConfig.BUFFER_SIZE));
        
    }
    
    
    public String read(SelectionKey selectionKey) throws IOException {
        
        String receive = "";
        
        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
        
        buffer.clear();
        int size = clientChannel.read(buffer);
        if (size == -1) {
            disconnect(clientChannel);
            return null;
        }
        buffer.flip();
        receive = CharactorUtils.utf16Decoder.decode(buffer).toString();
        
        System.out.println(receive);
        
        if (UserHolder.getHolder().containsKey(selectionKey)) {
            return receive;
        }
        UserHolder.getHolder().addUser(receive, selectionKey);
        
        return null;
    }
    
    private void disconnect(SocketChannel clientChannel) {
        
        System.out.println(clientChannel.socket().getRemoteSocketAddress() + "\t的用户断开了连接");
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

package cn.nio.client;


import cn.nio.client.utils.CharactorUtils;
import cn.nio.client.utils.ChatClientConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public class ReadThread extends Thread {
    
    ByteBuffer buffer = ByteBuffer.allocate(ChatClientConfig.BUFFER_SIZE);
    
    @SuppressWarnings("unused")
	@Override
    public void run() {
        
        SocketChannel clientChannel = ClientService.clientChannel;
        Selector selector = ClientService.selector;
        
        try {
            while (selector.select() > 0) {
            	// Java8的写法 
                selector.selectedKeys().forEach((key) -> {
                    
                    if (key.isReadable()) {
                        String receive = null;
                        buffer.clear();
                        try {
                            ((SocketChannel) key.channel()).read(buffer);
                            buffer.flip();
                            receive = CharactorUtils.utf16Decoder.decode(buffer).toString();
                            System.out.println(receive);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            selector.selectedKeys().remove(key);
                        }
                    }
                });
       
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

package cn.nio.client;



import cn.nio.client.utils.CharactorUtils;
import cn.nio.client.utils.ChatClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


/**
 * 
 * 客户端启动入口
 *
 */
public class ClientService {
    
    public static SocketChannel clientChannel = null;// TCP网络通道
    public static Selector selector = null;          // 选择器 
    
    public static void main(String[] args) throws IOException {
        
    	// 打开 SocketChannel
        clientChannel = SocketChannel.open(new InetSocketAddress(ChatClientConfig.HOSTS, ChatClientConfig.PORT));
        
        // 通道设置为非阻塞模式
        clientChannel.configureBlocking(false);
        
        // 打开选择器
        selector = Selector.open();
        
        // 通道注册到 选择器上 
        clientChannel.register(selector, SelectionKey.OP_READ);
        
        // 启动线程
        ReadThread readThread = new ReadThread();
        readThread.start();
        
        // 发送消息
        sendMsg();
    }
    
    /**
     *  
     *  发送消息的具体实现
     *  
     */
    public static void sendMsg() {
    	 
    	// 创建一个容量为1024字节的ByteBuffe
        ByteBuffer buffer = ByteBuffer.allocate(ChatClientConfig.BUFFER_SIZE);
        
        // 接受控制台输入的消息
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        String msg = null;
        System.out.print("请输入用户名：");
        buffer.clear();
        try {
            while ((msg = br.readLine()) != null) {
    
                buffer.put(CharactorUtils.utf16Encode(msg));
                buffer.flip(); 
                
                clientChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}

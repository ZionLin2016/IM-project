package cn.nio.client.utils;

/**
 * 
 * 客户端聊天配置
 *
 */
public interface ChatClientConfig {
	
    /**
     * 端口 
     */
    int PORT = 10131;
    
    /**
     * buffer_size 的大小
     */
    int BUFFER_SIZE = 1024;
    
    /**
     * 超时时间
     */
    int TIME_OUT = 3000;
    
    /**
     * Host
     */
    String HOSTS = "localhost";
    
    
}

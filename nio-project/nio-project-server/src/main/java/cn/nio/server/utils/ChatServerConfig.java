package cn.nio.server.utils;

/**
 * 
 * �������������
 *
 */
public interface ChatServerConfig {
    
	/**
     * �˿� 
     */
    int PORT = 10131;
    
    /**
     * buffer_size �Ĵ�С
     */
    int BUFFER_SIZE = 1024;
    
    /**
     * ��ʱʱ��
     */
    int TIME_OUT = 3000;
    
    /**
     * Host
     */
    String HOSTS = "localhost";
    
}

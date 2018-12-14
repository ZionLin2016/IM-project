package cn.nio.server.utils;


public interface Protocol {
  
	/**
     *       1      2       3       4       5      
     *      ������     ����Ϣ  
     */
    public static int NEW_ACCEPT = 0b00000001;
    
    public static int SEND_MSG = 0b00000010;
    
}

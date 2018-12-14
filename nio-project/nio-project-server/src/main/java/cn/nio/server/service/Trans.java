package cn.nio.server.service;

import java.io.IOException;
import java.nio.channels.SelectionKey;


public interface Trans {
    
    void handlerAccept(SelectionKey selectionKey) throws IOException;
    
    String handlerRead(SelectionKey selectionKey) throws IOException;
    
}

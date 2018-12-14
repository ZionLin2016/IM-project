package cn.nio.server.impl;


import cn.nio.server.service.Trans;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public abstract class TransAbstract implements Trans {
    
    public void handlerAccept(SelectionKey selectionKey) throws IOException {
        this.accept(selectionKey);
    }
    
    public String handlerRead(SelectionKey selectionKey) throws IOException {
        return this.read(selectionKey);
    }
    
    abstract void accept(SelectionKey selectionKey) throws IOException;
    
    abstract String read(SelectionKey selectionKey) throws IOException;
    
    
}

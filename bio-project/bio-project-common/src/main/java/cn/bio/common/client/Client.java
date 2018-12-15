package cn.bio.common.client;


import cn.bio.common.parser.BasicPacket;
import cn.bio.common.parser.BasicPacketParser;
import cn.bio.common.util.GetTime;
import cn.bio.common.util.PacketParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client implements Comparable<Client> {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    protected int clientID;
    protected String userName;
    protected int groupID;
    protected IConsoleShow protocol;
    private volatile boolean isLoopRecAndSend = true;

    private ThreadReceiveHandler threadReceiveHandler;
    private ThreadSendHander threadSendHander;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private volatile ConcurrentLinkedQueue<BasicPacketParser> queueToSend = new ConcurrentLinkedQueue<BasicPacketParser>();
    protected CopyOnWriteArrayList<ReceiveObserver> msgObserverList = new CopyOnWriteArrayList<ReceiveObserver>();

    private String userHostAddress = null;

    public String getUserIP() {
        if (userHostAddress == null) {
            userHostAddress = socket.getInetAddress().getHostAddress();
        }
        return userHostAddress;
    }

    public String getUserPort() {
        return "null";
    }

    public void setProcotol(IConsoleShow protocol) {
        this.protocol = protocol;
    }

    public void receiveAddObserver(ReceiveObserver observer) {
        if (msgObserverList.contains(observer)) {
            return;
        }

        msgObserverList.add(observer);
    }

    protected void sendToAllObservers(Object msg) {
        for (ReceiveObserver observer : msgObserverList) {
            try {
                observer.onIMMessage(msg);
            } catch (Exception e) {
                // do thing
            }
        }
    }

    public void receiveRemoveObserver(ReceiveObserver observer) {
        msgObserverList.remove(observer);
    }

    public Client() {

    }

    public void onConnect(Socket socket) {

        this.socket = socket;
        logger.debug("用户IP/port：" + getUserIP() + ":" + getUserPort());

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            // 开启接收线程
            threadReceiveHandler = new ThreadReceiveHandler();
            threadReceiveHandler.start();
            threadReceiveHandler.setPriority(Thread.MAX_PRIORITY);

            // 开启发送线程
            threadSendHander = new ThreadSendHander();
            threadSendHander.start();
            threadSendHander.setPriority(Thread.MAX_PRIORITY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized private void onDisconnected() {

        if (!isLoopRecAndSend) {
            return;
        }

        isLoopRecAndSend = false;

        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread() {

            @Override
            public void run() {

                logger.debug("onDisconnected() 1");

                protocol.onDisconnected();

                logger.debug("onDisconnected() 2");

                if (threadSendHander != null) {
                    try {
                        threadSendHander.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    threadSendHander = null;
                }

                logger.debug("onDisconnected() 3");

                if (threadReceiveHandler != null) {
                    try {
                        threadReceiveHandler.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    threadReceiveHandler = null;
                }

                logger.debug("onDisconnected() 4");

                if (inputStream != null) {
                    PacketParserUtil.closeInputStream(inputStream);
                    inputStream = null;
                }

                if (outputStream != null) {
                    PacketParserUtil.closeOutputStream(outputStream);
                    outputStream = null;
                }
                logger.debug("onDisconnected() 5");
            }
        }.start();

    }

    public void stop() {

        logger.debug("\n[Disconecting.....Client " + this.clientID + "　]\n");
        msgObserverList.clear();
        onDisconnected();
    }

    // 添加发送信息返回给自身
    public void sendMessage(BasicPacketParser selfData) {
        if (!isConnected()) {
            return;
        }

        logger.debug("send Msg to Client " + getClientID());

        // 有新增待发送数据，则唤醒发送线程
        synchronized (queueToSend) {
            queueToSend.offer(selfData);
            queueToSend.notifyAll();
        }
    }

    /**
     * 是否在线
     *
     * @return
     */
    public boolean isConnected() {
        if (socket.isClosed() || !socket.isConnected()) {
            return false;
        }
        return true;
    }

    public class ThreadReceiveHandler extends Thread {

        private void ackWrongMsg() {
            logger.debug("%%%%%%%%%% ackWrongMsg (TODO)");
        }

        @Override
        public void run() {
            while (isLoopRecAndSend) {
                if (!isConnected()) {
                    break;
                }
                try {
                    if (inputStream != null) {
                        BasicPacket cp = PacketParserUtil.readFromStream(inputStream);

                        boolean succ = protocol.onConsole(cp);

                        if (!succ) {
                            ackWrongMsg();
                        }

                        // TODO: deal with the case when the state is END
                    }
                } catch (EOFException e) {
                    logger.debug("客户端 sends EOF. Socket is closed");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

            }

            onDisconnected();

            logger.debug("End of Thread: ThreadReceiveHandler");
        }

    }

    public class ThreadSendHander extends Thread {

        @Override
        public void run() {

            while (isLoopRecAndSend) {
                if (!isConnected()) {
                    break;
                }

                BasicPacketParser selfpro = null;

                synchronized (queueToSend) {
                    while (isLoopRecAndSend && selfpro == null) {
                        try {
                            selfpro = queueToSend.poll();
                            if (selfpro == null) {
                                queueToSend.wait(100);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                    }

                    if (selfpro != null) {
                        try {
                            PacketParserUtil.writeToStream(selfpro, outputStream);

                            logger.debug(String.format("[Client %s] going a message", getClientID()));
                            logger.debug(String.format(
                                    "[%s] ***服务器 sends to Client (%d): size=%d protocol=%d content=%s",
                                    GetTime.getTimeShort(),
                                    getClientID(), selfpro.getLength(),
                                    selfpro.getProtocolNum(),
                                    selfpro.toString()));
                        } catch (EOFException e1) {
                            logger.debug("这是合法的，客户端已关闭");
                            e1.printStackTrace();
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }

                }

            }

            onDisconnected();

            logger.debug("End of Thread: ThreadSendHander");
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {

        return userName;

    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getSizeOfQueueToSend() {
        return queueToSend.size();
    }


    @Override
    public int compareTo(Client o) {
        if (o == null) {
            return -1;
        }

        return this.clientID - ((Client) o).clientID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Client) {
            return ((Client) obj).clientID == clientID;
        } else {
            return false;
        }
    }
}

package cn.bio.server.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.bio.server.dto.Data;
import cn.bio.server.dto.Ping;
import cn.bio.server.parser.BasicPacket;
import cn.bio.server.parser.BlockPacketParser;
import cn.bio.server.parser.StreamPacketParser;
import cn.bio.server.protocol.Client;
import cn.bio.server.protocol.ICmdProtocol;
import cn.bio.server.util.GetTime;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProtocol implements ICmdProtocol {
    private static final Logger logger = LoggerFactory.getLogger(ClientProtocol.class);
    private String TAG = "Protocol";

    protected ClientState curState = ClientState.Uninitialized;
    private final Client client;
    // Properties properties = new Properties();
    private boolean succ = false;

    public enum ClientState {
        Uninitialized, End, Leader_Begin, Leader_Responding, Member_Begin, Member_Listening
    }

    private ArrayList<ICmdProtocol> proChain = new ArrayList<ICmdProtocol>();

    public ClientProtocol(Client client) {
        this.client = client;
    }

    @Override
    public boolean onCmd(BasicPacket cp) {

        if (cp.protocolNum == ProtocolType.PING) {
            // //to do
            // logger.debug(TAG, );
            // logger.debug(TAG, "Received a ping");
            // logger.debug(TAG, );
            return true;
        }

        logger.debug(TAG, String.format("[Client %s] Coming a message", getClient().getClientID()));
        logger.debug(TAG, "+++[" + GetTime.getTimeShort() + "][BEGIN] Old State = " + curState);
        logger.debug(TAG,
                String.format("Protocol Type: %s (%d) ", ProtocolType.getMsgName(cp.protocolNum), cp.protocolNum));
        logger.debug(TAG, "Packet Size =   " + cp.size);

        if (cp.protocolNum != ProtocolType.AUDIO) {
            logger.debug(TAG, "coming content in text:" + cp.turnToContent);
        } else {
            logger.debug(TAG, "当前收到声音" + cp.data.length + "字节");
            logger.debug(TAG, "收到的声音字节：" + Arrays.toString(cp.data));
        }

        switch (curState) {
            case Uninitialized:
                if (cp.protocolNum == ProtocolType.GROUP_CREATE_AND_JOIN) {
                    curState = ClientState.Leader_Begin;
                    succ = true;
                } else {
                    break;
                }
                break;

            case Leader_Begin:
                if (cp.protocolNum == ProtocolType.GROUP_CREATE_AND_JOIN) {
                    parseGuideBegin(cp);
                    curState = ClientState.Leader_Responding;
                    succ = true;
                    break;
                } else {
                    break;
                }

            case Leader_Responding:
                switch (cp.protocolNum) {
                    case ProtocolType.DATA:
                        parseData(cp);
                        succ = true;
                        break;

                    case ProtocolType.GROUP_DISMISS:
                        parseGroupDismiss(cp);
                        curState = ClientState.End;
                        succ = true;
                        break;

                    case ProtocolType.AUDIO:
                        parseAudio(cp);
                        succ = true;
                        break;

                    case ProtocolType.GROUP_CREATE_AND_JOIN:
                        parseGuideBegin(cp);
                        succ = true;
                        break;
                }

                break;

            case Member_Begin:
                // if (cp.protocolNum == ProtocolType.GROUP_LIST) {
                // parseGuideList(cp);
                // succ = true;
                // break;
                // }
                // else if (cp.protocolNum == ProtocolType.GROUP_JOIN) {
                // parseSelectGuide(cp);
                // curState = ClientState.Stu_Listening;
                // succ = true;
                // break;
                // }
                break;

            case Member_Listening:
                switch (cp.protocolNum) {
//                    case ProtocolType.GROUP_LIST:
//                        parseGuideList(cp);
//                        succ = true;
//                        break;

                    case ProtocolType.GROUP_JOIN:
                        // parseSelectGuide(cp);
                        succ = true;
                        break;

                    case ProtocolType.GROUP_LEAVE:
                        parseGroupLeave(cp);
                        curState = ClientState.End;
                        succ = true;
                        break;

                    case ProtocolType.DATA:
                        parseData(cp);
                        succ = true;
                        break;
                }

                break;

            case End:
                break;
        }

        if (!succ) {
            logger.debug(TAG, "+++[Wrong Msg] unregnized message = " + cp.protocolNum);
        } else {
            logger.debug(TAG, "+++[" + GetTime.getTimeShort() + "][END], New State = " + curState);
        }

        return succ;
    }

    protected void parseAudio(BasicPacket cp) {

        logger.debug(TAG, "[Command] ParseAudio");

        StreamPacketParser protocol = new StreamPacketParser();
        protocol.setAudioData(cp.data);
        protocol.setProtocolNum(ProtocolType.AUDIO);

        ClientHub.getInstance().sendMsgToGroupExceptSelf(getClient(), protocol);

    }

    protected void parsePing(BasicPacket cp) {
        Gson gson = new Gson();
        Ping ping = gson.fromJson(cp.turnToContent, Ping.class);
        logger.debug(TAG, "心跳id+预留信息：" + ping.getPingid() + ping.getReserved());

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setData(cp.turnToContent);
        protocol.setProtocolNum(ProtocolType.PING);

        ClientHub.getInstance().sendMsgToClient(getClient(), protocol);
    }

    protected void parseData(BasicPacket cp) {
        Gson gson = new Gson();
        Data data = gson.fromJson(cp.turnToContent, Data.class);
        logger.debug(TAG, "Client(ID=" + getClient().getClientID() + "：" + data.toString());

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setData(cp.turnToContent);
        protocol.setProtocolNum(ProtocolType.DATA);

        // call ClientHub
        ClientHub.getInstance().sendMsgToGroupExceptSelf(getClient(), protocol);

    }

    @Override
    public Client getClient() {
        return this.client;
    }

    protected void parseGroupLeave(BasicPacket cp) {

        Map<String, Boolean> backdate = new HashMap<String, Boolean>();
        backdate.put("status", true);
        Gson gson = new Gson();
        String temp = gson.toJson(backdate);

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setProtocolNum(ProtocolType.GROUP_DISMISS);
        Data data = new Data();
        data.setPattion(3);
        data.setData(temp);

        String temp2 = gson.toJson(data);
        protocol.setData(temp2);

        ClientHub.getInstance().sendMsgToClient(client, protocol);
        logger.debug(TAG, "[Command] RemoveTourist");

    }

    // protected void parseSelectGuide(BasicPacket cp) {
    //
    // logger.debug(TAG, "[Command] SelectGuide");
    //
    // Gson gson = new Gson();
    // GroupJoin sel = gson.fromJson(cp.turnToContent, GroupJoin.class);
    //
    // logger.debug(TAG, "服务器接收到的导游ID为：" + sel.getGuideid());
    //
    // GroupJoin sg = new GroupJoin();
    // List<Client> list = ClientHub.getInstance().onGuideList();
    // for (Client wh : list) {
    // if (wh.getClientID() == sel.getGuideid()) {
    // // 根据选择的导游加到该导游对应的群组
    // ClientHub.getInstance().onSelectGuide(getClient(), sel.getGuideid());
    // sg.setStatus(100);// 请求成功
    // sg.setGroupid(sel.getGuideid());
    // sg.setGuideid(sel.getGuideid());
    // } else {
    // sg.setStatus(101);
    // }
    // }
    //
    // String temp = gson.toJson(sg);
    //
    // BlockPacketParser protocol = new BlockPacketParser();
    // protocol.setProtocolNum(ProtocolType.GROUP_JOIN);
    // protocol.setData(temp);
    //
    // ClientHub.getInstance().sendMsgToClient(getClient(), protocol);
    //
    // }

    protected void parseGuideBegin(BasicPacket cp) {

        logger.debug(TAG, "[Command] TeacherBegin");
        // call ClientHub
        // this operation always can be successful;
        ClientHub.getInstance().onGuideBegin(getClient());

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setData(cp.turnToContent);
        protocol.setProtocolNum(ProtocolType.GROUP_CREATE_AND_JOIN);
        logger.debug(TAG, "协议号：" + protocol.getProtocolNum());
        System.out.println("协议号：" + protocol.getProtocolNum());

        ClientHub.getInstance().sendMsgToClient(getClient(), protocol);

    }

    protected void parseGroupDismiss(BasicPacket cp) {

        logger.debug(TAG, "[Command] RemoveGuide");

        Map<String, Boolean> backdate = new HashMap<String, Boolean>();
        backdate.put("status", true);

        Gson gson = new Gson();
        String temp = gson.toJson(backdate);

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setProtocolNum(ProtocolType.GROUP_DISMISS);
        Data data = new Data();
        data.setPattion(3);
        data.setData(temp);

        String temp2 = gson.toJson(data);
        protocol.setData(temp2);

        ClientHub.getInstance().sendMsgToClient(client, protocol);

        BlockPacketParser toTourist = new BlockPacketParser();
        toTourist.setProtocolNum(ProtocolType.GROUP_LEAVE);
        Data data2 = new Data();
        data2.setPattion(3);
        data2.setData(temp);

        String temp3 = gson.toJson(data2);

        toTourist.setData(temp3);

        // call ClientHub
        ClientHub.getInstance().sendMsgToGroupExceptSelf(getClient(), toTourist);

    }

    @Override
    public void onDisconnected() {
        ClientHub.getInstance().onClientDisconnect(client);

    }

}

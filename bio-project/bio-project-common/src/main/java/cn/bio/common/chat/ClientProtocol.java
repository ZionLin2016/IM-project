package cn.bio.common.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bio.common.dto.Data;
import cn.bio.common.dto.GroupJoin;
import cn.bio.common.dto.Ping;
import cn.bio.common.parser.BasicPacket;
import cn.bio.common.parser.BlockPacketParser;
import cn.bio.common.parser.StreamPacketParser;
import cn.bio.common.protocol.Client;
import cn.bio.common.protocol.ICmdProtocol;
import cn.bio.common.util.GetTime;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProtocol implements ICmdProtocol {
    private static final Logger logger = LoggerFactory.getLogger(ClientProtocol.class);

    public ClientProtocol(Client client) {
        this.client = client;
    }

    public enum ClientState {
        Uninitialized, End, Leader_Begin, Leader_Responding, Member_Begin, Member_Listening
    }

    protected ClientState curState = ClientState.Uninitialized;
    private final Client client;
    private boolean succ = false;

    private ArrayList<ICmdProtocol> proChain = new ArrayList<ICmdProtocol>();

    @Override
    public Client getClient() {
        return this.client;
    }

    @Override
    public boolean onCmd(BasicPacket cp) {

        if (cp.protocolNum == ProtocolType.PING) {
            logger.debug("Received a ping");

            return true;
        }

        logger.debug(String.format("[Msg: Client %s]===[State: %s]===[Size: %d]", getClient().getClientID(), curState, cp.size));
        logger.debug(String.format("[Protocol Type: %s(%d)] ", ProtocolType.getMsgName(cp.protocolNum), cp.protocolNum));

        if (cp.protocolNum != ProtocolType.AUDIO) {
            logger.debug("收到的文本内容:" + cp.turnToContent);
        } else {
            logger.debug("当前收到声音" + cp.data.length + "字节");
//            logger.debug("收到的声音字节：" + Arrays.toString(cp.data));
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
                    parseLeaderBegin(cp);
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
                        parseLeaderBegin(cp);
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
                // else
                 if (cp.protocolNum == ProtocolType.GROUP_JOIN) {
                 parseSelectLeader(cp);
                 curState = ClientState.Member_Listening;
                 succ = true;
                 break;
                 }
                break;

            case Member_Listening:
                switch (cp.protocolNum) {
//                    case ProtocolType.GROUP_LIST:
//                        parseGuideList(cp);
//                        succ = true;
//                        break;

                    case ProtocolType.GROUP_JOIN:
                         parseSelectLeader(cp);
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
            logger.error("===[Wrong Msg] unregnized message = " + cp.protocolNum);
        } else {
            logger.debug("+++[" + GetTime.getTimeShort() + "][END], New State = " + curState);
        }

        return succ;
    }

    protected void parseAudio(BasicPacket cp) {

        logger.debug("[Command] ParseAudio");

        StreamPacketParser protocol = new StreamPacketParser();
        protocol.setAudioData(cp.data);
        protocol.setProtocolNum(ProtocolType.AUDIO);

        ClientHub.getInstance().sendMsgToGroupExceptSelf(getClient(), protocol);

    }

    protected void parsePing(BasicPacket cp) {
        Gson gson = new Gson();
        Ping ping = gson.fromJson(cp.turnToContent, Ping.class);
        logger.debug("心跳id+预留信息：" + ping.getPingId() + ping.getReserved());

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setData(cp.turnToContent);
        protocol.setProtocolNum(ProtocolType.PING);

        ClientHub.getInstance().sendMsgToClient(getClient(), protocol);
    }

    protected void parseData(BasicPacket cp) {
        Gson gson = new Gson();
        Data data = gson.fromJson(cp.turnToContent, Data.class);
        logger.debug("Client(ID=" + getClient().getClientID() + "：" + data.toString());

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setData(cp.turnToContent);
        protocol.setProtocolNum(ProtocolType.DATA);

        // call ClientHub
        ClientHub.getInstance().sendMsgToGroupExceptSelf(getClient(), protocol);

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
        logger.debug("[Command] RemoveTourist");

    }

    protected void parseSelectLeader(BasicPacket cp) {

        logger.debug("[Command] SelectGuide");

        Gson gson = new Gson();
        GroupJoin sel = gson.fromJson(cp.turnToContent, GroupJoin.class);

        logger.debug("服务器接收到的导游ID为：" + sel.getLeaderId());

        GroupJoin sg = new GroupJoin();
        List<Client> clientList = ClientHub.getInstance().onLeaderList();
        for (Client client : clientList) {
            if (client.getClientID() == sel.getLeaderId()) {
                // 根据选择的导游加到该导游对应的群组
                ClientHub.getInstance().onSelectLeader(getClient(), sel.getLeaderId());
                sg.setStatus(100);// 请求成功
                sg.setGroupId(sel.getLeaderId());
                sg.setLeaderId(sel.getLeaderId());
            } else {
                sg.setStatus(101);
            }
        }

        String temp = gson.toJson(sg);

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setProtocolNum(ProtocolType.GROUP_JOIN);
        protocol.setData(temp);

        ClientHub.getInstance().sendMsgToClient(getClient(), protocol);

    }

    protected void parseLeaderBegin(BasicPacket cp) {

        logger.debug("[Command] TeacherBegin");
        // call ClientHub
        // this operation always can be successful;
        ClientHub.getInstance().onLeaderBegin(getClient());

        BlockPacketParser protocol = new BlockPacketParser();
        protocol.setData(cp.turnToContent);
        protocol.setProtocolNum(ProtocolType.GROUP_CREATE_AND_JOIN);
        logger.debug("协议号：" + protocol.getProtocolNum());

        ClientHub.getInstance().sendMsgToClient(getClient(), protocol);

    }

    protected void parseGroupDismiss(BasicPacket cp) {

        logger.debug("[Command] RemoveGuide");

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

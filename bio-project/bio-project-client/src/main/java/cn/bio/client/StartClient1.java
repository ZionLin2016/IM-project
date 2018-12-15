package cn.bio.client;


import cn.bio.common.chat.ProtocolType;
import cn.bio.common.dto.Data;
import cn.bio.common.dto.GroupCreateAndJoin;
import cn.bio.common.parser.BlockPacketParser;
import cn.bio.common.client.Client;
import cn.bio.common.util.PacketParserUtil;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.net.Socket;


public class StartClient1 {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(Config.SOCKET_HOST, Config.SOCKET_PORT);
        new Client().onConnect(socket);

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        BlockPacketParser blockPacketParser = new BlockPacketParser();
        GroupCreateAndJoin gcaj = new GroupCreateAndJoin();
        gcaj.setGroupId(0);
        gcaj.setLeaderId(0);
        gcaj.setReserved("create group test --来自客户端1");

        blockPacketParser.setProtocolNum(ProtocolType.GROUP_CREATE_AND_JOIN);
        blockPacketParser.setData(new Gson().toJson(gcaj));
        PacketParserUtil.writeToStream(blockPacketParser,outputStream);

        Data data = new Data();
        data.setData("来自客户端1");
        data.setPattion(0);
        data.setType(0);

//        blockPacketParser.setProtocolNum(ProtocolType.DATA);
        blockPacketParser.setData(new Gson().toJson(data));
        for (int i = 0; i < 50; i++) {
            Thread.sleep(1000);
            PacketParserUtil.writeToStream(blockPacketParser,outputStream);
        }
    }
}
package cn.bio.client;

import cn.bio.common.chat.ProtocolType;
import cn.bio.common.client.Client;
import cn.bio.common.dto.Data;
import cn.bio.common.dto.GroupJoin;
import cn.bio.common.parser.BlockPacketParser;
import cn.bio.common.util.PacketParserUtil;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.net.Socket;

public class StartClient2 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(Config.SOCKET_HOST, Config.SOCKET_PORT);
        new Client().onConnect(socket);

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        BlockPacketParser blockPacketParser = new BlockPacketParser();
        GroupJoin groupJoin = new GroupJoin();
        groupJoin.setGroupId(0);
        groupJoin.setLeaderId(0);
        groupJoin.setUserId(101);
        groupJoin.setStatus(0);

        blockPacketParser.setProtocolNum(ProtocolType.GROUP_JOIN);
        blockPacketParser.setData(new Gson().toJson(groupJoin));
        PacketParserUtil.writeToStream(blockPacketParser, outputStream);

        Data data = new Data();
        data.setData("test");
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

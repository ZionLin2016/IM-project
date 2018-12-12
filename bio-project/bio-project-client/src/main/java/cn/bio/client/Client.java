package cn.bio.client;

import cn.bio.client.protocol.DataProtocol;
import cn.bio.client.util.SocketUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {

	private ReciveTask reciveTask;
	private SendMsgTask sendMsgTask;

	void conn() {
		try {
			System.out.println("正在尝试连接服务端...");
			Socket client = new Socket("127.0.0.1", 2017);
			System.out.println("客户端1启动---------");
			// 开启接收线程
			reciveTask = new ReciveTask();
			reciveTask.inputStream = new DataInputStream(client.getInputStream());
			reciveTask.start();

			// 开启发送线程
			sendMsgTask = new SendMsgTask();
			sendMsgTask.outputStream = new DataOutputStream(client.getOutputStream());
			sendMsgTask.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private void acceptMsg(InputStream ins) throws IOException{
	//
	// System.out.println("客户端接收到的数据："+SocketUtil.readFromStream(ins).getHead()+"--"+SocketUtil.readFromStream(ins).getProtocolType());
	// }

	public class ReciveTask extends Thread {
		private DataInputStream inputStream;

		@Override
		public void run() {
			while (true) {
				System.out
						.println("--客户端1接收到的数据为--" + ((DataProtocol) SocketUtil.readFromStream(inputStream)).getData());
			}
		}
	}

	public class SendMsgTask extends Thread {
		private DataOutputStream outputStream;

		@Override
		public void run() {
			DataProtocol protocol = new DataProtocol();
			protocol.setDtype(0);
			protocol.setPattion(0);
			protocol.setUserid(0);
			protocol.setUtype(0);
			protocol.setVersion(0);
			protocol.setData("客户端11111111111111111");

			for (int i = 0; i < 100; i++) {
				try {
					Thread.sleep(1000);
					SocketUtil.write2Stream(protocol, outputStream);
					System.out.println("客户端1第" + i + "整条数据长度：" + protocol.getLength());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			SocketUtil.closeOutputStream(outputStream);
		}
	}
}
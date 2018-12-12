package cn.bio.server.beanparser;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BeanParserUtil {

	private static Gson gson = new Gson();

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static Object fromJson(String json, Class<?> obj) {
		return gson.fromJson(json, obj);
	}

	// 读数据
	public static BeanPacket readFromStream(DataInputStream dins)
			throws IOException {

		BeanPacket re = new BeanPacket();

		re.headId = dins.readInt();
		re.size = dins.readInt();
		re.protocolNum = dins.readInt();
		re.data = new byte[re.size - 4 - 4 - 4];
		dins.readFully(re.data);

		re.turnToContent = new String(re.data, "UTF-8");

		return re;
	}

	// 写数据
	public static void writeToStream(BasicBeanParser basicProtocol,
                                     DataOutputStream outputStream) throws Exception {

		byte[] buffData = basicProtocol.sendAllData();
		outputStream.write(buffData);
		outputStream.flush();
	}

	// 关闭输入流
	public static void closeInputStream(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 关闭输出流
	public static void closeOutputStream(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "SocketUtil [getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

}
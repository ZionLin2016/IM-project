package cn.bio.common;

import cn.bio.common.chat.StartClientTcpDeamon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StartServer {
	public static void main(String[] args) {
		ExecutorService cachedThreadPool =  new ThreadPoolExecutor(0, Integer.MAX_VALUE,
				60L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>());

		cachedThreadPool.execute(new StartClientTcpDeamon());
	}
}

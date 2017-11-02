package com.mina.handler;


import com.mina.util.DataFormatTransformUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.MessageProtoBuf;
import sun.rmi.runtime.Log;

import java.text.SimpleDateFormat;


public class ServerHandler extends IoHandlerAdapter {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public ServerHandler() {
		System.out.println("ClientHandler启动..");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println("exceptionCaught: " + cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		MessageProtoBuf.ProtoMessage protoMessage = (MessageProtoBuf.ProtoMessage) message;
		logger.info(protoMessage.toString());
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("messageSented..");

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("sessionClosed:"+session.toString());
		session.closeNow();
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpen");
//		MessageProtoBuf.ProtoMessage message = DataFormatTransformUtil.packingToProtoMessageOption(MessageProtoBuf.ProtoMessage.Type.LOGIN,
//				"miles(1)","","");
//		logger.info("login time:");
//		session.write(message);
//		logger.info("write done:");


//		Thread.sleep(3000);
//
//		MessageProtoBuf.ProtoMessage chatMessage = DataFormatTransformUtil.packingToProtoMessageOption(MessageProtoBuf.ProtoMessage.Type.CHAT,
//				"miles(1)","1","hello");
//		logger.info("chat time:");
//		session.write(chatMessage);
//		logger.info("write done:");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

}

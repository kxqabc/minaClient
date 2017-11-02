package com.mina;

import com.mina.coder.MessageCodeFactory;
import com.mina.handler.ServerHandler;
import com.mina.util.DataFormatTransformUtil;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.MessageProtoBuf;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientMain {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String localHost = "127.0.0.1";

    private static final String remoteAdress = "139.199.170.95";

    private static final int PORT = 8989;

    private SocketConnector connector;

    private ConnectFuture future;

    private IoSession session;

    public ClientMain() {
    }

    public static void main(String[] args) {
        ClientMain clientMain = new ClientMain();
//        clientMain.start();
        clientMain.minaClient(localHost,PORT);
    }

    public void start(){
        try {
            Socket socket = new Socket(localHost,PORT);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream();
            MessageProtoBuf.ProtoMessage message = DataFormatTransformUtil.packingToProtoMessageOption(
                    MessageProtoBuf.ProtoMessage.Type.LOGIN,"kk(1)","","");
            byte[] messageBytes = message.toByteArray();
            System.out.println("messageLength:"+messageBytes.length);
            System.out.println("messageBytes:"+DataFormatTransformUtil.bytesToHexString(messageBytes));
            ByteBuffer byteBuffer = ByteBuffer.allocate(messageBytes.length+4);
            byteBuffer.putInt(messageBytes.length);
            byteBuffer.put(messageBytes);
            byteBuffer.flip();
            outputStream.write(byteBuffer.array());
//            byte[] receiveBytes = new byte[];
            int input;
            while (socket.isConnected()){
                while ((input = bufferedInputStream.read())!=-1){
                    System.out.printf(input+" ");
                }
            }

            bufferedInputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean minaClient(String address,int port){
        connector = new NioSocketConnector();
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();
        chain.addLast("myFilter",new ProtocolCodecFilter(new MessageCodeFactory()));
        connector.setHandler(new ServerHandler());

        future = connector.connect(new InetSocketAddress(localHost, port));
        // 等待连接创建完成
        future.awaitUninterruptibly();

        /*
         * 5.获取session对象,通过session可以向服务器发送消息；
         */
        session = future.getSession();
        session.getConfig().setUseReadOperation(true);
        		MessageProtoBuf.ProtoMessage message = DataFormatTransformUtil.packingToProtoMessageOption(MessageProtoBuf.ProtoMessage.Type.LOGIN,
				"miles(1)","","");
		logger.info("login time:");
		session.write(message);
		logger.info("write done:");


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MessageProtoBuf.ProtoMessage chatMessage = DataFormatTransformUtil.packingToProtoMessageOption(MessageProtoBuf.ProtoMessage.Type.CHAT,
				"miles(1)","1","hello");
		logger.info("chat time:");
		session.write(chatMessage);
		logger.info("write done:");
        return future.isConnected();
    }


}

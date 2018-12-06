package io.meltcoin.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Peer {

    public String host;
    public Integer port;
    public SocketChannel socketChannel = null;

    public Peer(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public Boolean accept(ServerSocketChannel serverSocketChannel) {
        SocketChannel acceptedChannel = null;
        try {
            acceptedChannel = serverSocketChannel.accept();
            if (acceptedChannel != null) {
                InetAddress remoteAddress = acceptedChannel.socket().getInetAddress();
                if (remoteAddress != null) {
                    this.host = remoteAddress.getAddress().toString();
                    this.port = acceptedChannel.socket().getPort();
                }
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void connect() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, this.port));
            socketChannel.configureBlocking(false);
            sendMessage(PeerMessageType.HELLO, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(PeerMessageType peerMessageType, String message) {
        if (socketChannel != null && socketChannel.isConnected()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
            byteBuffer.put((peerMessageType.name() + ":" + message).getBytes());
            try {
                socketChannel.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onMessage(PeerMessageType peerMessageType, String message) {
        System.out.println("Type: " + peerMessageType.name() + " Message: " + message);
    }
}

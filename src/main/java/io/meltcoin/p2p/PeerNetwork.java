package io.meltcoin.p2p;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;

public class PeerNetwork {

    public int listenPort;
    public ServerSocketChannel serverSocketChannel;
    public ArrayList<Peer> peers;

    public ReceiveServer receiveServer;
    public SendAction sendAction;

    public ArrayList<String> discoveredPeers;

    public PeerNetwork() {
        this.listenPort = 8420;
        this.peers = new ArrayList<>();

        receiveServer = new ReceiveServer(this.listenPort);
        receiveServer.start();

        sendAction = new SendAction();
    }

    /*public void connectToPeer(String host, Integer port) {
        peers.add(newPeer);
    }*/

    /*public void broadcastMessage(PeerMessageType peerMessageType, String message) {
        for (Peer peer : peers) {
            peer.sendMessage(peerMessageType, message);
        }
    }*/
}

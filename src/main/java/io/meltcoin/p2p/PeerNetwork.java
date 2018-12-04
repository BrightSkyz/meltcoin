package io.meltcoin.p2p;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PeerNetwork extends Thread {

    public int listenPort;
    public boolean shouldRun = true;
    public ArrayList<PeerThread> peerThreads;

    public ArrayList<String> newPeers;

    public PeerNetwork() {
        this.listenPort = 8420;
        this.peerThreads = new ArrayList<>();
        this.newPeers = new ArrayList<>();
    }

    public void connectToPeer(String peer, int port) {
        try {
            Socket socket = new Socket(peer, port);
            String remoteHost = socket.getInetAddress() + "";
            remoteHost = remoteHost.replace("/", "");
            remoteHost = remoteHost.replace("\\", "");
            int remotePort = socket.getPort();
            newPeers.add(remoteHost + ":" + remotePort);
            peerThreads.add(new PeerThread(socket));
            peerThreads.get(peerThreads.size() - 1).start();
        } catch (Exception e) {
            System.out.println("Unable to connect to " + peer + ":" + port);
        }
    }

    public PeerNetwork(int port) {
        this.listenPort = port;
        this.peerThreads = new ArrayList<>();
    }

    public void run() {
        try {
            ServerSocket listenSocket = new ServerSocket(listenPort);
            while (shouldRun) {
                peerThreads.add(new PeerThread(listenSocket.accept()));
                peerThreads.get(peerThreads.size() - 1).start();

            }
            listenSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String toBroadcast) {
        for (int i = 0; i < peerThreads.size(); i++) {
            System.out.println("Sent: " + toBroadcast);
            peerThreads.get(i).send(toBroadcast);
        }
    }

    public void broadcastIgnorePeer(String toBroadcast, String peerToIgnore) {
        for (int i = 0; i < peerThreads.size(); i++) {
            peerThreads.get(i).send(toBroadcast);
        }
    }
}
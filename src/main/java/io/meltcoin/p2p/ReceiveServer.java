package io.meltcoin.p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveServer extends Thread {

    private DatagramSocket datagramSocket = null;

    public ReceiveServer(Integer listenPort) {
        try {
            this.datagramSocket = new DatagramSocket(listenPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        String receivedData;
        while (true) {
            try {
                datagramSocket.receive(packet);
                receivedData = new String(packet.getData());
                System.out.println("Received data: " + receivedData);
                if (receivedData.contains(":")) {
                    String[] splitMessage = receivedData.split(":");
                    if (PeerMessageType.valueOf(splitMessage[0]) != null) {
                        onMessage(PeerMessageType.valueOf(splitMessage[0]), splitMessage[1]);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onMessage(PeerMessageType peerMessageType, String message) {
        // TODO: on message received
    }
}

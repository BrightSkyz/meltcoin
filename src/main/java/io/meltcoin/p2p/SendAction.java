package io.meltcoin.p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendAction {

    private DatagramSocket datagramSocket = null;

    public SendAction() {
        try {
            this.datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(PeerMessageType peerMessageType, String message, String host, Integer port) {
        // Generate message to send
        byte finalMessage[] = (peerMessageType.name() + ":" + message).getBytes();
        try {
            // Send packet after creation
            DatagramPacket packet = new DatagramPacket(finalMessage, finalMessage.length, InetAddress.getByName(host), port);
            System.out.println("Sending data: " + new String(packet.getData()));
            datagramSocket.send(packet);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

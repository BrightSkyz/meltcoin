package io.meltcoin.p2p;

import java.net.Socket;

public class PeerThread extends Thread {

    private Socket socket;
    public InputThread inputThread;
    public OutputThread outputThread;

    public PeerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Got connection from " + socket.getInetAddress() + ".");
        inputThread = new InputThread(socket);
        inputThread.start();
        outputThread = new OutputThread(socket);
        outputThread.start();
    }

    public void send(String data) {
        if (outputThread == null) {
            System.out.println("Couldn't send " + data + " !");
        } else {
            outputThread.write(data);
        }
    }
}
package io.meltcoin.p2p;

import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class OutputThread extends Thread {

    private Socket socket;

    private ArrayList<String> outputBuffer;
    private boolean shouldContinue = true;

    public OutputThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            outputBuffer = new ArrayList<>();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (shouldContinue) {
                if (outputBuffer.size() > 0) {
                    if (outputBuffer.get(0) != null) {
                        for (int i = 0; i < outputBuffer.size(); i++) {
                            if (outputBuffer.get(i).length() > 20) {
                                System.out.println("Sending " + outputBuffer.get(i).substring(0, 20) + " to " + socket.getInetAddress());
                            } else {
                                System.out.println("Sending " + outputBuffer.get(i) + " to " + socket.getInetAddress());
                            }
                            out.println(outputBuffer.get(i));
                        }
                        outputBuffer = new ArrayList<>();
                        outputBuffer.add(null);
                    }
                }
                Thread.sleep(100);
            }
            System.out.println("WHY AM I HERE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String data) {
        if (data.length() > 20) {
            System.out.println("PUTTING INTO WRITE BUFFER: " + data.substring(0, 20) + "...");
        } else {
            System.out.println("PUTTING INTO WRITE BUFFER: " + data);
        }
        File f = new File("writebuffer");
        try {
            PrintWriter out = new PrintWriter(f);
            out.println("SENDING: " + data);
            out.close();
        } catch (Exception e) {
        }
        if (outputBuffer.size() > 0) {
            if (outputBuffer.get(0) == null) {
                outputBuffer.remove(0);
            }
        }
        outputBuffer.add(data);
    }

    public void shutdown() {
        shouldContinue = false;
    }
}
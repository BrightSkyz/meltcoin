package io.meltcoin.p2p.listeners;

import io.meltcoin.Main;
import io.meltcoin.p2p.types.Peer;

public class AddPeerListener implements MessageListener {

    @Override
    public void onMessage(Peer peer, String type, String message) {
        if (!type.equalsIgnoreCase("addpeer")) return;
        if (!Main.peers.containsKey(message)) {
            if (message.contains(":") && message.split(":").length == 2) {
                String[] splitMessage = message.split(":");
                Main.peers.put(message, new Peer(Main.peerToPeer, splitMessage[0], Integer.getInteger(splitMessage[1])));
            }
        }
    }
}

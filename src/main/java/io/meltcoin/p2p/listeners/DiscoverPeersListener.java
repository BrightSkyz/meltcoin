package io.meltcoin.p2p.listeners;

import io.meltcoin.Main;
import io.meltcoin.p2p.types.Message;
import io.meltcoin.p2p.types.Peer;

public class DiscoverPeersListener implements MessageListener {

    @Override
    public void onMessage(Peer peer, String type, String message) {
        if (!type.equalsIgnoreCase("discoverpeers")) return;
        for (Peer peer1 : Main.peers.values()) {
            peer.sendMessage(new Message("addpeer", peer1.getHost() + ":" + peer1.getPort()));
        }
    }
}

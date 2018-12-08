package io.meltcoin;

import io.meltcoin.blockchain.Block;
import io.meltcoin.blockchain.Blockchain;
import io.meltcoin.blockchain.Wallet;
import io.meltcoin.blockchain.transaction.Transaction;
import io.meltcoin.blockchain.transaction.TransactionOutput;
import io.meltcoin.p2p.PeerToPeer;
import io.meltcoin.p2p.types.Message;
import io.meltcoin.p2p.types.Peer;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.ec.ECPublicKeyImpl;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

public class Main {

    public static PeerToPeer peerToPeer = null;
    public static HashMap<String, Peer> peers = new HashMap<>();

    public static final String version = "1.0.0";

    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) throws IOException {
        // Create PeerToPeer network and run it to make sure we can connect to peers
        peerToPeer = new PeerToPeer(8426);
        peerToPeer.start();

        Peer dnsSeedPeer = new Peer(peerToPeer, "dnsseed1.meltcoin.io", 8426);
        peers.put(dnsSeedPeer.getHost() + ":" + dnsSeedPeer.getPort(), dnsSeedPeer);

        for (Peer peer : peers.values()) {
            try {
                peer.sendMessage(new Message("discoverpeers", ""));
            } catch (Exception e) {
                System.out.println("There was an error connecting to " + peer.getHost() + ":" + peer.getPort() + ". Is it offline?");
                peers.remove(peer.getHost() + ":" + peer.getPort());
            }
        }

        // Load config files
        File dataDir = new File("data");
        if (!dataDir.isDirectory()) {
            dataDir.mkdir();
        }
        // Peers config
        File peersDir = new File("data/peers");
        if (!peersDir.isDirectory()) {
            peersDir.mkdir();
        }
        // Get the peer files
        File[] peersDirFiles = peersDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".peers");
            }
        });
        // Loop over the files
        for (File peersFile: peersDirFiles) {
            FileInputStream fileInputStream = new FileInputStream(peersFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null)   {
                if (strLine.contains(":") && strLine.split(":").length == 2) {
                    String[] splitLine = strLine.split(":");
                    peers.put(splitLine[0] + ":" + splitLine[1], new Peer(peerToPeer, splitLine[0], Integer.getInteger(splitLine[1])));
                }
            }
            fileInputStream.close();
        }

        // Setup Bouncy castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        // Load wallet or create if it doesn't exist
        Wallet wallet = null;
        File walletFile = new File("data/meltcoin.wallet");
        if (!walletFile.isFile()) {
            walletFile.createNewFile();
            wallet = new Wallet();
            // TODO: Save wallet to file
        } else {
            // TODO: Load wallet from file
        }

        // TODO: Start GUI wallet/miner
    }
}

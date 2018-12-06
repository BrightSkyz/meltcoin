package io.meltcoin;

import io.meltcoin.blockchain.Block;
import io.meltcoin.blockchain.Blockchain;
import io.meltcoin.blockchain.Wallet;
import io.meltcoin.blockchain.transaction.Transaction;
import io.meltcoin.blockchain.transaction.TransactionOutput;
import io.meltcoin.p2p.PeerNetwork;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Security;

public class Main {

    public static PeerNetwork peerNetwork;

    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) throws IOException {
        // Create PeerNetwork and run it to make sure we can connect to peers
        peerNetwork = new PeerNetwork();

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
                if (strLine.contains(":")) {
                    String[] splitLine = strLine.split(":");
                    //peerNetwork.connectToPeer(splitLine[0], Integer.getInteger(splitLine[1]));
                }
            }
            fileInputStream.close();
        }

        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        // Create genesis transaction, which sends 100 Meltcoin to walletA:
        Blockchain.genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        Blockchain.genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction
        Blockchain.genesisTransaction.transactionId = "0"; //manually set the transaction id
        Blockchain.genesisTransaction.outputs.add(new TransactionOutput(Blockchain.genesisTransaction.reciepient, Blockchain.genesisTransaction.value, Blockchain.genesisTransaction.transactionId)); //manually add the Transactions Output
        Blockchain.UTXOs.put(Blockchain.genesisTransaction.outputs.get(0).id, Blockchain.genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(Blockchain.genesisTransaction);
        Blockchain.addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        Blockchain.addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        Blockchain.addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Blockchain.isChainValid();

    }
}

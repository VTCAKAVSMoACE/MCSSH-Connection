package com.VTC.mcserver;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PrintStream;

import com.VTC.mcserver.graphics.MainFrame;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;

public class Main {

	public static void main(String[] args) throws JSchException, IOException {
		new MainFrame().setVisible(true);
	}

	public static Channel setupChannel(String privateKey, String publicKey, String user, String host, int port,
			PipedInputStream input, PrintStream output) throws JSchException, IOException {
		Shell mainshell = new Shell();
		Channel chan = mainshell.connect(privateKey, publicKey, user, host, port);
		chan.setInputStream(input);
		chan.setOutputStream(output);
		return chan;
	}

	public static void generateKeypair() {
		int type = 2;
		int size = 4096;
		String filename = "id_rsa";

		JSch jsch = new JSch();

		try {
			KeyPair kpair = KeyPair.genKeyPair(jsch, type, size);
			kpair.writePrivateKey(filename);
			kpair.writePublicKey(filename + ".pub", "");
			kpair.dispose();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

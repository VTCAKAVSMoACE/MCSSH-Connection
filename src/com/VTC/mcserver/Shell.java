package com.VTC.mcserver;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.jcraft.jsch.*;

public class Shell {
	public Channel connect(String privateKey, String publicKey, String user, String host, int port) throws JSchException, IOException {
		JSch jsch = new JSch();

		if (!new File("known_hosts").exists())
			new File("known_hosts").createNewFile();
		
		jsch.setKnownHosts("known_hosts");

		jsch.addIdentity(privateKey, publicKey, new byte[0]);

		Session session = jsch.getSession(user, host, port);

		Scanner sc = new Scanner(System.in);
		
		UserInfo ui = new MyUserInfo() {
			public void showMessage(String message) {
				System.out.println(message);
			}

			public boolean promptYesNo(String message) {
				System.out.println(message);
				if (sc.nextLine().toLowerCase().equals("y"))
					return true;
				return false;
			}
		};

		sc.close();

		session.setUserInfo(ui);

		session.setConfig("StrictHostKeyChecking", "no");
		
		session.connect(30000);

		return session.openChannel("shell");
	}

	public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return null;
		}

		public boolean promptYesNo(String str) {
			return false;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public void showMessage(String message) {
		}

		public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
				boolean[] echo) {
			return null;
		}
	}
}

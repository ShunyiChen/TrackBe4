package com.maxtree.trackbe4.server;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;

public class EmbeddedFTPServer {

	public static void main(String[] args) {
		FtpServerFactory serverFactory = new FtpServerFactory();
		FtpServer server = serverFactory.createServer();
		// start the server
		try {
			server.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}

}

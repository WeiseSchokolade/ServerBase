package de.schoko.serverbase.core;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Connection {
	public Client(String ip, int port) throws UnknownHostException, IOException {
		super(new Socket(ip, port));
		System.out.println("Connected to " + ip + ":" + port);
	}

	protected Client() {
		super();
	}
}

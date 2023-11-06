package de.schoko.serverbase.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

public class Connection {
	private static boolean logPackets;
	private Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private UUID uuid;
	private boolean closed;
	
	public Connection(Socket socket) {
		this.socket = socket;
		this.uuid = UUID.randomUUID();
		try {
			this.dataInputStream = new DataInputStream(socket.getInputStream());
			this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Connection() {
		this.uuid = UUID.randomUUID();
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		closed = true;
	}
	
	public void send(String s) {
		if (logPackets) {
			System.out.println("Sending " + s);
		}
		try {
			dataOutputStream.writeUTF(s);
			dataOutputStream.flush();
		} catch (SocketException e) {
			if (!e.getMessage().equals("Connection reset by peer")) {
				e.printStackTrace();
			}
			closed = true;
		} catch (IOException e) {
			e.printStackTrace();
			closed = true;
		}
	}
	
	public String read() {
		try {
			if (dataInputStream.available() <= 0) {
				return null;
			}
			String result = dataInputStream.readUTF();
			if (logPackets) {
				System.out.println("Received " + result);
			}
			if (result == null || result.isEmpty()) {
				return null;
			} else {
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
			closed = true;
		}
		return null;
	}
	
	/**
	 * Flushes the output stream and clears all upcoming packets in the input stream
	 */
	public void clear() {
		try {
			dataOutputStream.flush();
			while (dataInputStream.available() > 0) {
				dataInputStream.readUTF();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public UUID getUID() {
		return uuid;
	}

	public DataInputStream getDataInputStream() {
		return dataInputStream;
	}
	
	public DataOutputStream getDataOutputStream() {
		return dataOutputStream;
	}
}

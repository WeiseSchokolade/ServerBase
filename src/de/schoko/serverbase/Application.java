package de.schoko.serverbase;

import java.util.List;
import java.util.Vector;

import de.schoko.serverbase.core.Connection;

public abstract class Application {
	private String name;
	protected Vector<Connection> connections;
	private boolean running;
	private boolean stoppedWhenEmpty;
	
	public Application(String name, boolean stoppedWhenEmpty) {
		this.name = name;
		this.stoppedWhenEmpty = stoppedWhenEmpty;
		this.connections = new Vector<>();
	}
	
	public void clean() {
		connections.removeIf(connection -> {
			return connection.isClosed();
		});
		if (connections.isEmpty() && stoppedWhenEmpty) running = false;
	}
	
	public abstract void update(double deltaTimeMS);
	
	public void disconnect(Connection connection) {
		
	}
	
	public void setConnections(Vector<Connection> connections) {
		this.connections = connections;
	}
	
	public void addConnection(Connection connection) {
		this.connections.add(connection);
	}
	
	public List<Connection> getConnections() {
		return connections;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void sendAll(String s) {
		for (int i = 0; i < connections.size(); i++) {
			Connection connection = connections.get(i);
			if (connection.isClosed()) {
				disconnect(connection);
				connections.remove(i);
				i--;
				continue;
			}
			connection.send(s);
		}
	}
}

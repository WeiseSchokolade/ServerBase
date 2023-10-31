package de.schoko.serverbase;

import de.schoko.serverbase.core.Connection;

public class PacketReducer {
	private double lastUpdate;
	private double updateTime;
	private Connection connection;
	
	public PacketReducer(Connection connection, long updateTime) {
		this.connection = connection;
		this.updateTime = updateTime;
	}
	
	public void send(String data, double deltaTimeMS) {
		lastUpdate += deltaTimeMS;
		if (lastUpdate >= updateTime) {
			lastUpdate -= updateTime;
			connection.send(data);
		}
		
	}
}

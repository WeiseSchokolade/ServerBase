package de.schoko.serverbase.local;

import de.schoko.serverbase.ApplicationProvider;
import de.schoko.serverbase.Server;
import de.schoko.serverbase.core.Connection;

public class LocalServer extends Server {

	public LocalServer(ApplicationProvider applicationProvider, int tps) {
		super(-1, applicationProvider, tps);
	}
	
	@Override
	public void run() {
		
	}
	
	public void addConnection(Connection connection) {
		super.addConnection(connection);
	}
}

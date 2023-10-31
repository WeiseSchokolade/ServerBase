package de.schoko.serverbase;

import de.schoko.serverbase.core.Connection;

public abstract class ApplicationProvider {
	public abstract void handleConnection(Server server, Connection connection);
}

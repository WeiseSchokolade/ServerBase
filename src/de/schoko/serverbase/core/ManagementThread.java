package de.schoko.serverbase.core;

import de.schoko.serverbase.Server;

public class ManagementThread extends Thread {
	private Server server;
	private int goalTPS;
	private long tickTime;
	private boolean running;
	
	public ManagementThread(Server server, int tps) {
		super("ManagementThread");
		this.server = server;
		this.goalTPS = tps;
		tickTime = 1000 / goalTPS;
	}
	
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		long lastTime = startTime;
		long tickAmount = 0;
		running = true;
		while (running) {
			long currentTime = System.currentTimeMillis();
			long deltaTimeMS = currentTime - lastTime;
			
			// Clear out closed threads/connections on server
			server.getApplications().removeIf(application -> {
				application.clean();
				return !application.isRunning();
			});
			server.getApplicationThreads().removeIf(thread -> {
				return !thread.getApplication().isRunning();
			});
			server.getConnections().removeIf(connection -> {
				return connection.isClosed();
			});
			server.getPendingConnections().removeIf(connection -> {
				return connection.isClosed();
			});
			
			// Ticking
			tickAmount++;
			
			currentTime = System.currentTimeMillis();
			long timeSinceStart = startTime - currentTime;
			long averageTickTime = timeSinceStart / tickAmount;
			lastTime = currentTime;
			
			if (averageTickTime <= tickTime) {
				try {
					long sleepTime = tickTime - deltaTimeMS;
					if (sleepTime > 0) {
						Thread.sleep(sleepTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Management Thread exited!");
	}
}

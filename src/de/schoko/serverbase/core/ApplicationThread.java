package de.schoko.serverbase.core;

import de.schoko.serverbase.Application;

public class ApplicationThread extends Thread {
	private Application application;
	private static int maxID;
	private int goalTPS;
	private long tickTime;
	
	public ApplicationThread(Application application, int tps) {
		super("ApplicationThread#" + application.getName() + "" + maxID++);
		this.application = application;
		this.goalTPS = tps;
		tickTime = 1000 / goalTPS;
	}
	
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		long lastTime = startTime;
		long tickAmount = 0;
		application.setRunning(true);
		while (application.isRunning()) {
			long currentTime = System.currentTimeMillis();
			long deltaTimeMS = currentTime - lastTime;
			
			application.update(deltaTimeMS);
			tickAmount++;
			
			currentTime = System.currentTimeMillis();
			long timeSinceStart = startTime - currentTime;
			long averageTickTime = timeSinceStart / tickAmount;
			lastTime = currentTime;
			
			if (averageTickTime <= tickTime) {
				try {
					long sleepTime = tickTime - deltaTimeMS;
					if (sleepTime <= 0) {
						//System.out.println("tickTime: " + tickTime + " deltaTime: " + deltaTimeMS);
					} else {
						Thread.sleep(sleepTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					application.setRunning(false);
				}
			}
		}
		System.out.println("Application Thread " + getName() + " complete");
	}
	
	public Application getApplication() {
		return application;
	}
}

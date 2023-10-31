package de.schoko.serverbase;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.function.Predicate;

import de.schoko.serverbase.core.ApplicationThread;
import de.schoko.serverbase.core.Connection;
import de.schoko.serverbase.core.ConsoleThread;
import de.schoko.serverbase.core.ManagementThread;

public class Server {
	private ServerSocket serverSocket;
	private int port;
	private ApplicationProvider applicationProvider;
	private ConsoleHandler consoleHandler;
	private ArrayList<Application> applications;
	private ArrayList<ApplicationThread> applicationThreads;
	private ArrayList<Connection> connections;
	private ArrayList<Connection> pendingConnections;
	private int tps;
	
	/**
	 * Prepares the server. Use {@link Server#run()} to run the server.
	 * @param port
	 * @param applicationProvider
	 * @param tps
	 */
	public Server(int port, ApplicationProvider applicationProvider, int tps) {
		this.port = port;
		this.applicationProvider = applicationProvider;
		this.tps = tps;
		applications = new ArrayList<>();
		applicationThreads = new ArrayList<>();
		connections = new ArrayList<>();
		pendingConnections = new ArrayList<>();
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Opened server: " + serverSocket.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (consoleHandler != null) {
			ConsoleThread consoleThread = new ConsoleThread(this, consoleHandler);
			consoleThread.start();
		}
		ManagementThread managementThread = new ManagementThread(this, tps);
		managementThread.start();
		
		
		while (true) {
			try {
				addConnection(new Connection(serverSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void addConnection(Connection connection) {
		connections.add(connection);
		applicationProvider.handleConnection(this, connection);
	}
	
	public ApplicationThread startApplication(Application application) {
		for (int i = 0; i < pendingConnections.size(); i++) {
			application.addConnection(pendingConnections.get(i));
		}
		pendingConnections.clear();
		applications.add(application);
		ApplicationThread applicationThread = new ApplicationThread(application, tps);
		applicationThread.start();
		applicationThreads.add(applicationThread);
		return applicationThread;
	}
	
	public ApplicationThread startApplication(Application application, Predicate<Connection> connectionPredicate, int minConnectionAmount) {
		int connectionAmount = 0;
		for (int i = 0; i < pendingConnections.size(); i++) {
			if (connectionPredicate.test(pendingConnections.get(i))) {
				application.addConnection(pendingConnections.get(i));
				connectionAmount++;
			}
		}
		if (connectionAmount < minConnectionAmount) {
			return null;
		}
		pendingConnections.removeIf(t -> connectionPredicate.test(t));
		ApplicationThread applicationThread = new ApplicationThread(application, tps);
		applicationThread.start();
		applicationThreads.add(applicationThread);
		return applicationThread;
	}
	
	public ArrayList<Connection> getPendingConnections() {
		return pendingConnections;
	}
	
	public void addPendingConnection(Connection connection) {
		pendingConnections.add(connection);
	}
	
	public void setConsoleHandler(ConsoleHandler consoleHandler) {
		this.consoleHandler = consoleHandler;
	}
	
	public ArrayList<ApplicationThread> getApplicationThreads() {
		return applicationThreads;
	}
	
	public ArrayList<Application> getApplications() {
		return applications;
	}
	
	public ArrayList<Connection> getConnections() {
		return connections;
	}
}

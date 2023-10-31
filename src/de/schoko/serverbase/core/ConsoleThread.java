package de.schoko.serverbase.core;

import java.util.Scanner;

import de.schoko.serverbase.ConsoleHandler;
import de.schoko.serverbase.Server;

public class ConsoleThread extends Thread {
	private Server server;
	private ConsoleHandler consoleHandler;
	private boolean running;
	
	public ConsoleThread(Server server, ConsoleHandler consoleHandler) {
		super("ConsoleThread");
		this.server = server;
		this.consoleHandler = consoleHandler;
	}
	
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		running = true;
		while (running) {
			String input = scanner.nextLine();
			try {
				consoleHandler.handle(server, input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scanner.close();
		System.out.println("Console Thread exited!");
	}
}

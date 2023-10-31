package de.schoko.serverbase.ext;

import java.util.HashMap;

import de.schoko.serverbase.ConsoleHandler;
import de.schoko.serverbase.Server;

public class ConsoleCommandManager extends ConsoleHandler {
	private HashMap<String, ConsoleCommandHandler> commands;
	
	public ConsoleCommandManager() {
		commands = new HashMap<>();
	}

	@Override
	public void handle(Server server, String input) {
		String[] split = input.trim().split(" ", 2);
		String command = split[0].trim();
		if (commands.containsKey(command)) {
			if (split.length > 1) {
				String args = split[1].trim();
				commands.get(command).handle(args);
			} else {
				commands.get(command).handle("");
			}
		} else {
			System.err.println("Unknown command: " + command);
		}
	}
	
	public void registerCommand(String command, ConsoleCommandHandler consoleCommand) {
		this.commands.put(command, consoleCommand);
	}
}

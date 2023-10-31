package de.schoko.serverbase.local;

import java.util.ArrayList;

import de.schoko.serverbase.core.Client;

public class Bridge extends Client {
	public Bridge() {
		super();
	}

	private ArrayList<String> data;
	
	@Override
	public void send(String s) {
		data.add(s);
	}
	
	@Override
	public String read() {
		return data.remove(0);
	}
}

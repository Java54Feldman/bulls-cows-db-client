package telran.net.games;

import telran.net.TcpClient;
import telran.net.games.service.BullsCowsProxy;
import telran.view.*;

import static telran.net.games.config.BullsCowsConfigurationProperties.*;

import java.util.*;

public class BullsCowsClientAppl {

	public static void main(String[] args) {
		TcpClient tcpClient = new TcpClient(HOSTNAME, PORT);
		BullsCowsProxy bullsCows = new BullsCowsProxy(tcpClient);
		List<Item> items = BullsCowsApplItems.getItems(bullsCows);
		Menu menu = new Menu("Bulls and Cows Network Game (Database version)",
				items.toArray(Item[]::new));
		menu.perform(new SystemInputOutput());

	}

}

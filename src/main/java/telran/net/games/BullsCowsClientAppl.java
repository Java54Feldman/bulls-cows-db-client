package telran.net.games;

import java.util.*;

import telran.net.TcpClient;
import telran.net.games.service.*;
import telran.view.*;
public class BullsCowsClientAppl {

	private static final int N_DIGITS = 4;

	public static void main(String[] args) {
		InputOutput inputOutput = new SystemInputOutput();
		TcpClient tcpClient = new TcpClient("localhost", 5000);
		BullsCowsTcpProxy proxy = new BullsCowsTcpProxy(tcpClient);
		List<Item> items = BullsCowsApplicationItems.getItems(proxy, N_DIGITS);
		items.add(Item.of("Exit",
				io -> tcpClient.close(), true));
		Menu menu = new Menu("Bulls Cows Logical Network Game",
				items.toArray(Item[]::new));
		
		menu.perform(inputOutput);
	}
}
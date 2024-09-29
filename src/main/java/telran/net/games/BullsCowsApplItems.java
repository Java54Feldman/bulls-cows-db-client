package telran.net.games;

import java.time.LocalDate;
import java.util.*;

import telran.net.games.model.MoveData;
import telran.net.games.service.BullsCowsService;
import telran.view.*;
import static telran.net.games.config.BullsCowsConfigurationProperties.*;

public class BullsCowsApplItems {
	static BullsCowsService bcService;
	record UserSession(String username) {

	};

	public static List<Item> getItems(BullsCowsService bcService) {
		BullsCowsApplItems.bcService = bcService;
		Item[] items = { 
				Item.of("Login gamer", BullsCowsApplItems::loginGamer),
				Item.of("Register gamer", BullsCowsApplItems::registerGamer), 
				Item.ofExit() };
		return new ArrayList<>(List.of(items));
	}

	static void loginGamer(InputOutput io) {
		String username = io.readString("Enter gamer name:");
		bcService.loginGamer(username);
		gameMenuActions(io, username, LOGIN_TITLE);

	}
	static void registerGamer(InputOutput io) {
		String username = io.readString("Enter gamer name:");
		LocalDate birthdate = io.readIsoDateRange("Enter gamer birthdate in the format yyyy-mm-dd:", "",
				LocalDate.of(1904, 1, 1), LocalDate.of(2020, 12, 31));
		bcService.registerGamer(username, birthdate);
		gameMenuActions(io, username, REGISTER_TITLE);
	}
	
	private static void gameMenuActions(InputOutput io, String username, String title) {
		UserSession userSession = new UserSession(username);
		Menu menu = new Menu(title,
				new Item[] { 
						Item.of("Create game", BullsCowsApplItems::createGame),
						Item.of("Start game", io2 -> startGame(io2, userSession.username())),
						Item.of("Continue game", io2 -> continueGame(io2, userSession.username())),
						Item.of("Join game", io2 -> joinGame(io2, userSession.username())), 
						Item.ofExit() });
		menu.perform(io);
	}

	static void createGame(InputOutput io) {
		long gameId = bcService.createGame();
		io.writeString(String.format("Game #%d created", gameId));
	}

	static void startGame(InputOutput io, String username) {
		List<Long> games = bcService.getNotStartedGamesWithGamer(username);
		List<Item> menuItems = new ArrayList<>();
		for (Long gameId : games) {
			menuItems.add(Item.of("Game #" + gameId, io2 -> {
				bcService.startGame(gameId);
				playingGame(io, username, gameId);
			}));
		}
		menuItems.add(Item.ofExit());
		Menu gameSelectionMenu = new Menu("Choose the game to start:", menuItems.toArray(Item[]::new));
		gameSelectionMenu.perform(io);
	}

	private static void playingGame(InputOutput io, String username, Long gameId) {
		Menu menu = new Menu(
				"Playing the game #" + gameId, 
				new Item[] {
						Item.of("Guess sequence" , io2 -> guessItem(io2, username, gameId)),
						Item.ofExit()
				});
		menu.perform(io);
	}
	private static void guessItem(InputOutput io, String username, Long gameId) {
		String guess = io.readStringPredicate(
				String.format("Enter %d non-repeated digits", N_DIGITS),
				"Wrong input", str -> str.chars()
				.distinct().filter(c -> c >= '0' && c <= '9')
				.count() == N_DIGITS);
		List<MoveData> history = bcService.moveProcessing(guess, gameId, username);
		history.forEach(io::writeLine);
		if (bcService.gameOver(gameId)) {
			io.writeLine("Congratulations: you are winner");
		}
	}

	static void joinGame(InputOutput io, String username) {
		List<Long> games = bcService.getNotStartedGamesWithNoGamer(username);
		List<Item> menuItems = new ArrayList<>();
		for (Long gameId : games) {
			menuItems.add(Item.of("Game #" + gameId, io2 -> {
				bcService.gamerJoinGame(gameId, username);
				io2.writeString(String.format("Gamer %s has joined the game #%d", username, gameId));
			}));
		}
		menuItems.add(Item.ofExit());
		Menu gameSelectionMenu = new Menu("Choose the game to join:", menuItems.toArray(Item[]::new));
		gameSelectionMenu.perform(io);
	}
	static void continueGame(InputOutput io, String username) {
		List<Long> games = bcService.getStartedGamesWithGamer(username);
		List<Item> menuItems = new ArrayList<>();
		for (Long gameId : games) {
			menuItems.add(Item.of("Game #" + gameId, io2 -> {
				playingGame(io, username, gameId);
			}));
		}
		menuItems.add(Item.ofExit());
		Menu gameSelectionMenu = new Menu("Choose the game to continue playing:", menuItems.toArray(Item[]::new));
		gameSelectionMenu.perform(io);

	}
}

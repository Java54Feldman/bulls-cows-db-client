package telran.net.games.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

import org.json.JSONObject;

import telran.net.*;
import telran.net.games.model.*;

public class BullsCowsProxy implements BullsCowsService {
	TcpClient tcpClient;

	public BullsCowsProxy(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}
	
	@Override
	public String loginGamer(String username) {
		return tcpClient.sendAndReceive(new Request("loginGamer", username));
	}

	@Override
	public long createGame() {
		String strRes = tcpClient.sendAndReceive(new Request("createGame", ""));
		return Long.parseLong(strRes);
	}

	@Override
	public List<String> startGame(long gameId) {
		String strRes = tcpClient.sendAndReceive(new Request("startGame", "" + gameId));
		return Stream.of(strRes.split(";")).collect(Collectors.toList());
	}

	@Override
	public void registerGamer(String username, LocalDate birthdate) {
		tcpClient.sendAndReceive(new Request("registerGamer", new GamerDto(username, birthdate).toString()));
	}

	@Override
	public void gamerJoinGame(long gameId, String username) {
		tcpClient.sendAndReceive(new Request("gamerJoinGame", new GameGamerDto(gameId, username).toString()));

	}

	@Override
	public List<Long> getNotStartedGames() {
		String strRes = tcpClient.sendAndReceive(new Request("getNotStartedGames", ""));
	    return Arrays.stream(strRes.split(";")).map(Long::parseLong).collect(Collectors.toList());
	}

	@Override
	public List<MoveData> moveProcessing(String sequence, long gameId, String username) {
		String strRes = tcpClient.sendAndReceive(new Request("moveProcessing", 
				new SequenceGameGamerDto(sequence, gameId, username).toString()));
		return Arrays.stream(strRes.split(";"))
				.map(s -> new MoveData(new JSONObject(s))).toList();
	}

	@Override
	public boolean gameOver(long gameId) {
		String strRes = tcpClient.sendAndReceive(new Request("gameOver", "" + gameId));
		return Boolean.parseBoolean(strRes);
	}

	@Override
	public List<String> getGameGamers(long gameId) {
		String strRes = tcpClient.sendAndReceive(new Request("getGameGamers", "" + gameId));
		return Stream.of(strRes.split(";")).collect(Collectors.toList());
	}

	@Override
	public List<Long> getNotStartedGamesWithGamer(String username) {
		String strRes = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithGamer", username));
		return Arrays.stream(strRes.split(";")).map(Long::parseLong).collect(Collectors.toList());
	}

	@Override
	public List<Long> getNotStartedGamesWithNoGamer(String username) {
		String strRes = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithNoGamer", username));
		return Arrays.stream(strRes.split(";")).map(Long::parseLong).collect(Collectors.toList());
	}

	@Override
	public List<Long> getStartedGamesWithGamer(String username) {
		String strRes = tcpClient.sendAndReceive(new Request("getStartedGamesWithGamer", username));
		return Arrays.stream(strRes.split(";")).map(Long::parseLong).collect(Collectors.toList());
	}

}

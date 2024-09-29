package telran.net.games.model;

import org.json.JSONObject;
import static telran.net.games.config.BullsCowsConfigurationProperties.*;

public record SequenceGameGamerDto(String sequence, Long gameId, String username) {

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(GAME_ID_FIELD, gameId);
		jsonObject.put(USERNAME_FIELD, username);
		jsonObject.put(SEQUENCE_FIELD, sequence);
		return jsonObject.toString();
	}
	public SequenceGameGamerDto(JSONObject jsonObject) {
		this(jsonObject.getString(SEQUENCE_FIELD), 
				jsonObject.getLong(GAME_ID_FIELD), 
				jsonObject.getString(USERNAME_FIELD));
	}
}

package telran.net.games.model;

import org.json.JSONObject;
import static telran.net.games.config.BullsCowsConfigurationProperties.*;

public record MoveData(String sequence, Integer bulls, Integer cows) {

	public MoveData(JSONObject jsonObject) {
		this(jsonObject.getString(SEQUENCE_FIELD),
				jsonObject.getInt(BULLS_FIELD),
				jsonObject.getInt(COWS_FIELD));
	}
	public String toString() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(SEQUENCE_FIELD, sequence);
		jsonObj.put(BULLS_FIELD, bulls);
		jsonObj.put(COWS_FIELD, cows);
		return jsonObj.toString();
	}
}

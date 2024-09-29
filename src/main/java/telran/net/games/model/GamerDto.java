package telran.net.games.model;

import java.time.LocalDate;

import org.json.JSONObject;
import static telran.net.games.config.BullsCowsConfigurationProperties.*;

public record GamerDto(String username, LocalDate birthdate) {
	
	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(USERNAME_FIELD, username);
		jsonObject.put(BIRTHDATE_FIELD, birthdate);
		return jsonObject.toString();
	}
	public GamerDto(JSONObject jsonObject) {
		this(jsonObject.getString(USERNAME_FIELD), LocalDate.parse(jsonObject.getString(BIRTHDATE_FIELD)));
	}

}

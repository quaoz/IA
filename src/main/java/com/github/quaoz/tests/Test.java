package com.github.quaoz.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.quaoz.Main;
import com.github.quaoz.managers.UserManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class Test {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		File authRequests = Main
			.getInstance()
			.getInstallDir()
			.resolve(Paths.get("data", "auth.json"))
			.toFile();

		HashMap<String, Integer> map = new HashMap<>();
		map.put(
			"first",
			UserManager.UserAuthLevels.get(UserManager.UserAuthLevels.USER)
		);
		map.put(
			"user",
			UserManager.UserAuthLevels.get(UserManager.UserAuthLevels.USER)
		);
		map.put(
			"admin",
			UserManager.UserAuthLevels.get(UserManager.UserAuthLevels.ADMIN)
		);
		map.put(
			"mod",
			UserManager.UserAuthLevels.get(UserManager.UserAuthLevels.MODERATOR)
		);

		new ObjectMapper().writeValue(authRequests, map);
		HashMap<String, UserManager.UserAuthLevels> map2 = new HashMap<>();

		new ObjectMapper()
			.readValue(authRequests, HashMap.class)
			.forEach((k, v) ->
				map2.put((String) k, UserManager.UserAuthLevels.get((int) v))
			);
		System.out.println(map2.get("user"));
	}
}
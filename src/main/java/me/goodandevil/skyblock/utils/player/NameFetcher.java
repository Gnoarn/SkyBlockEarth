package me.goodandevil.skyblock.utils.player;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import com.google.gson.Gson;

public class NameFetcher {

	public static Names[] getNames(UUID uuid) throws MalformedURLException, IOException {
		if (uuid == null) {
			return null;
		}

		Names[] names = null;

		Scanner jsonScanner = new Scanner(
				(new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replaceAll("-", "") + "/names"))
						.openConnection().getInputStream(),
				"UTF-8");
		names = new Gson().fromJson(jsonScanner.next(), Names[].class);
		jsonScanner.close();

		return names;
	}

	public class Names {

		public String name;
		public long changedToAt;

		public String getName() {
			return name;
		}

		public Date getChangeDate() {
			return new Date(changedToAt);
		}
	}
}

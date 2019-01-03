package me.goodandevil.skyblock.leaderboard.leaderheads;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import java.util.UUID;

import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.leaderboard.Leaderboard;
import me.goodandevil.skyblock.leaderboard.Leaderboard.Type;
import me.goodandevil.skyblock.visit.Visit;
import me.robin.leaderheads.api.LeaderHeadsAPI;
import me.robin.leaderheads.datacollectors.DataCollector;
import me.robin.leaderheads.objects.BoardType;

public class TopLevel extends DataCollector {

	private final SkyBlock skyblock;

	public TopLevel(SkyBlock skyblock) {
		super("toplevels", "SkyBlock", BoardType.DEFAULT, "&bTop Level", "toplevel",
				Arrays.asList(ChatColor.DARK_GRAY + "-=+=-", ChatColor.AQUA + "{name}",
						ChatColor.WHITE + "{amount} Level", ChatColor.DARK_GRAY + "-=+=-"),
				true, UUID.class);

		this.skyblock = skyblock;
	}

	@Override
	public List<Entry<?, Double>> requestAll() {
		Map<UUID, Double> topLevels = new HashMap<>();

		List<Leaderboard> leaderboards = skyblock.getLeaderboardManager().getLeaderboard(Type.Level);

		for (int i = 0; i < leaderboards.size(); i++) {
			Leaderboard leaderboard = leaderboards.get(i);
			Visit visit = leaderboard.getVisit();
			topLevels.put(visit.getOwnerUUID(), (double) visit.getLevel().getLevel());
		}

		return LeaderHeadsAPI.sortMap(topLevels);
	}
}

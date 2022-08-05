package de.keule.gh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.keule.gh.main.GHPlugin;

public class UpdateUtil {
	private final static int resourceID = 55955;

	private static void getVersion(Plugin pl, final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
			try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID)
					.openStream(); Scanner scanner = new Scanner(inputStream)) {
				if (scanner.hasNext())
					consumer.accept(scanner.next());
			} catch (IOException exception) {
				Bukkit.getConsoleSender().sendMessage("§4Cannot look for updates: §7" + exception.getMessage());
			}
		});
	}

	public static void checkForUpdate() {
		final Plugin pl = GHPlugin.getInstance();
		getVersion(pl, version -> {
			if (!pl.getDescription().getVersion().equalsIgnoreCase(version))
				GHPlugin.sendConsoleMesssage("&2There is a new update available: &7v_" + version + " (Current version: v_"
						+ pl.getDescription().getVersion() + ")");
		});
	}
}

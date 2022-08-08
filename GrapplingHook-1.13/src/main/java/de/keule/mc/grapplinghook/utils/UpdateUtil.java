package de.keule.mc.grapplinghook.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.keule.mc.grapplinghook.main.GHPlugin;

public class UpdateUtil {
	private final static int resourceID = 55955;

	private static void getVersion(Plugin pl, final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
			try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID)
					.openStream(); Scanner scanner = new Scanner(inputStream)) {
				if (scanner.hasNext())
					consumer.accept(scanner.next());
			} catch (IOException e) {
				pl.getLogger().log(Level.WARNING, "Couldn't retrieve new version info.", e);
			}
		});
	}

	public static void checkForUpdate(GHPlugin pl) {
		getVersion(pl, version -> {
			if (!pl.getDescription().getVersion().equalsIgnoreCase(version)) {

			}
			GHPlugin.println("&2There is a new update available: &7v_" + version + " (Current version: v_"
					+ pl.getDescription().getVersion() + ")");
		});
	}
}

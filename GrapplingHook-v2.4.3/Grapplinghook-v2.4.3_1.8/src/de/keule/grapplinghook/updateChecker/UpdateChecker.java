package de.keule.grapplinghook.updateChecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class UpdateChecker {

	private Plugin plugin;
	private final int resourceID = 55955;

	public UpdateChecker(Plugin pl) {
		this.plugin = pl;
	}

	public void getVersion(final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			try (InputStream inputStream = new URL(
					"https://api.spigotmc.org/legacy/update.php?resource=" + resourceID).openStream();
					Scanner scanner = new Scanner(inputStream)) {
				if (scanner.hasNext()) {
					consumer.accept(scanner.next());
				}
			} catch (IOException exception) {
				Bukkit.getConsoleSender().sendMessage("§4Cannot look for updates: §7" + exception.getMessage());
			}
		});
	}
}

package de.keule.mc.grapplinghhok.adapters.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;

public interface Glow {

	public void register(Plugin pl);

	public Enchantment get();
}
package de.keule.mc.grapplinghhok.adapters.sound;

import org.bukkit.Sound;

public class DefaultSound_v_1_8 implements DefaultSounds {

	@Override
	public Sound getPullSound() {
		return Sound.ENDERMAN_TELEPORT;
	}

	@Override
	public Sound getBreakSound() {
		return Sound.ITEM_BREAK;
	}
}
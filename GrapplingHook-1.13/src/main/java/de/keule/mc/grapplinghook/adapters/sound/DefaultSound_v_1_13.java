package de.keule.mc.grapplinghook.adapters.sound;

import org.bukkit.Sound;

import de.keule.mc.grapplinghhok.adapters.sound.DefaultSounds;

public class DefaultSound_v_1_13 implements DefaultSounds {

	@Override
	public Sound getPullSound() {
		return Sound.ENTITY_ENDERMAN_TELEPORT;
	}

	@Override
	public Sound getBreakSound() {
		return Sound.ENTITY_ITEM_BREAK;
	}
}
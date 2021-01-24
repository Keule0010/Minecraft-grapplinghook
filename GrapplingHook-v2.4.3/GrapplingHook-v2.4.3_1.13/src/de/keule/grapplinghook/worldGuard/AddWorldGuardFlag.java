package de.keule.grapplinghook.worldGuard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import de.keule.grapplinghook.main.Main;

public class AddWorldGuardFlag {
	public static StateFlag ghFlag;

	public AddWorldGuardFlag() {
		FlagRegistry registery = WorldGuard.getInstance().getFlagRegistry();

		try {
			StateFlag flag = new StateFlag("gh-pl", true);
			registery.register(flag);
			ghFlag = flag;
			System.out.println("Flag added");
		} catch (FlagConflictException e) {
			e.printStackTrace();
			Main.error = true;
		}
	}
}

For some more/latest information look on Spigotmc: https://www.spigotmc.org/resources/grappling-hook-enterhaken.55955/

# Grappling hook

A brief video made by myself: [YT-Video](https://www.youtube.com/watch?v=a4B1McFUxEQ)
<br/>
### Features:
***Customize item name<br/>
Customize Cooldown<br/>
Customize sound<br/>
Customize lore<br/>
Customize recipe (requires reload or restart)<br/>
WorldGuard flag<br/>
Simple autocomplete<br/>
Full console support<br/>
Set a limited amount of uses<br/>
No fall damage on landing (changeable in the config)<br/>
Set the gravity that works when throwing<br/>
Set the distance<br/>
You can craft the grappling hook (when you change the name or lore you have to reload or restart the server. Changing the lore or name "destroys" all old GH)<br/>
Use any fishing rod as the grappling hook or only the crafted ones(change "allRods" in the config)<br/>
Decide for each world individuell where the grappling hook is enable or enable the grappling hook for all worlds<br/>
Decide if you want to use the air as a pull-object<br/>
And more<br/>
WorldGuard:<br/>
Add the gh-pl flag to a region with the allow option to use the grapplinghook there. If you don't want to use the grapplinghook in a region, just use the deny option and to use the options from the grapplinghook plugin just use the option none.<br/>***
<br/>
### Commands ```| <permission>```:
***All commands can start with /grapplinghook or /gh
You can use all commands, if you have the permission ```grapplinghook.cmds.*``` or you are OP***

#### Player:
```
/gh help [page] | <grapplinghook.help> To see commands
/gh reload | <grapplinghook.cmd.reload> Reload the config of this plugin
/gh resetConfig | <grapplinghook.cmd.resetConfig> Restores the default values of the config
/gh allWorlds | <grapplinghook.cmd.useInAllWorlds> Enable/Disable the GH in all worlds
/gh setWorld/addWorld | <grapplinghook.cmd.setWorld> Add the world in wich you are
/gh removeWorld/rmWorld | <grapplinghook.cmd.removeWorld> Remove the world in which you are
/gh give | <grapplinghook.cmd.give> Gives you the grappling hook
/gh setPullSound [sound] | <grapplinghook.cmd.setSound> Set the pull sound of the GH
/gh setDestroySound [sound] | <grapplinghook.cmd.setSound> Set the sound of the GH when it gets destroyed
/gh setUses [Number] | <grapplinghook.cmd.setUses> Changes the maxUses in the config
/gh setGravity [Number] | <grapplinghook.cmd.setGravity> Changes the garvity
/gh setSpeed [Number] | <grapplinghook.cmd.setSpeed> Changes the speed/distance
/gh setDestroyDelay [Number] | <grapplinghook.cmd.setDestroyDelay> Changes the destroy delay(in ticks)
/gh setPrefix [String] | <grapplinghook.cmd.setPrefix> Changes the prefix of the plugin
/gh setName [String] | <grapplinghook.cmd.setName> Changes the name of the GH
/gh setCooldown [seconds] | <grapplinghook.cmd.setCooldown> Set the Cooldown of the GH
/gh addUses [Number] | <grapplinghook.cmd.addUses> Adds to the uses of the GH in your hand new uses
/gh setPlayerUses [Number] | <grapplinghook.cmd.setUses> Sets the uses of the GH in your hand
/gh useAir | <grapplinghook.cmd.useAir> Enable/Disable useAir when throwing
/gh crafting | <grapplinghook.cmd.craft> Enable/Disable crafting
/gh unlimited | <grapplinghook.cmd.unlimited> Enable/Disable unlimited uses for the GH
/gh enchanted | <grapplinghook.cmd.enchanted> Enable/Disable the glow look for the GH
/gh noFallDamage | <grapplinghook.cmd.noFallDamage> Enable/Disable falldamage when you land on the ground
/gh setDestroy | <grapplinghook.cmd.setDestroy> Enable/Disable destroying the GH when no more uses left
/gh useFloatingBlocks | <grapplinghook.cmd.useFloatingBlocks> Enable/Disable using floating blocks in the air as a pull object
/gh unbreakable | <grapplinghook.cmd.unbreakable> Enable/Disable unbreakability for the GH
/gh allRods | <grapplinghook.cmd.allRods> Enable/Disable all fishingrods as grappling hooks
```
#### Console differences:
```
/gh setWorld [worldName]
/gh removeWorld/rmWorld [worldName]
/gh give [username]
/gh addUses [PlayerName] [Number]
/gh setPlayerUses [PlayerName] [Number]
```


### Permissions: <br/>
***OP players have all permissions
You can use all commands, if you have the permission ```grapplinghook.cmds.*``` or you are OP***
```
grapplinghook.cmd.autoComplete | To use the auto complete function***
grapplinghook.craft | The player can craft the grappling hook
grapplinghook.worlds.* | The player can use the grappling hook in all worlds wich are in the list
grapplinghook.world.[worldName] | The player can use the grappling hook in the mentioned world wich the permission contains
grapplinghook.removeWorld.[worldName] | The player can't use the grappling hook in the mentioned world wich the permission contains
```

### Config:
```
%prefix% | Give you the prefix wich is specified in the config
%world% | Give you the world wich you added or removed from the list
%timeLeft% | Give you the time remaining befor you can use the grappling hook again
%newValue% | Gives you a new value
%name% | Displayes a username
%uses% | Displayes the uses left or when you have unlimited uses, it displayes the unlimitedUsesName value (can only be used in the lore)
```
***Config file:***
``` YML
Plugin:
  prefix: '&7[&6GrapplingHook&7]' #Is used in messages with the %prefix% placeholder
  name: '&6Grappling hook' #Is the name of the grappling hook when you craft it
  unlimitedUsesName: unlimited #Is used in %uses% when you have unlimited uses enabled
  unlimitedUses: false #The grappling hook has unlimited uses
  useAir: false #You can also use the air as a pull object
  allRods: false #All fishing rods act like a grappling hook, when crafting is enabled this option is false
  crafting: false #Players can craft the grappling hook
  enchanted: false #Gives the grappling hook the enchanted look
  unbreakable: false #Makes the grappling hook unbreakable
  noFallDamage: false #Disable fall damage when you land on the ground
  useInAllWorlds: false #Enabels the grappling hook in all worlds
  useFloatingBlocks: true #Uses blocks in the air as pull objects
  cancleOnEntityCatch: true #You don't get pulled to an entity
  destroyWhenNoMoreUses: false #Destroys the grappling hook when no more uses left
  maxUses: 10 #The uses the grappling hook have
  destroyDelay: 10 #In ticks 20ticks = 1 second
  cooldown: 5.0 #Cooldown of the grappling hook
  gravity: 0.35 #The higher the value, the more you are pulled to the ground
  speedMultiplier: 0.25 #The higher the value, the further and faster you get pulled
  fallDamageRemove: 1.25 #When the player is removed from the no fall damage list at the latest
  pullSound: ENTITY_ENDERMAN_TELEPORT
  destroySound: ENTITY_ITEM_BREAK
Messages:
  reload: '%prefix% &7Config reloaded!'
  successful: '%prefix% &2Operetion successful done.'
  noperm: '%prefix% &cYou have no permissions to do that.'
  unkownCMD: '%prefix% &7Unknown command or wrong syntax.'
  unkownPlayer: '%prefix% &cPlayer: &7%name%&c is unkown or offline!'
  onlyNumbers: '%prefix% &cOnly numbers!'
  addWorld: '%prefix% &2The world: &7%world%&2 has been added to the list.'
  removeWorld: '%prefix% &cThe world: &7%world% &chas been removed from the list.'
  alreadyInList: '%prefix% &7World: %world% is already in the list.'
  isNotInList: '%prefix% &7World: %world% isn''t in the list.'
  cooldown: '%prefix% &7One Moment!(%timeLeft%s)'
  valueChanged: '%prefix% &7You changed the value of &e%option%&7 to: %newValue%'
  itemNotInHand: '%prefix% &cThe player has the grapplinghook not in his hand!'

#To take affect the whole server must be reload or retsart
Recipe:
  slot1: ''
  slot2: ''
  slot3: STICK
  slot4: ''
  slot5: STICK
  slot6: STRING
  slot7: STICK
  slot8: ''
  slot9: IRON_INGOT

#The lore of the grappling hook
Lore:
- '&eYou have: &c%uses%&e uses left'

#All worlds where the grappling hook is active are in this list
Worlds:
- ''
```

### Support: <br/>
***For Support please use the discussion section here on Spigot or dm me. Please don't ask for support  in the reviewes.***
<br/>
### Version information: <br/>
Version 2.4.1 and above supports mc-version 1.8-1.16 tested<br/>
Version 2.3 and above supports mc-version 1.8-1.15 tested<br/>
Version 2.2.1 and lower supports mc-version: 1.8 tested(1.9-1.12 should work)<br/>
Video made by myself for version 2.3: [Version 2.3](https://www.youtube.com/watch?v=QrF1VMeEzIQ)<br/>
Video made by myself for version 2.0: [Version 2.0](https://www.youtube.com/watch?v=MeHDN55omFc)<br/>

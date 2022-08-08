 
# Grappling hook

## Plugin Features:​

- Create as many grappling hook variants as you like<br/>
- Support for WorldGuard regions with a custom flag<br/>
- Use any fishing rod as the grappling hook, if desired<br/>
- Craft the grappling hook<br/>
- Full console support<br/>
- Simple auto complete<br/>
- Everything changeable through in-game commands (except lore and recipe)<br/>

## Grappling Hook Features:

***All of the following features can be changed for each grappling hook individually.*** ​<br/>
- Whether to use air​<br/>
- Customize item name​<br/>
- Customize cool-down​<br/>
- Customize lore​<br/>
- Customize gravity​<br/>
- Customize distance/speed​<br/>
- Customize recipe (requires reload or restart)​<br/>
- Enable/Disable crafting​<br/>
- Give the grappling hook a enchanted look​<br/>
- Set a limited/unlimited amount of uses​<br/>
- Set custom permission​<br/>
- Set pull-/break-sound​<br/>
- No fall damage on landing​<br/>
- Whether to use floating blocks​<br/>
- Whether the grappling hook should be enabled in all worlds or only in certain selected worlds​<br/>

## WorldGuard:

***Add the flag gh-pl to a region with the allow option to allow the usage of any grappling hook in this region.<br/>
If you don't want to use the grappling hook in a region, just use the deny option.***

## Commands ``` [permission] ```:

***All commands start with /grapplinghook or /gh <br/>
Players with the permission ``` grapplinghook.cmds.* ``` or are OP can use all commands.​*** <br/>
/gh help | [grapplinghook.cmd.help] Show available commands<br/>
/gh reload | [grapplinghook.cmd.reload] Reload plugin data<br/>
/gh allRods | [grapplinghook.cmd.allRods] Use all fishing rods as grappling hooks<br/>
/gh list <Page> | [grapplinghook.cmd.list] Show existing grappling hooks<br/>
/gh info <GrapplingHookName> | [grapplinghook.cmd.info] Display information about a grappling hook<br/>
/gh get <GrapplingHookName> | [grapplinghook.cmd.get] Give you a grappling hook<br/>
/gh give <GrapplingHookName> <PlayerName> | [grapplinghook.cmd.give] Give a player a grappling hook<br/>
/gh create <GrapplingHookName> | [grapplinghook.cmd.create] Create a grappling hook<br/>
/gh remove <GrapplingHookName> | [grapplinghook.cmd.remove] Remove a grappling hook<br/>
/gh allWorlds <GrapplingHookName> <true/false> | [grapplinghook.cmd.allWorlds] Use the grappling hook in all worlds<br/>
gh addWorld <GrapplingHookName> <WorldName> | [grapplinghook.cmd.addWorld] Add a world to a grappling hook<br/>
gh addWorld <GrapplingHookName> | [grapplinghook.cmd.addWorld] Add a world to a grappling hook. The plugin will take the world the player is in.<br/>
/gh rmWorld <GrapplingHookName> <WorldName> | [grapplinghook.cmd.removeWorld] Remove a world from a grappling hook<br/>
/gh rmWorld <GrapplingHookName> | [grapplinghook.cmd.removeWorld] Remove a world from a grappling hook. The plugin will take the world the player is in.<br/>
/gh breakSound <GrapplingHookName> <BreakSound> | [grapplinghook.cmd.setSound] Set the break sound<br/>
/gh pullSound <GrapplingHookName> <PullSound> | [grapplinghook.cmd.setSound] Set pull sound<br/>
/gh cooldown <GrapplingHookName> <Seconds:Number> | [grapplinghook.cmd.cooldown] Set cool-down<br/>
/gh useAir <GrapplingHookName> <true/false> | [grapplinghook.cmd.useAir] Whether use air as a pull object<br/>
/gh crafting <GrapplingHookName> <true/false> | [grapplinghook.cmd.crafting] Whether it should be possible to craft the grappling hook<br/>
/gh glow <GrapplingHookName> <true/false> | [grapplinghook.cmd.glow] Gives the enchanted look to the grappling hook<br/>
/gh unbreakable <GrapplingHookName> <true/false> | [grapplinghook.cmd.unbreakable] Makes the grappling hook unbreakable<br/>
/gh noFallDamage <GrapplingHookName> <true/false> | [grapplinghook.cmd.noFallDamage] Whether a player receives fall damage when lading<br/>
/gh useFloatingBlocks <GrapplingHookName> <true/false> | [useFloatingBlocks] Whether the grappling hook uses floating blocks as a pull object<br/>
/gh cacnelEntityCatch <GrapplingHookName> <true/false> | [grapplinghook.cmd.cancelOnEntityCatch] Whether the grappling hook should work when fishing or catching entyties<br/>
/gh destroyNoUses <GrapplingHookName> <true/false> | [grapplinghook.cmd.destroyWhenNoMoreUses] Whether the grappling hook will be destroyed when no more uses are available<br/>
/gh maxUses <GrapplingHookName> <MaxUses> | [grapplinghook.cmd.maxUses] Set the uses of the grappling hook<br/>
/gh destroyDelay <GrapplingHookName> <Ticks:Number> | [grapplinghook.cmd.destroyDelay] Set the destroy delay<br/>
/gh gravity <GrapplingHookName> <Gravity: Decimal> | [grapplinghook.cmd.gravity] Set the gravity (high value -> player takes off less)<br/>
/gh multiplier <GrapplingHookName> <Multiplier: Decimal> | [grapplinghook.cmd.multiplier] Set the distance/speed<br/>
/gh permission <GrapplingHookName> <Permission: PlainText> | [grapplinghook.cmd.permission] Set the permission to use and craft the grappling hook<br/>
/gh displayName <GrapplingHookName> <DisplayName:Text> | [grapplinghook.cmd.displayName] Set the display name<br/>
/gh unlimitedName <GrapplingHookName> <UnlimitedName:Text> | [grapplinghook.cmd.unlimitedName] Set the unlimited name/placeholder (will be replaced with %uses% in the lore if the grappling hook has unlimited uses)<br/>
/gh unlimited <GrapplingHookName> <true/false> | [grapplinghook.cmd.unlimited] Whether the grappling hook has unlimited uses<br/>

## Permissions:​

- OP players have all permissions​<br/>
- grapplinghook.cmds.* | To be able to use all commands and tab complete​<br/>
- grapplinghook.player.default | The player can craft grappling hooks and use them in all worlds ​<br/>
- grapplinghook.tab.complete| To be able to use tab complete​<br/>
- grapplinghook.craft | The player can craft the grappling hooks​<br/>
- grapplinghook.worlds.* | The player can use grappling hooks in all worlds​<br/>
- grapplinghook.world.[worldName] | The player can use the grappling hook in the mentioned world [worldName]​<br/>
- grapplinghook.removeWorld.[worldName] | The player can't use the grappling hook in the mentioned [worldName]​<br/>

## Grappling Hook Permission:

When a grappling hook has a permission, the player will need this permission to craft and use the grappling hook. But to craft any grappling hook the player still needs the permission grapplinghook.craft or grapplinghook.player.default. The same applies for the use of the grappling hook. Players still need the permission to use the grappling hook in the world there are at the moment (grapplinghook.world.[worldName]) or for all worlds (grapplinghook.worlds.*). For example: A grappling hook has the permission "gh.unlimited" and the player has this permission as well but not the permission grapplinghook.craft, the player won't be able to craft the grappling hook.

### Remove world permission:

The permission grapplinghook.removeWorld.[worldName] has ***more*** value than grapplinghook.world.[worldName] or grapplinghook.worlds.*
For example: A player is in a group which has the permission grapplinghook.worlds.* and the player itself has the permission grapplinghook.removeWorld.lobby then the player won't be able to use the grappling hook in the world lobby.

## Configs:

### Main Config file:​

```yml
config-version: 1 #Do not change

prefix: '&7[&6GrapplingHook&7]' #The prefix of the plugin
fallDamageRemove: 10 #After how many server-ticks the player will be removed from the no fall damage list, if he didn't trigger the fall damage event while landing
allFishingRods: false #Whether all fishing rods should act as grappling hooks
```

### Grappling Hook Config:​

```yml
## Example-Config Structure ##
#<grapplingHookName>:
# options...
#
# To create a new grappling hook either use the in-game command /gh create <name> or just copy the example below,
# replace <grapplingHookName> with a unique name (only a-z 0-9 _ - are allowed, max 10 long, lower case), comment out all lines and change the settings to your liking.
# If you want that every player can use the grappling hook without giving permission to the player/group just set permission to none.
#
# If you are using all fishing rods as grappling hooks you can change the settings for that under the allrods (already exists).
#
# !!WARNINGS!!
# - CRAFTING:
# A recipe has to be unique obviously. When you change the recipe, name, lore or glow you have to reload/restart(better) the server.
# - CHANGING NAME/LORE:
# Grappling hooks which are already in the game will stob wroking after changing the display name or lore.
#
## Example-Real ##
#<grapplingHookName>:
# permission: 'none'
# displayName: '&6Grapling Hook'
# unlimitedUsesName: '&cunlimited'
# unlimitedUses: false
# glow: true
# useAir: false
# crafting: true
# allWorlds: true
# unbreakable: true
# noFallDamage: true
# useFloatingBlocks: true
# cancelOnEntityCatch: true
# destroyWhenNoMoreUses: false
# maxUses: 10
# destroyDelay: 10
# cooldown: 2
# gravity: 0.35
# throw_speed_multiplier: 0.25
# pullSound: ENTITY_ENDERMAN_TELEPORT
# breakSound: ENTITY_ITEM_BREAK
# recipe:
# slot1: ''
# slot2: ''
# slot3: Stick
# slot4: ''
# slot5: Stick
# slot6: String
# slot7: Stick
# slot8: ''
# slot9: Iron_Ingot
# lore:
# - ' '
# - '&7You have &6%uses% &7uses left'
# worlds: []

allrods:
permission: 'none'
useAir: false
allWorlds: true
unbreakable: false
noFallDamage: true
useFloatingBlocks: true
cancelOnEntityCatch: true
cooldown: 2
gravity: 0.35
throw_speed_multiplier: 0.25
worlds: []
```

### Message Config:

```yml
config-version: 1 #Do not change

reload: '%prefix% &7Config reloaded!'
noperm: '%prefix% &4You have no permissions to do that.'
unknownCMD: '%prefix% &7Unknown command.'
wrongArguments: '%prefix% &cThe arguments aren''t correct!'
usageCMD: '%prefix% &7Usage: &c%usage%'
onlyPlayers: '%prefix% &cOnly players can execute this command!'
playerIsOffline: '%prefix% &7Player %player% is &cOffline&7!'
invalidValue: '%prefix% &7The value: %value% is &cinvalid&7!'
operationSucc: '%prefix% &2Operation successfull completed!'

ghRemoved: '%prefix% &7You &cremoved &7the grappling hook: &a%ghName%&7!'
ghCreated: '%prefix% &7You &acreated &7the grappling hook: &a%ghName%&7! Edit the settings of the grappling hook in the config or with commands(/gh help).'
noGHFound: '%prefix% &cNo &7grappling hook with the name: &c%ghName% &7found!'
ghReceived: '%prefix% &7You &2obtained &7a grappling hook (%ghName%)'

helpHeader: '%prefix% &7--------- Help - %page%/%maxPages% ---------'
helpItem: '%prefix% &a%subCmd% &7 - %usage%'

listHeader: '%prefix% &7------ Grappling Hooks - %page%/%maxPages% ------'
listItem: '%prefix% &a%ghName% &7 - %displayName%'

noUsesLeft: '%prefix% &7You have &cno &7uses left!'
cooldownMsg: '%prefix% &7One Moment!(%timeLeft%s)'

allFishingRods: '%prefix% &7All fishing rods is now: &c%newValue%'

addWorld: '%prefix% &7[&a%ghName%&7] The world: &a%world%&7 has been &aadded &7to the list.'
removeWorld: '%prefix% &7[&a%ghName%&7] world: &a%world%&7 has been &cremoved &7from the list.'
alreadyInList: '%prefix% &7[&a%ghName%&7] World: &a%world%&7 is &calready &7in the list.'
isNotInList: '%prefix% &7[&a%ghName%&7] World: &a%world%&7 is &cnot &7in the list.'

allWorlds: '%prefix% &7[&a%ghName%&7] Use in all worlds is now: &c%newValue%'
setSound: '%prefix% &7[&a%ghName%&7] You set the sound to: &c%newValue%'
cooldown: '%prefix% &7[&a%ghName%&7] New cooldown: &c%newValue%s'
useAir: '%prefix% &7[&a%ghName%&7] UseAir is now: &c%newValue%'
crafting: '%prefix% &7[&a%ghName%&7] Crafting is now: &c%newValue%'
glow: '%prefix% &7[&a%ghName%&7] Glow is now: &c%newValue%'
unbreakable: '%prefix% &7[&a%ghName%&7] Unbreakable is now: &c%newValue%'
noFallDamage: '%prefix% &7[&a%ghName%&7] No fall damage is now: &c%newValue%'
useFloatingBlocks: '%prefix% &7[&a%ghName%&7] Use floating blocks is now: &c%newValue%'
cancelOnEntityCatch: '%prefix% &7[&a%ghName%&7] Cancel on entity catch is now: &c%newValue%'
destroyWhenNoMoreUses: '%prefix% &7[&a%ghName%&7] Destroy when no more uses is now: &c%newValue%'
maxUses: '%prefix% &7[&a%ghName%&7] Max uses is now: &c%newValue%'
destroyDelay: '%prefix% &7[&a%ghName%&7] Destroy delay is now: &c%newValue%'
gravity: '%prefix% &7[&a%ghName%&7] Gravity is now: &c%newValue%'
throw_speed_multiplier: '%prefix% &7[&a%ghName%&7] Throw speed multiplier is now: &c%newValue%'
permission: '%prefix% &7[&a%ghName%&7] Permission is now: &c%newValue%'
displayName: '%prefix% &7[&a%ghName%&7] DisplayName is now: &c%newValue%'
unlimitedUsesName: '%prefix% &7[&a%ghName%&7] Unlimited uses display name is now: &c%newValue%'
unlimitedUses: '%prefix% &7[&a%ghName%&7] Unlimited uses is now: &c%newValue%'
```

## Support:

***For support please use the discussion section on Spigot or dm me. Please don't ask for support in the reviews.***

## Version information:​

Version 3.0 and above support mc-version 1.8-1.19 (tested)​<br/>
Version 2.4.1 until 2.6 supports mc-version 1.8-1.16 (tested)​<br/>
Version 2.3 and above supports mc-version 1.8-1.15 (tested)​<br/>
Version 2.2.1 and lower supports mc-version: 1.8 (tested, 1.9-1.12 should work)​<br/>
Video made by myself for version 2.6: [YT-Link](https://www.youtube.com/watch?v=a4B1McFUxEQ)<br/>
Video made by myself for version 2.3: [YT-Link](https://www.youtube.com/watch?v=QrF1VMeEzIQ)<br/>
Video made by myself for version 2.0: [YT-Link](https://www.youtube.com/watch?v=MeHDN55omFc)<br/>

## TODO:
If you have any ideas please let me know

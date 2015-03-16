package me.caske33.hardcore;

import org.bukkit.command.CommandSender;

public class HardcoreOption {

	// TODO change the defaults
	public boolean grave = true; // enable graves when dead?
	public int width = 1024; // width of the map
	public int length = 1024; // length of the map
	public int duration = 0; // number of minutes of game before end scene battle starts; if 0 then it's disabled
	public int durationWarning = 5; // number of minutes before end scene battle that a warning has to be broadcasted.
	public boolean randomSpawn = false; // spawn more random? (players could spawn nearer together)
	public boolean circleWorld = true; // players can break trough glass to get to the other end of the map
	public int countdown = 30; // number of seconds to count down before beginning
	public boolean pumpkinConvert = false; // changes pumpkins to gold blocks
	public boolean worldinformation = true; // gives information about the world: ore count, size, ...

	public int lowerWorldSizeMinutes = 15; // lowers the world after specified minutes; if 0 then it's disabled
	public int lowerWorldSizeX = 16; // lowers the width with this amount
	public int lowerWorldSizeZ = 16; // lowers the Z-axis with this amount

	public int chestPlacingMinutes = 10; // tries to place a chest every X minutes
	public int chestPlacingLuck = 2; // the chest placing has a chance of 1 out of X
	// Chest loot groups
	// GROUP1: chest decoration
	// GROUP2: tools
	// GROUP3: gems and pearl
	// GROUP4: food
	// GROUP5: armor
	// GROUP6: diamonds
	public int[] chestLootItems = { 20, 7, 5, 10, 5, 5 }; // the max chest loot items of each group.
	public int[][] chestLoot = { { 30 }, { 257, 261, 267, 277, 279 }, { 265, 266, 368 }, { 260, 360, 364, 366 }, { 298, 299, 300, 301, 313 }, { 264 } };
	// chestLoot == the items in each group
	public int[] chestLootMaxAmount = { 1, 1, 4, 13, 1, 1 };
	public boolean chestWorld = false;

	public boolean waterborder = false;
	public int waterborderwidth = 100;
	public int waterborderlength = 100;

	// TODO add command support to change waterborder
	// TODO add command support to change chestLoot

	public HardcoreOption() {

	}

	public void command(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("* With this command you can change the hardcore options. [paramater] (default value)");
			sender.sendMessage("* grave [on/off]: (on) enable graves for players who died?");
			sender.sendMessage("* width [number]: (1024) width (in blocks) of the map?");
			sender.sendMessage("* length [number]: (1024)length (in blocks) of the map?");
			sender.sendMessage("* duration [number]: (0) timelength (in minutes) of the game. After the time's up, players are teleported to a random location for the end battle.");
			sender.sendMessage("* durationWarning [number]: (5) timelength (in minutes) before the end scene that the warning has to be broadcasted.");
			sender.sendMessage("* randomspawn [on/off]: (off) spawn more random? (players could spawn nearer together)");
			sender.sendMessage("* circleWorld [on/off]: (on) length (in blocks) of the map?");
			sender.sendMessage("* countdown [number]: (30) number of seconds to count down before beginning?");
			sender.sendMessage("* pumpkinconvert [on/off]: (off) changes pumpkins to gold blocks");
			sender.sendMessage("* worldinformation [on/off]: (on) gives every player information about the world at the beginning: ore count, size.");

			sender.sendMessage("* lsminutes [number]: (15) lowers the world every X minutes; if 0 then it's disabled");
			sender.sendMessage("* lsw [number]: (16) lowers the width with this amount");
			sender.sendMessage("* lsl [number]: (16) lowers the length with this amount");

			sender.sendMessage("* cpminutes [number]: (10) tries to place a chest every X minutes; if 0 then it's disabled");
			sender.sendMessage("* cpluck [number]: (1) the chest placing has a chance of 1 out of X. Eg for 50% this should be 2.");
			sender.sendMessage("* cw [on/off]: (off) This will put lootchests at y=120 in a checkerboard pattern.");

			sender.sendMessage("* wb [on/off]: (off) enables the waterborder around the hardcore game");
			sender.sendMessage("* wbw [number]: (100) sets the waterborder with (for each side)");
			sender.sendMessage("* wbl [number]: (100) sets the waterborder length (for each side)");
		} else if (args[0].equalsIgnoreCase("grave")) {
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on"))
					grave = true;
				else if (args[1].equalsIgnoreCase("off"))
					grave = false;
			}
			sender.sendMessage("Grave is " + (grave ? "on" : "off"));
		} else if (args[0].equalsIgnoreCase("width")) {
			if (args.length >= 2) {
				width = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Width is " + width + " blocks.");
		} else if (args[0].equalsIgnoreCase("length")) {
			if (args.length >= 2) {
				length = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Length is " + length + " blocks.");
		} else if (args[0].equalsIgnoreCase("duration")) {
			if (args.length >= 2) {
				duration = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Duration is " + duration + " minutes.");
		} else if (args[0].equalsIgnoreCase("durationWarning")) {
			if (args.length >= 2) {
				durationWarning = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Durationwarning is " + durationWarning + " minutes.");
		} else if (args[0].equalsIgnoreCase("randomspawn")) {
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on"))
					randomSpawn = true;
				else if (args[1].equalsIgnoreCase("off"))
					randomSpawn = false;
			}
			sender.sendMessage("Randomspawn is " + (randomSpawn ? "on" : "off"));
		} else if (args[0].equalsIgnoreCase("circleworld")) {
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on"))
					circleWorld = true;
				else if (args[1].equalsIgnoreCase("off"))
					circleWorld = false;
			}
			sender.sendMessage("Circleworld is " + (circleWorld ? "on" : "off"));
		} else if (args[0].equalsIgnoreCase("countdown")) {
			if (args.length >= 2) {
				countdown = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Countdown is " + countdown + " seconds");
		} else if (args[0].equalsIgnoreCase("pumpkinconvert")) {
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on"))
					pumpkinConvert = true;
				else if (args[1].equalsIgnoreCase("off"))
					pumpkinConvert = false;
			}
			sender.sendMessage("Pumpkinconvert is " + (pumpkinConvert ? "on" : "off"));
		} else if (args[0].equalsIgnoreCase("worldinformation")) {
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on"))
					worldinformation = true;
				else if (args[1].equalsIgnoreCase("off"))
					worldinformation = false;
			}
			sender.sendMessage("Worldinformation is " + (worldinformation ? "on" : "off"));
		} else if (args[0].equalsIgnoreCase("lsminutes")) {
			if (args.length >= 2) {
				lowerWorldSizeMinutes = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Lowerworldsizeminutes is " + lowerWorldSizeMinutes + " minutes");
		} else if (args[0].equalsIgnoreCase("lsw")) {
			if (args.length >= 2) {
				lowerWorldSizeX = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Lowerworldsizewidth is " + lowerWorldSizeX + " blocks");
		} else if (args[0].equalsIgnoreCase("lsl")) {
			if (args.length >= 2) {
				lowerWorldSizeZ = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Lowerworldsizelength is " + lowerWorldSizeZ + " blocks");
		} else if (args[0].equalsIgnoreCase("cpminutes")) {
			if (args.length >= 2) {
				chestPlacingMinutes = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Chestplacingminutes is " + chestPlacingMinutes + " minutes");
		} else if (args[0].equalsIgnoreCase("cpluck")) {
			if (args.length >= 2) {
				chestPlacingLuck = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Chestplacingluck is 1 out of " + chestPlacingLuck);
		} else if (args[0].equalsIgnoreCase("cw")) {
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on"))
					chestWorld = true;
				else if (args[1].equalsIgnoreCase("off"))
					chestWorld = false;
			}
			sender.sendMessage("Chestworld is " + (chestWorld ? "on" : "off"));
		} else if (args[0].equalsIgnoreCase("wb")) {
			if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("on"))
					waterborder = true;
				else if (args[1].equalsIgnoreCase("off"))
					waterborder = false;
			}
			sender.sendMessage("Waterborder is " + (waterborder ? "on" : "off"));
		} else if (args[0].equalsIgnoreCase("wbw")) {
			if (args.length >= 2) {
				waterborderwidth = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Waterborderwidth is " + waterborderwidth + " blocks");
		} else if (args[0].equalsIgnoreCase("wbl")) {
			if (args.length >= 2) {
				waterborderlength = Integer.parseInt(args[1]);
			}
			sender.sendMessage("Waterborderlength is " + waterborderlength + " blocks");
		} else {
			sender.sendMessage("Option not found!");
		}
	}

	public void save(String name) {
		// TODO add save code
	}

	public void load(String name) {
		// TODO add load code
	}
}

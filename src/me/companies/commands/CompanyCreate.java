package me.companies.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.companies.main.Primal;



public class CompanyCreate implements CommandExecutor{

	Primal plugin;
	public CompanyCreate(Primal instance) {
		plugin = instance;
	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static HashMap<String, Long> invitedPlayers = new HashMap<String, Long>();

	public void invitePlayer(Player player, int seconds) { //Invite the playe for a specified time in seconds
		if (seconds > 0) {       
			invitedPlayers.put(player.getName(), ((seconds * 1000) + System.currentTimeMillis()));
			player.sendMessage(ChatColor.GOLD + "You have been invited to a company!"); //You can change this as need be
			player.sendMessage(ChatColor.GRAY + "" + seconds + " seconds left to type /company join to join the company."); //Same with this message
		}       
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		if(cmd.getLabel().equalsIgnoreCase("company")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player, and in online game to use this command");
			}
			if(args.length == 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error:&c You have the incorrect usuage!"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Usage: /company help"));
			} else if (args.length == 1) {
				if(args[0].equalsIgnoreCase("create")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error:&c You have the incorrect usuage!"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Usage: /company help"));
				}else if (args[0].equalsIgnoreCase("help")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l-------[Companies]-------"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l/company create <name> - Creates Company Name"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l/company close - Closes Company"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l/company balance - Shows your Company Balance"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l/company info <name> - Shows Company Information"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l---------------------"));
				}else if (args[0].equalsIgnoreCase("balance")) {
					if (!plugin.getConfig().contains(p.getUniqueId().toString() + ".Company.Balance")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4ERROR:&c You don't own a Company!"));
					}else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Your company value is $" + plugin.getCompanyValue(p) + ""));
					}
				}else if(args[0].equalsIgnoreCase("close")) {
					if (plugin.getConfig().contains(p.getUniqueId().toString() + ".Company.Balance")) {
						plugin.closeCompany(p);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have sucessfully closed your company!"));
					}
				}else if(args[0].equalsIgnoreCase("invite")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error:&c You have the incorrect usuage!"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Usage: /company invite <name>"));
				}else if(args[0].equalsIgnoreCase("info")) {
					if (!plugin.getConfig().contains(p.getUniqueId().toString() + ".Company.Balance")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4ERROR:&c You don't own a Company!"));
					}else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l------[Company Info]------"));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCompany Name: " + plugin.getCompanyName(p)));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCompany Balance:" + plugin.getCompanyValue(p)));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNumber of Employees: " + plugin.getCompanyEmployees(p)));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNames of Employees: " + plugin.getCompanyEmployeeNames(p)));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l------[Company Info]------"));
					}
				}else if (args[0].equalsIgnoreCase("join")) {
					if (invitedPlayers.containsKey(p.getName())) { //If the player was invited at some point, check if the invitation has expired
						long inviteEnds = invitedPlayers.get(p.getName());
						if (inviteEnds >= System.currentTimeMillis()) { //If the invitation is still valid, let him join the game
							if(plugin.getConfig().contains(p.getUniqueId().toString() + ".Company.Employees.Names")); {
								Bukkit.broadcastMessage("you're already in a company");
							}
						}else { //If the invitation has expired, tell the player and remove him from the invitation list
							invitedPlayers.remove(p.getName());
							p.sendMessage(ChatColor.RED + "Your invitation to join the company expired!");
							p.sendMessage(ChatColor.YELLOW + "You'll need to get invited again to join the company!");
						}
					} else { //If the player hasn't ever received an invite or the last one expired and was removed, tell him
						p.sendMessage(ChatColor.RED + "You need to receive an invitation before you can join a company");
					}
				}
			}else if (args.length == 2) {
				if(args[1].equalsIgnoreCase("add")){
					if (!plugin.getConfig().contains(p.getUniqueId().toString() + ".Company.Name")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4ERROR:&c You don't own a Company!"));
					}else {
						return true;
					}
					return true;
				}
				Player target = (Player) Bukkit.getPlayerExact(args[1]);
				if(args[1].equals(target.getName())){
					if(args[1] == null) {
						p.sendMessage(ChatColor.RED  + "Target:" + args[1] + " is not online!");
						return true;
					}
					if (!plugin.getConfig().contains(p.getUniqueId().toString() + ".Company.Name")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4ERROR:&c You don't own a Company!"));
						return true;
					}
					this.invitePlayer(target, 15);
					return true;
				}else {
					Bukkit.broadcastMessage("test null?");
				}

				if(isInt(args[1]) == true) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "Your Company Must be a name. Numeric vaules are NOT Allowed!"));
					return true;
				}else {
					plugin.createCompany(p, args[1]);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have created a Company!"));
					return true;

				}
			}else if(args.length == 3) {
				if(isInt(args[2]) == true) {
					if (!plugin.getConfig().contains(p.getUniqueId().toString() + ".Company.Name")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4ERROR:&c You don't own a Company!"));
						return true;
					}
					if(plugin.economy.getBalance(p) >= Integer.parseInt(args[2])){
						plugin.economy.withdrawPlayer(p,Integer.parseInt(args[2]));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[Companies] &3You have deposited &a$" + args[2] + " &3Into your company balance!"));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYour Account balance is now: $" + plugin.economy.getBalance(p)));
						plugin.addMoney(p, Integer.parseInt(args[2]));
					}else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4ERROR:&c You don't have enough money to put that into your Company Balance"));
					}
				}else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "You must add money using numbers!"));
					return true;
				}
			}
		}
		return false;
	}
}
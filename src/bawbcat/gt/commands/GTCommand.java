package bawbcat.gt.commands;

import bawbcat.gt.GroupTracker;
import bawbcat.gt.Main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GTCommand extends Command {

    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        List<Command> commands = new ArrayList<Command>();
        if (bot.containsPhrase(message, "help")) {
            bot.sendNotice(sender, "GroupTracker Documentation: http://pastebin.com/MhHiVavf");
            return true;
        }
        if (bot.containsPhrase(message, "who made")) {
            bot.sendMessage(channel, Colors.BOLD + "BawbCat" + Colors.NORMAL + " made me, " + Colors.DARK_BLUE + sender + Colors.NORMAL + ". I was modified slightly by " + Colors.BOLD + "Judge.");
            return true;
        }
        if (bot.contains(message, "version")) {
            bot.sendNotice(sender, Colors.DARK_GREEN + Colors.BOLD + "GroupTracker" + Colors.NORMAL + " is currently version " + Colors.BROWN + Colors.BOLD + bot.version + Colors.NORMAL + ".");
            return true;
        }
        if (bot.contains(message, "districts")) {
            String response = "";
            List<String> districts = new ArrayList<String>(Arrays.asList(bot.districts));
            Collections.sort(districts);
            for (String s : districts) {
                response = response + Colors.DARK_GREEN + Colors.BOLD + s + Colors.NORMAL;
                if (districts.get(districts.size() - 1).equals(s)) {
                    response = response + ".";
                } else if (districts.get(districts.size() - 2).equals(s)) {
                    response = response + ", and ";
                } else {
                    response = response + ", ";
                }
            }
            bot.sendNotice(sender, response);
            return true;
        }
        if (bot.contains(message, "pls")) {
            bot.sendMessage(channel, sender + Colors.NORMAL + " pls");
            return true;
        }
        if (bot.contains(message, "enable")) {
            if (bot.ops.contains(sender)) {
                bot.sendNotice(sender, Colors.PURPLE + Colors.BOLD + "GroupTracker" + Colors.NORMAL + " is already " + Colors.DARK_GREEN + Colors.BOLD + "enabled" + Colors.NORMAL + "!");
            }
        }
        if (bot.contains(message, "disable") || bot.contains(message, "toggle")) {
            if (bot.ops.contains(sender)) {
                bot.enabled = false;
                bot.sendNotice(sender, Colors.PURPLE + Colors.BOLD + "GroupTracker" + Colors.NORMAL + " is now " + Colors.DARK_GRAY + Colors.BOLD + "disabled" + Colors.NORMAL + ".");
            }
        }
        if (bot.containsPhrase(message, "reset groups")) {
            if (bot.ops.contains(sender)) {
                bot.groups.clear();
                bot.sendNotice(sender, "The groups list has been cleared.");
                return true;
            }
        }
        if (bot.contains(message, "update")) {
            if (bot.ops.contains(sender)) {
                try {
                    Runtime.getRuntime().exec("java -jar GroupTrackerUpdate.jar");
                    bot.saveGroups();
                    for (String s : Main.bot.channels) {
                        bot.partChannel(s, "Updating! I'll be right back!");
                    }
                    System.exit(0);
                } catch (IOException ex) {
                }
            }
        }
        if (bot.contains(message, "reset")) {
            if (bot.ops.contains(sender)) {
                try {
                    Runtime.getRuntime().exec("java -jar bot.jar");
                    bot.saveGroups();
                    for (String s : bot.channels) {
                        bot.partChannel(s, "Resetting! Be right back!");
                    }
                    System.exit(0);
                } catch (IOException iOException) {
                }
            }
        }       
        for (Command c : commands) {
            if (bot.runCommand(c, message, sender, channel)) {
                return true;
            }
        }
        return false;
    }
}

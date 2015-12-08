package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GroupCommand extends Command {
    
    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        List<Command> commands = new ArrayList<Command>();
        if (bot.contains(message, new String[]{"make", "create", "new", "start", "add"}) != null) {
            commands.add(new GroupCreateCommand());
        }
        if (bot.contains(message, new String[]{"remove", "end", "rip", "destroy", "delete", "cancel", "kill", "murder"}) != null) {
            commands.add(new GroupRemoveCommand());
        }
        if (bot.contains(message, "set") && bot.containsPhrase(message, bot.districts, true) != null) {
            commands.add(new GroupChangeDistrictCommand());
        }
        if (bot.contains(message, new String[]{"add", "subtract", "minus", "set"}) != null) {
            commands.add(new GroupPlayerCountChangeCommand());
        }
        if (bot.contains(message, "info")) {
            commands.add(new GroupInfoCommand());
        }
        if (bot.contains(message, "setinfo")) {
            Group g = Group.getGroup(sender);
            if (g != null) {
                if (message.length() > 14) {
                    g.extraInfo = message.substring(message.indexOf("setinfo") + 8);
                    bot.sendNotice(sender, Colors.DARK_BLUE + "Group info set.");
                } else {
                    g.extraInfo = null;
                    bot.sendNotice(sender, Colors.DARK_BLUE + "Group info removed.");
                }
                return true;
            }
        }
//        if (bot.contains(message, "notify")) {
//            if (message.length() > 13) {
//                //TODO Set notice
//            } else {
//                //TODO clear notice
//            }
//        }
        if (bot.contains(message, "join")) {
            commands.add(new GroupJoinCommand());
        }
        if (bot.contains(message, "leave")) {
            commands.add(new GroupLeaveCommand());
        }
        for (Command c : commands) {
            if (bot.runCommand(c, message, sender, channel)) {
                return true;
            }
        }
        return false;
    }
}

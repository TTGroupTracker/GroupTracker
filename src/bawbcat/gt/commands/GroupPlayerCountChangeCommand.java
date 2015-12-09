package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import bawbcat.gt.GroupType;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GroupPlayerCountChangeCommand extends Command {

    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        List<String> s = bot.contains(message, new String[]{"add", "subtract", "minus", "set"});
        if (s != null) {
            String mathType = s.get(0);
            Group g = Group.getGroup(sender);
            if (g != null) {
                s = bot.contains(message, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
                if (s != null) {
                    if (g.type != GroupType.BEAN_FEST) {
                        int number = Integer.parseInt(s.get(0));
                        if (mathType.equalsIgnoreCase("add")) {
                            g.players += number;
                            if (g.players > g.maxPlayers) {
                                g.players = g.maxPlayers;
                            }
                        } else if (mathType.equalsIgnoreCase("subtract") || mathType.equalsIgnoreCase("minus")) {
                            g.players -= number;
                            if (g.players < g.nicksComing.size()) {
                                g.players = g.nicksComing.size();
                            }
                        } else if (mathType.equalsIgnoreCase("set")) {
                            g.players = number;
                            if (g.players > g.maxPlayers) {
                                g.players = g.maxPlayers;
                            } else if (g.players < g.nicksComing.size()) {
                                g.players = g.nicksComing.size();
                            }
                        }
                        bot.sendNotice(sender, "Your " + g.getTypeString() + " now has " + Colors.NORMAL + g.players + "/" + g.maxPlayers + Colors.NORMAL + " players.");
                    } else {
                        bot.sendNotice(sender, "It doesn't really make much sense to be tracking the amount of players at a Bean Fest, does it?");
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

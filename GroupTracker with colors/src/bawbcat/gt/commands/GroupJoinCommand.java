package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import bawbcat.gt.GroupType;
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GroupJoinCommand extends Command {
    
    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        List<String> validUsers = new ArrayList<String>();
        for (Group g : bot.groups) {
            validUsers.add(g.owner);
        }
        List<String> s = bot.contains(message, validUsers.toArray());
        if (s != null) {
            Group g = Group.getGroup(s.get(0));
            if (g.type != GroupType.BEAN_FEST) {
                if (g.players < g.maxPlayers) {
                    if (!sender.equals(g.owner) && !g.nicksComing.contains(sender)) {
                        g.nicksComing.add(sender);
                        g.players++;
                        bot.sendNotice(sender, "You have joined " + Colors.DARK_BLUE + g.owner + "'s " + Colors.NORMAL + g.getTypeString() + ". You'll be meeting" + g.getLocationString() + ".");
                        bot.sendNotice(g.owner, Colors.DARK_BLUE + sender + Colors.NORMAL + " has joined your " + g.getTypeString() + "." + " Your group now has " + Colors.BOLD + g.players + "/" + g.maxPlayers + Colors.NORMAL + " players.");
                    } else {
                        bot.sendNotice(sender, "You're already in that group.");
                    }
                } else {
                    bot.sendNotice(sender, Colors.DARK_BLUE + g.owner + "'s " + Colors.NORMAL + g.getTypeString() + " is full.");
                }
            } else {
                bot.sendNotice(sender, "One does not simply 'join' a Bean Fest; one simply goes there.");
            }
        }
        return true;
    }
}

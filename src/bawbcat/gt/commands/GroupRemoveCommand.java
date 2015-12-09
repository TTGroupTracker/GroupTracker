package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GroupRemoveCommand extends Command {
    
    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        Group removedGroup;
        String groupOwner;
        List<String> people = new ArrayList<String>();
        for (Group g : bot.groups) {
            people.add(g.owner);
        }
        List<String> s = bot.contains(message, people.toArray());
        if (!bot.ops.contains(sender) || s == null) {
            groupOwner = sender;
        } else {
            groupOwner = s.get(0);
        }
        removedGroup = Group.getGroup(groupOwner);
        if (removedGroup != null) {
            bot.groups.remove(removedGroup);
            if (groupOwner.equals(sender)) {
                bot.sendNotice(sender, "Your " + removedGroup.getTypeString() + " group has been terminated.");
            } else {
                bot.sendNotice(sender, Colors.DARK_BLUE + groupOwner + "'s " + Colors.NORMAL + removedGroup.getTypeString() + " has been terminated.");
                bot.sendNotice(groupOwner, "Your " + removedGroup.getTypeString() + " has been forcefully terminated by " + Colors.BROWN + sender + Colors.NORMAL + ".");
            }
            for (String s2 : removedGroup.nicksComing) {
                if (!s2.equals(removedGroup.owner)) {
                    bot.sendNotice(s2, "The group you were in (" + Colors.DARK_BLUE + groupOwner + "'s " + Colors.NORMAL + removedGroup.getTypeString() + ") has been disbanded.");
                }
            }
        } else {
            if (groupOwner.equals(sender)) {
                bot.sendNotice(sender, "You don't seem to have a group!");
            } else {
                bot.sendNotice(sender, Colors.DARK_BLUE + groupOwner + Colors.NORMAL + " doesn't seem to have a group!");
            }
        }
        return true;
    }
}

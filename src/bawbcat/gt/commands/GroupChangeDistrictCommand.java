package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.User;

public class GroupChangeDistrictCommand extends Command {

    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        String groupOwner;
        List<String> people = new ArrayList<String>();
        for (String c : bot.getChannels()) {
            for (User u : bot.getUsers(c)) {
                if (!people.contains(u.getNick())) {
                    people.add(u.getNick());
                }
            }
        }
        List<String> s = bot.contains(message, people.toArray());
        if (!bot.ops.contains(sender) || s == null) {
            groupOwner = sender;
        } else {
            groupOwner = s.get(0);
        }
        Group g = Group.getGroup(groupOwner);
        s = bot.containsPhrase(message, bot.districts, true);
        if (s != null) {
            String newDistrict = s.get(0);
            if (!g.district.equals(newDistrict)) {
                g.district = newDistrict;
                if (sender.equals(groupOwner)) {
                    bot.sendNotice(sender, "Your " + g.getTypeString() + " is now located in " + Colors.DARK_GREEN + Colors.BOLD + newDistrict + Colors.NORMAL + ".");
                } else {
                    bot.sendNotice(sender, Colors.BLUE + groupOwner + Colors.NORMAL + "'s " + g.getTypeString() + " is now located in " + Colors.DARK_GREEN + Colors.BOLD + newDistrict + Colors.NORMAL + ".");
                    bot.sendNotice(groupOwner, Colors.BLUE + sender + Colors.NORMAL + " has moved your " + g.getTypeString() + " to " + Colors.DARK_GREEN + Colors.BOLD + newDistrict + Colors.NORMAL + ".");
                }
            } else {
                if (sender.equals(groupOwner)) {
                    bot.sendNotice(sender, "Your " + g.getTypeString() + " is already in " + Colors.DARK_GREEN + Colors.BOLD + newDistrict + Colors.NORMAL + ".");
                } else {
                    bot.sendNotice(sender, Colors.BLUE + groupOwner + Colors.NORMAL + "'s " + g.getTypeString() + " is already in " + Colors.DARK_GREEN + Colors.BOLD + newDistrict + Colors.NORMAL + ".");
                }
            }
            return true;
        } else {
            bot.sendNotice(sender, "You don't seem to have a group!.");
        }
        return false;
    }
}

package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import org.jibble.pircbot.Colors;

public class GroupLeaveCommand extends Command {
  
    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        for (Group g : bot.groups) {
            if (g.nicksComing.contains(sender)) {
                if (!sender.equals(g.owner)) {
                        g.nicksComing.remove(sender);
                        g.players--;
                        bot.sendNotice(sender, "You have left " + Colors.DARK_BLUE + g.owner + "'s " + Colors.NORMAL + g.getTypeString() + ".");
                        bot.sendNotice(g.owner, Colors.DARK_BLUE + sender + Colors.NORMAL + " has left your " + g.getTypeString() + "." + " Your group now has " + Colors.BOLD + g.players + "/" + g.maxPlayers + Colors.NORMAL + " players.");  
                } else {
                    bot.sendNotice(sender, "You can't leave your own group! To disband it, say '" + Colors.BOLD + "delete group" + Colors.NORMAL + "'.");
                }
                return true;
            }
        }
        bot.sendNotice(sender, "You're not in a group!");
        return true;
    }
}
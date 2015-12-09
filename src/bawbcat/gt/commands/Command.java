package bawbcat.gt.commands;

import bawbcat.gt.GroupTracker;

public class Command {
    
    public boolean opOnly = false;

    public boolean run(String message, String sender, String channel, GroupTracker bot) {
        if (bot.ops.contains(sender) || !opOnly) {
            return action(message, sender, channel, bot);
        } else {
            bot.sendNotice(sender, "You must be an OP to do that.");
            return false;
        }
    }

    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        return false;
    }
}

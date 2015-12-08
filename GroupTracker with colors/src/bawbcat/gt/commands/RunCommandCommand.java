package bawbcat.gt.commands;

import bawbcat.gt.GroupTracker;
import org.jibble.pircbot.Colors;

public class RunCommandCommand extends Command {
    
    public RunCommandCommand() {
        opOnly = true;
    }

    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        String command = message.replace(bot.getInstanceOf(message, "runcommand"), "");
        bot.sendRawLine(command);
        bot.sendNotice(sender, "Ran command '" + Colors.BOLD + command + "'");
        return true;
    }
}

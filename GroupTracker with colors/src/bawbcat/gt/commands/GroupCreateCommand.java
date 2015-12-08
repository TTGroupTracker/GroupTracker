package bawbcat.gt.commands;

import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import bawbcat.gt.GroupType;
import bawbcat.gt.Modifier;
import bawbcat.gt.Playground;
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GroupCreateCommand extends Command {

    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        List<String> s = bot.containsPhrase(message, bot.groupWords);
        if (s != null) {
            String typeInstance = bot.getInstanceOf(message, s.get(0));
            if (typeInstance != null) {
                s = bot.containsPhrase(message.replace(typeInstance, ""), bot.districts, true);
            } else {
                s = bot.containsPhrase(message, bot.districts, true);
            }
            if (s != null) {
                String district = s.get(0);
                for (String s2 : bot.getWords(district)) {
                    String instance = bot.getInstanceOf(message, s2);
                    if (instance != null) {
                        message = message.replaceFirst(instance, "");
                    }
                }
                s = bot.containsPhrase(message, bot.groupWords);
                if (s != null) {
                    String groupType = s.get(0);
                    GroupType type = GroupType.toGroupType(groupType);
                    Group g = new Group(sender, district, type);
                    for (Modifier[] array : g.validModifiers) {
                        for (Modifier m : array) {
                            if (bot.containsPhrase(message, m.searchText.toArray()) != null) {
                                g.modifiers.add(m);
                                break;
                            }
                        }
                    }
                    List<Modifier> modsToDelete = new ArrayList<Modifier>();
                    for (Modifier m : g.modifiers) {
                        boolean deleteMod = true;
                        if (m.requiredMod != null) {
                            for (Modifier m2 : g.modifiers) {
                                if (m2.searchText.contains(m.requiredMod)) {
                                    deleteMod = false;
                                    break;
                                }
                            }
                        } else {
                            deleteMod = false;
                        }
                        if (deleteMod) {
                            modsToDelete.add(m);
                        }
                    }
                    for (Modifier m : modsToDelete) {
                        g.modifiers.remove(m);
                    }
                    if (type.needsStreet) {
                        s = bot.containsPhrase(message, Playground.getAllStreets().toArray(), true);
                        if (s != null) {
                            g.street = s.get(0);
                        } else {
                            return false;
                        }
                    }
                    if (type.needsPlayground) {
                        s = bot.containsPhrase(message, Playground.getAllSearchText().toArray(), false);
                        if (s != null) {
                            g.playground = Playground.getPlayground(s.get(0));
                        } else {
                            return false;
                        }
                    }
                    if (g.modifiers.size() >= g.getMinimumNeededMods()) {
                        Group removedGroup = Group.getGroup(sender);
                        if (removedGroup != null) {
                            bot.groups.remove(removedGroup);
                            bot.sendNotice(sender, "Since you can only have one group at a time, your " + removedGroup.getTypeString() + " has been deleted.");
                        }
                        bot.groups.add(g);
                        bot.sendNotice(sender, "Your " + g.getTypeString() + Colors.NORMAL + " group" + g.getLocationString() + " has been created. Good luck!");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

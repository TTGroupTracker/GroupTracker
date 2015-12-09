package bawbcat.gt.commands;


import bawbcat.gt.Group;
import bawbcat.gt.GroupTracker;
import bawbcat.gt.GroupType;
import bawbcat.gt.Modifier;
import bawbcat.gt.Playground;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jibble.pircbot.Colors;

public class GroupSearchCommand extends Command {

    @Override
    public boolean action(String message, String sender, String channel, GroupTracker bot) {
        List<String> s = bot.containsPhrase(message, bot.groupWords);
        List<Group> searchResults = new ArrayList<Group>(bot.groups);
        boolean allGroups = true;
        if (s != null || bot.contains(message, "all") || bot.contains(message, "any") || bot.containsPhrase(message, bot.districts, true) != null || bot.containsPhrase(message, Group.getAllModifiers().toArray(), true) != null || bot.containsPhrase(message, Playground.getAllSearchText().toArray()) != null || bot.containsPhrase(message, Playground.getAllStreets().toArray(), true) != null) {
            String groupType = null;
            GroupType searchType = null;
            if (s != null) {
                allGroups = false;
                groupType = bot.getInstanceOf(message, s.get(0));
                searchType = GroupType.toGroupType(groupType);
                searchResults = Group.getGroupsOfType(searchResults, searchType);
                for (Modifier[] array : new Group("", "", searchType).validModifiers) {
                    List<Modifier> modsFound = new ArrayList<Modifier>();
                    for (Modifier m : array) {
                        if (bot.containsPhrase(message, m.searchText.toArray()) != null) {
                            modsFound.add(m);
                        }
                    }
                    for (Modifier m : modsFound) {
                        System.out.println("Processing mod " + m.display);
                        List<Modifier> mod = new ArrayList<Modifier>();
                        mod.add(m);
                        searchResults = Group.getGroupsWithMods(searchResults, mod);
                    }
                }
                
            }
            if (searchType != null) {
                s = bot.containsPhrase(message.replace(groupType, ""), bot.districts, true);
            } else {
                s = bot.containsPhrase(message, bot.districts, true);
            }
            if (s != null) {
                allGroups = false;
                searchResults = Group.getGroupsOfDistrict(searchResults, s.get(0));
            }
            s = bot.containsPhrase(message, Playground.getAllSearchText().toArray());
            if (s != null) {
                allGroups = false;
                searchResults = Group.getGroupsInPlayground(searchResults, Playground.getPlayground(s.get(0)));
            } else {
                s = bot.containsPhrase(message, Playground.getAllStreets().toArray(), true);
                if (s != null) {
                    allGroups = false;
                    searchResults = Group.getGroupsOnStreet(searchResults, s.get(0));
                }
            }
            searchResults = Group.getGroupsWithRoom(searchResults);
            if (!searchResults.isEmpty()) {
                int antiSpamLimit = 2;
                int searchSize = searchResults.size();
                String response;      
                if (searchSize > antiSpamLimit) { //AntiSpam search
                    response = Colors.NORMAL + sender + Colors.NORMAL + ", there ";
                    HashMap<GroupType, Integer> typeAmounts = new HashMap<GroupType, Integer>();
                    List<GroupType> uniqueTypes = new ArrayList<GroupType>();
                    for (Group g : searchResults) {
                        if (!uniqueTypes.contains(g.type)) {
                            uniqueTypes.add(g.type);
                        }
                    }
                    for (GroupType t : uniqueTypes) {
                        List<Group> groupsOfType = new ArrayList<Group>();
                        for (Group g : bot.groups) {
                            if (t == g.type) {
                                groupsOfType.add(g);
                            }
                        }
                        typeAmounts.put(t, groupsOfType.size());
                        if (groupsOfType.size() > 1) {
                            if (allGroups) {
                                for (Group g2 : groupsOfType) {
                                    searchResults.remove(g2);
                                }
                                searchResults.add(new Group("", "", t));
                            }
                        }
                    }
                    searchSize = searchResults.size();
                    boolean gaveGroupAmount = false;
                    boolean gaveGroupOwner = false;
                    while (!searchResults.isEmpty()) {                    
                        Group g = searchResults.get(0);                        
                        if (searchResults.size() == searchSize) {
                            System.out.println(typeAmounts.get(g.type));
                            response = response + (typeAmounts.get(g.type) > 1 ? "are " : "is ");
                        }
                        if (typeAmounts.get(g.type) > 1 && allGroups) {
                            response = response + typeAmounts.get(g.type) + " " + Colors.NORMAL + Colors.NORMAL + g.type.display + Colors.NORMAL + " groups";
                            gaveGroupAmount = true;
                        } else {
                            response = response + "a " + g.getTypeString() + " group owned by " + Colors.NORMAL + Colors.NORMAL + g.owner + Colors.NORMAL;
                            gaveGroupOwner = true;
                        }
                               
                        if (searchResults.get(searchResults.size() - 1) == g) {
                            response = response + ". For more information, say" + (gaveGroupAmount ? " '" + Colors.NORMAL + "<group type> groups" + Colors.NORMAL + "'" : "") + (gaveGroupAmount && !gaveGroupOwner ? "." : " ") + (gaveGroupAmount && gaveGroupOwner ? "or " : "") + (gaveGroupOwner ? "'" + Colors.NORMAL + "<group owner> group info" + Colors.NORMAL + "'." : "");
                        } else if (searchResults.get(searchResults.size() - 2) == g) {
                            if (searchSize == 2) {
                                response = response + " and ";
                            } else {
                                response = response + ", and ";
                            }
                        } else {
                            response = response + ", ";
                        }

                        searchResults.remove(g);
                    }
                    if (allGroups) {
                        bot.sendMessage(channel, response);
                    } else {
                        bot.sendNotice(sender, response);
                    }
                } else { //Normal Search                                
                    response = Colors.NORMAL + sender + Colors.NORMAL + ", there is ";
                    while (!searchResults.isEmpty()) {
                        Group g = searchResults.get(0);
                        response = response + "a " + g.getTypeString() + " group" + g.getLocationString() + g.getInfoString();
                        if (searchResults.get(searchResults.size() - 1) == g) {
                            response = response + ".";
                        } else if (searchResults.get(searchResults.size() - 2) == g) {
                            if (searchSize == 2) {
                                response = response + " and ";
                            } else {
                                response = response + ", and ";
                            }
                        } else {
                            response = response + ", ";
                        }
                        searchResults.remove(g);
                    }
                    bot.sendMessage(channel, response);
                }
            } else {
                if (searchType != null) {
                    Group fakeGroup = new Group("", "", searchType);                   
                    for (Modifier[] mArray : fakeGroup.validModifiers) {
                        for (Modifier m : mArray) {
                            boolean gotMod = false;
                            for (String s2 : m.searchText) {
                                if (bot.containsPhrase(message, s2)) {
                                    fakeGroup.modifiers.add(m);
                                    gotMod = true;
                                    break;
                                }
                            }
                            if (gotMod) {
                                break;
                            }
                        }
                    }
                    
                    bot.sendMessage(channel, Colors.NORMAL + sender + Colors.NORMAL + ", there aren't any " + fakeGroup.getTypeString() + " groups currently.");
                } else {
                    bot.sendMessage(channel, Colors.NORMAL + sender + Colors.NORMAL + ", there aren't any groups currently.");
                }
            }
            return true;
        }
        return false;
    }
}

package bawbcat.gt;

import bawbcat.gt.Modifier.ModifierPos;
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.Colors;

public class Group {
    
    public GroupType type;
    public String owner;
    public String district;
    public Playground playground;
    public String street;
    public String extraInfo = null;
    public List<Modifier> modifiers = new ArrayList<Modifier>();
    public List<Modifier[]> validModifiers = new ArrayList<Modifier[]>();
    public int players = 1;
    public int maxPlayers = 4;
    public int timeInactive = 0;
    public List<String> nicksComing = new ArrayList<String>();
    public static List<Modifier[]> gagTrainingMods = new ArrayList<Modifier[]>();
    public static List<Modifier[]> buildingMods = new ArrayList<Modifier[]>();
    public static List<Modifier[]> fieldOfficeMods = new ArrayList<Modifier[]>();
    public static List<Modifier[]> factoryMods = new ArrayList<Modifier[]>();
    public static List<Modifier[]> mintMods = new ArrayList<Modifier[]>();
    public static List<Modifier[]> daOfficeMods = new ArrayList<Modifier[]>();
    public static List<Modifier[]> cogGolfMods = new ArrayList<Modifier[]>();
    public static List<Modifier[]> bossMods = new ArrayList<Modifier[]>();

    public Group(String owner, String district, GroupType type) {
        this.owner = owner;
        this.district = district;
        this.type = type;
        if (type == GroupType.GAG_TRAINING) {
            validModifiers = gagTrainingMods;
        } else if (type == GroupType.BUILDING) {
            validModifiers = buildingMods;
        } else if (type == GroupType.FIELD_OFFICE) {
            validModifiers = fieldOfficeMods;
        } else if (type == GroupType.FACTORY) {
            validModifiers = factoryMods;
        } else if (type == GroupType.MINT) {
            validModifiers = mintMods;
        } else if (type == GroupType.DA_OFFICE) {
            validModifiers = daOfficeMods;
        } else if (type == GroupType.COG_GOLF) {
            validModifiers = cogGolfMods;
        } else if (type == GroupType.BOSS) {
            validModifiers = bossMods;
            maxPlayers = 8;
        }
        nicksComing.add(owner);
    }
    
    public int getMinimumNeededMods() {
        int needed = 0;
        for (Modifier[] array : validModifiers) {
            for (Modifier m : array) {
                if (m.needed) {
                    needed++;
                    break;
                }
            }
        }
        return needed;
    }
    
    public static List<Modifier> getAllModifiers() {
        List<Modifier> mods = new ArrayList<Modifier>();     
        for (GroupType t : GroupType.getAllGroupTypes()) {
            for (Modifier[] array : new Group("", "", t).validModifiers) {
                for (Modifier m : array) {
                    mods.add(m);
                }
            }
        }
        return mods;
    }
    
    public static List<Group> getGroupsWithRoom(List<Group> groups) {
        List<Group> matches = new ArrayList<Group>();
        for (Group g : groups) {
            if (g.players < g.maxPlayers) {
                matches.add(g);
            }
        }
        return matches;
    }
    
    public static List<Group> getGroupsOfType(List<Group> groups, GroupType type) {
        List<Group> matches = new ArrayList<Group>();
        for (Group g : groups) {
            if (g.type == type) {
                matches.add(g);
            }
        }
        return matches;
    }
    
    public static List<Group> getGroupsWithMods(List<Group> groups, List<Modifier> mods) {
        List<Group> groupMatches = new ArrayList<Group>();
        for (Group g : groups) {
            List<String> modStrings = new ArrayList<String>();
            for (Modifier m : g.modifiers) {
                for (String s : m.searchText) {
                    modStrings.add(s);
                }
            }
            int matches = 0;
            for (Modifier m : mods) {
                for (String s : m.searchText) {
                    if (modStrings.contains(s)) {
                        matches++;
                        break;
                    }
                }
            }
            if (matches == mods.size()) {
                groupMatches.add(g);
            }
        }
        return groupMatches;
    }
    
    public static List<Group> getGroupsOfDistrict(List<Group> groups, String district) {
        List<Group> matches = new ArrayList<Group>();
        for (Group g : groups) {
            if (g.district.equals(district)) {
                matches.add(g);
            }
        }
        return matches;
    }
    
    public static List<Group> getGroupsInPlayground(List<Group> groups, Playground playground) {
        List<Group> matches = new ArrayList<Group>();
        for (Group g : groups) {
            if (g.playground == playground) {
                matches.add(g);
            }
        }
        return matches;
    }
    
    public static List<Group> getGroupsOnStreet(List<Group> groups, String street) {
        List<Group> matches = new ArrayList<Group>();
        for (Group g : groups) {
            if (g.street.equals(street)) {
                matches.add(g);
            }
        }
        return matches;
    }
    
    public static List<Group> getGroupsOnStreets(List<Group> groups, String[] streets) {
        List<Group> matches = new ArrayList<Group>();
        for (Group g : groups) {
            for (String s : streets) {
                if (g.street.equals(s)) {
                    matches.add(g);
                }
            }
        }
        return matches;
    }

    public String getTypeString() {
        List<Modifier> frontMods = new ArrayList<Modifier>();
        List<Modifier> backMods = new ArrayList<Modifier>();
        String frontModString = "";
        String backModString = "";
        for (Modifier m : modifiers) {
            if (m.pos == ModifierPos.FRONT) {
                frontMods.add(m);
            } else {
                backMods.add(m);
            }           
        }
        for (Modifier m : frontMods) {
            frontModString = frontModString + m.display;
            if (m != frontMods.get(frontMods.size() - 1)) {
                frontModString = frontModString + " ";
            }
        }
        for (Modifier m : backMods) {
            backModString = backModString + m.display;
            if (m != backMods.get(backMods.size() - 1)) {
                backModString = backModString + " ";
            }
        }
        boolean haveFrontSpace = false;
        if (!frontModString.equals("")) {
            haveFrontSpace = true;
        }
        if (type.display.equals("")) {
            haveFrontSpace = false;
        }
        return Colors.NORMAL + Colors.NORMAL + frontModString + (haveFrontSpace ? (type.hideNameIfHasMods && !modifiers.isEmpty() ? "" : " ") : "") + (type.hideNameIfHasMods && !modifiers.isEmpty() ? "" : type.display) + (!backModString.equals("") ? " " : "") + backModString + Colors.NORMAL;
    }
    
    public String getLocationString() {
        return Colors.NORMAL + (street != null ? " on " + Colors.NORMAL + Colors.NORMAL + street + Colors.NORMAL : "") + (playground != null ? " in " + Colors.NORMAL + Colors.NORMAL + playground.display + Colors.NORMAL : "") + " in " + Colors.NORMAL + Colors.NORMAL + district + Colors.NORMAL;
    }
     
    public String getInfoString() {      
        return Colors.NORMAL + " (" + Colors.NORMAL + "Owned by " + Colors.NORMAL + owner + (type != GroupType.BEAN_FEST ? ", " + players + "/" + maxPlayers + " players" + Colors.NORMAL : "") + Colors.NORMAL + ")";
    }

    public static Group getGroup(String owner) {
        for (Group g : GroupTracker.self.groups) {
            if (g.owner.equals(owner)) {
                return g;
            }
        }
        return null;
    }
}

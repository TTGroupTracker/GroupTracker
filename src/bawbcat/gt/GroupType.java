package bawbcat.gt;

import java.util.ArrayList;
import java.util.List;

public enum GroupType {
    BEAN_FEST("Bean Fest", new String[]{"beanfest", "bean", "fest"}, false, true),
    TASKING("Toon Tasking", new String[]{"tasking", "task", "toontask"}, false, true),
    GAG_TRAINING("Gag Training", new String[]{"training"}, false, true),
    BUILDING("building", new String[]{"build", "bldg", "bld"}, true),
    FIELD_OFFICE("Field Office", new String[]{"fieldoffice"}, true),
    FACTORY("Factory", new String[]{"fact"}),
    MINT("Mint"),
    DA_OFFICE("D.A. Office", new String[]{"da office", "daoffice", "da"}),
    COG_GOLF("Cog Golf", new String[]{"front", "middle", "back"}, false, false, true),
    BOSS("boss", new String[]{"VP", "CFO", "CJ", "CEO", "sos", "shopping"}, false, false, true);
    
    public String display;
    public List<String> searchText = new ArrayList<String>();
    public boolean needsStreet = false;
    public boolean needsPlayground = false;
    public boolean hideNameIfHasMods = false;
    
    GroupType(String display) {
        this.display = display;
        searchText.add(display);
    }
    
    GroupType(String display, String[] searchText) {
        this.display = display;
        this.searchText.add(display);
        for (String s : searchText) {
            this.searchText.add(s);
        }
    }
    
    GroupType(String display, String[] searchText, boolean needsStreet) {
        this.display = display;
        this.searchText.add(display);
        for (String s : searchText) {
            this.searchText.add(s);
        }
        this.needsStreet = needsStreet;
    }
    
    GroupType(String display, String[] searchText, boolean needsStreet, boolean needsPlayground) {
        this.display = display;
        this.searchText.add(display);
        for (String s : searchText) {
            this.searchText.add(s);
        }
        this.needsStreet = needsStreet;
        this.needsPlayground = needsPlayground;
    }
    
    GroupType(String display, String[] searchText, boolean needsStreet, boolean needsPlayground, boolean hideNameIfHasMods) {
        this.display = display;
        this.searchText.add(display);
        for (String s : searchText) {
            this.searchText.add(s);
        }
        this.needsStreet = needsStreet;
        this.needsPlayground = needsPlayground;
        this.hideNameIfHasMods = hideNameIfHasMods;
    }
    
    public static GroupType toGroupType(String s) {
        for (GroupType g : getAllGroupTypes()) {
            for (String s2 : g.searchText) {
                if (s.equalsIgnoreCase(s2)) {
                    return g;
                }
            }
        }
        return null;
    }
    
    public static GroupType[] getAllGroupTypes() {
        return new GroupType[]{GroupType.BEAN_FEST, GroupType.TASKING, GroupType.GAG_TRAINING, GroupType.BUILDING, GroupType.FIELD_OFFICE, GroupType.FACTORY, GroupType.MINT, GroupType.DA_OFFICE, GroupType.COG_GOLF, GroupType.BOSS};
    }
}


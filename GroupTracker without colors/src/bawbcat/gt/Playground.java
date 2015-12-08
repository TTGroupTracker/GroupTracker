package bawbcat.gt;

import java.util.ArrayList;
import java.util.List;

public class Playground {

    public String display;
    public String[] searchText;
    public String[] streets = new String[0];

    public Playground(String display, String[] searchText, String[] streets) {
        this.display = display;
        List<String> search = new ArrayList<String>();
        for (String s : searchText) {
            search.add(s);
        }
        search.add(display);
        searchText = search.toArray(new String[searchText.length]);
        this.searchText = searchText;
        this.streets = streets;
    }

    public static Playground getPlayground(String s) {
        for (Playground p : Main.bot.playgrounds) {
            for (String s2 : p.searchText) {
                if (s.equals(s2)) {
                    return p;
                }
            }
        }
        return null;
    }
    
    public static List<String> getAllSearchText() {
        List<String> text = new ArrayList<String>();
        for (Playground p : GroupTracker.self.playgrounds) {
            for (String s : p.searchText) {
                text.add(s);
            }
        }
        return text;
    }

    public static List<String> getAllStreets() {
        List<String> streets = new ArrayList<String>();
        for (Playground p : Main.bot.playgrounds) {
            for (String s : p.streets) {
                streets.add(s);
            }
        }
        return streets;
    }
}

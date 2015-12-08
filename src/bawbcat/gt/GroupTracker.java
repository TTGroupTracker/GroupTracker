package bawbcat.gt;

import bawbcat.gt.Modifier.ModifierPos;
import bawbcat.gt.commands.Command;
import bawbcat.gt.commands.GTCommand;
import bawbcat.gt.commands.GroupCommand;
import bawbcat.gt.commands.GroupSearchCommand;
import bawbcat.gt.commands.RunCommandCommand;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.User;



public class GroupTracker extends OromusBot implements ActionListener {

    public String version = "2.1";
    
    public String[] joinChannels = {"#channels"};
    
    public String[] districts = {"Corky Bay", "Mild Meadows", "Silly Savannah", "Silly Slopes", "Toony Tundra"};

    public String[] presetOps = {"Judge"};
    public List<String> ops = new ArrayList<String>();

    public String[] groupWords;

    public Playground[] playgrounds = {new Playground("Toontown Central", new String[]{"TTC", "central"}, new String[]{"Silly Street", "Loopy Lane", "Punchline Place"}), new Playground("Donald's Dock", new String[]{"DD", "dock"}, new String[]{"Barnacle Boulevard", "Seaweed Street", "Lighthouse Lane"}),
        new Playground("Daisy Gardens", new String[]{"DG", "gardens", "garden"}, new String[]{"Oak Street", "Maple Street", "Elm Street"}), new Playground("Minnie's Melodyland", new String[]{"MML", "melody"}, new String[]{"Tenor Terrace", "Alto Avenue", "Baritone Boulevard"}),
        new Playground("The Brrrgh", new String[]{"TB", "brrrgh"}, new String[]{"Walrus Way", "Polar Place", "Sleet Street"}), new Playground("Donald's Dreamland", new String[]{"DDL", "dreamland", "dream"}, new String[]{"Pajama Place", "Lullaby Lane"})};
    
    public List<Group> groups = new ArrayList<Group>();
    
    public List<Group> removedGroups = new ArrayList<Group>();
    
    public List<String> channels = new ArrayList<String>();
    
    public boolean enabled = true;
    
    public Timer timer;
    
    public static GroupTracker self;

    public GroupTracker() {
        super();
        setName("GroupTracker");
        setLogin("GroupTracker");
        for (String s : presetOps) {
            ops.add(s);
        }

        Group.gagTrainingMods.add(new Modifier[]{new Modifier("Toonup", false), new Modifier("Trap", false), new Modifier("Lure", false), new Modifier("Sound", false), new Modifier("Throw", false), new Modifier("Squirt", false), new Modifier("Drop", false)});
        
        Group.buildingMods.add(new Modifier[]{new Modifier("1 story", new String[]{"1", "one"}), new Modifier("2 story", new String[]{"2", "two"}), new Modifier("3 story", new String[]{"3", "three"}), new Modifier("4 story", new String[]{"4", "four"}), new Modifier("5 story", new String[]{"5", "five"})});
        Group.buildingMods.add(new Modifier[]{new Modifier("Sellbot", new String[]{"Sell"}), new Modifier("Cashbot", new String[]{"Cash"}), new Modifier("Lawbot", new String[]{"Law"}), new Modifier("Bossbot")});
        
        Group.fieldOfficeMods.add(new Modifier[]{new Modifier("Sellbot", new String[]{"Sell"}), new Modifier("Cashbot", new String[]{"Cash"}), new Modifier("Lawbot", new String[]{"Law"}), new Modifier("Bossbot")});
        
        Group.factoryMods.add(new Modifier[]{new Modifier("Front", false), new Modifier("Side", false), new Modifier("Brutal", false)});
        Group.factoryMods.add(new Modifier[]{new Modifier("Short", false), new Modifier("Long", false), new Modifier("Hybrid", false)});
               
        Group.mintMods.add(new Modifier[]{new Modifier("Coin", false), new Modifier("Dollar", false), new Modifier("Bullion", false)});
        
        Group.daOfficeMods.add(new Modifier[]{new Modifier("A", false).pos(ModifierPos.BACK), new Modifier("B", false).pos(ModifierPos.BACK), new Modifier("C", false).pos(ModifierPos.BACK), new Modifier("D", false).pos(ModifierPos.BACK)});
        
        Group.cogGolfMods.add(new Modifier[]{new Modifier("Front Three", new String[]{"Front"}), new Modifier("Middle Six", new String[]{"Middle"}), new Modifier("Back Nine", new String[]{"Back"})});
        
        Group.bossMods.add(new Modifier[]{new Modifier("SOS Shopping", false, new String[]{"sos", "shopping"}).requires("VP")});
        Group.bossMods.add(new Modifier[]{new Modifier("Brutal", false, new String[]{"Brutal"}).requires("VP")});
        Group.bossMods.add(new Modifier[]{new Modifier("Brutal", false, new String[]{"Brutal"}).requires("CFO")});
        Group.bossMods.add(new Modifier[]{new Modifier("VP"), new Modifier("CFO"), new Modifier("CJ"), new Modifier("CEO")});
        
        List<String> words = new ArrayList<String>();
        for (GroupType g : GroupType.getAllGroupTypes()) {
            for (String s : g.searchText) {
                words.add(s);
            }
        }
        
        groupWords = words.toArray(new String[words.size()]);
        
        timer = new Timer(60000, this);
        timer.start();
        
        self = this;                 
    }

    @Override
    protected void onConnect() {
        //sendRawLine("AUTHSERV COOKIE GroupTracker cookie");
        sendRawLine("AUTHSERV AUTH judge2020 mypassword");
        sendRawLine("mode " + getNick() + " +x");
        for (String s : joinChannels) {
            joinChannel(s);
        }
    }
    
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        if (sender.equals(getNick())) {
            channels.add(channel);
        }
    }
    
    @Override
    protected void onUserList(String channel, User[] users) {
        for (User u : getUsers(channel)) {
            if (u.isOp()) {
                String nick = u.getNick();
                if (!ops.contains(nick)) {
                    ops.add(nick);
                }
            }
        }       
    }
    
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (!sender.equalsIgnoreCase("chanserv")) {            
            process(message, sender, channel);
        }  
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        process(message, sender, sender);
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if (ops.contains(oldNick)) {
            ops.remove(oldNick);
            ops.add(newNick);
        }
        for (Group g : groups) {
            if (g.owner.equals(oldNick)) {
                g.owner = newNick;
            }
            List<String> removeNicks = new ArrayList<String>();
            List<String> addNicks = new ArrayList<String>();
            for (String s : g.nicksComing) {
                if (s.equals(oldNick)) {
                    removeNicks.add(s);
                    addNicks.add(newNick);
                }
            }
            for (String s : removeNicks) {
                g.nicksComing.remove(s);
            }
            for (String s : addNicks) {
                g.nicksComing.add(s);
            }
        }
        
        Group g = Group.getGroup(oldNick);
        if (g != null) {
            g.owner = newNick;
        }
    }
    
    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        handleLoss(sender);
        if (sender.equals(getNick())) {
            channels.remove(channel);
        }
    }
    
    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        handleLoss(recipientNick);
        if (recipientNick.equals(getNick())) {
            channels.remove(channel);
        }
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        handleLoss(sourceNick);
        if (sourceNick.equals(getNick())) {
            channels.clear();
        }     
    }

    @Override
    protected void onDisconnect() {
        channels.clear();
    }
    
    public void handleLoss(String nick) {
        for (String s : channels) {
            for (User u : getUsers(s)) {
                if (u.getNick().equals(nick)) {
                    return;
                }
            }
        }
            Group removedGroup = Group.getGroup(nick);
            if (removedGroup != null) {
                groups.remove(removedGroup);
            }
    }
    
    public void process(String message, String sender, String channel) {
        for (Group g : removedGroups) {
            if (groups.contains(g)) {
                groups.remove(g);
                sendNotice(g.owner, "Your " + g.getTypeString() + " has been deleted due to inactivity.");
            }
        }
        removedGroups.clear();
        
         if (enabled) {
            List<Command> commands = new ArrayList<Command>();
            if (contains(message, "runcommand")) {
                commands.add(new RunCommandCommand());
            }
            if (contains(message, "GT") || contains(message, "GroupServ") || sender.equals(channel)) {
                commands.add(new GTCommand());
            }
            if (contains(message, "group")) {
                commands.add(new GroupCommand());
            }
            if (contains(message, "groups")) {
                commands.add(new GroupSearchCommand());
            }
            boolean ranCommand = false;
            for (Command c : commands) {
                if (runCommand(c, message, sender, channel)) {
                    ranCommand = true;
                    break;
                }
            }
            if (ranCommand) {
                Group g = Group.getGroup(sender);
                if (g != null) {
                    g.timeInactive = 0;
                }
            }
        } else {
            if (contains(message, "GT") || contains(message, "GroupTracker") || sender.equals(channel)) {
                if (contains(message, "enable") || contains(message, "toggle")) {
                    if (ops.contains(sender)) {
                        enabled = true;
                        sendNotice(sender, Colors.PURPLE + Colors.BOLD + "GroupTracker" + Colors.NORMAL + " is now " + Colors.DARK_GREEN + Colors.BOLD + "enabled" + Colors.NORMAL + ".");
                    }
                }
            }
        }
    }

    public boolean runCommand(Command c, String message, String sender, String channel) {
        return c.run(message, sender, channel, this);
    } 
    
    public void saveGroups() {
        try {
            File groupFile = new File("groups.txt");
            groupFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(groupFile));
            for (Group g : groups) { //EG BawbCat:BUILDING:Pastel_Plains:Loopy_Lane:Toontown_Central:7 people:modifiers
                String info = g.owner + ":" + g.type + ":" + g.district + ":" + (g.street != null ? g.street : "null") + ":" + (g.playground != null ? g.playground.display : "null") + ":" + g.players;
                for (Modifier m : g.modifiers) {
                    info = info + m.searchText.get(0) + ":";
                }
                bw.write(info + "\n");
            }
            bw.close();
        } catch (IOException ex) {
        }
    }

    public void loadGroups() {
        try {
            File groupFile = new File("groups.txt");
            if (groupFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader("groups.txt"));
                List<String> groupStrings = new ArrayList<String>();
                while (true) {
                    String s = br.readLine();
                    if (s != null) {
                        groupStrings.add(s);
                    } else {
                        break;
                    }
                }                
                for (String s : groupStrings) {
                    String[] info = s.split(":");
                    Group g = new Group(info[0], info[2], GroupType.toGroupType(info[1]));
                    if (!info[3].equals("null")) {
                        g.street = info[3];
                    }
                    if (!info[4].equals("null")) {
                        g.playground = Playground.getPlayground(info[4]);
                    }
                    g.players = Integer.parseInt(info[5]);
                    for (int i = 6; i < info.length; i++) {
                        for (Modifier[] array : g.validModifiers) {
                            for (Modifier m : array) {
                                for (String s2 : m.searchText) {
                                    if (s2.equalsIgnoreCase(info[i])) {
                                        g.modifiers.add(m);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    groups.add(g);
                }
                br.close();
                groupFile.delete();
            }
        } catch (IOException ex) {
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent a) {
        if (!isConnected()) {
            try {
                reconnect();
            } catch (IOException ex) {
                Logger.getLogger(GroupTracker.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IrcException ex) {
                Logger.getLogger(GroupTracker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (String s : channels) {
            if (!channels.contains(s)) {
                joinChannel(s);
            }
        }
        for (Group g : groups) {
            g.timeInactive++;
            if (g.timeInactive >= 15) {
                removedGroups.add(g);
            } else if (g.timeInactive >= 5 && g.players == g.maxPlayers) {
                removedGroups.add(g);
            }
        }
    }
}

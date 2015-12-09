# GroupTracker

Original GroupTracker by BawbCat from 2014. 

If you would like to improve anything or find a bug, you are welcome to submit a pull request.

Usage guidelines:

1. Do NOT log in with the name "GroupTracker", as that might confuse people of being ran officially. Instead, add a different suffix like "GroupBot", "TTGroupsbot", or most anything else.

2. If you are using it for a private server's IRC, please contact me or bawb for permission. While you do not have to follow this rule, doing so may increase the possibility of support with errors or whatnot. Toontown Infinite is cleared to use this if wanted. 

3. Leave a bit of credit in there to BawbCat and me, at least in "who made GT" action.

4. more to come (maybe?) 



Requirements:

apache ant. super hard to set up on windows, no help here

recommended IDE: eclipse. netbeans may work for you with some tinkering

in IDE: add pircbot.jar (from /dist/lib) to classpath 

recommended to run using the JDK or JRE to build with ant. Whichever one builds successfully


to start:

clone or form the repository

edit GroupTracker.java (src/bawbcat/gt) to include channels to join (L32), districts (L34), ops not in regular op list of channel (L36), and the account/authcookie to use when logging in (L103 & 104)

build with ant, make sure you added pircbot to your classpath. 

make a direct link to the jar file built in dist/GroupTracker.jar and the pircbot.jar in dist/lib.
 
 >> go to grouptrackerupdate section of the repository https://github.com/TTGroupTracker/GroupTrackerUpdate
 
 edit src/bawbcat/gtupdater/Updater.java to include a direct link to the grouptracker.jar and pircbot.jar
 
 build this with ant too (pirc in classpath may be required)
 
 move the compiled source code (GroupTrackerUpdate.jar in /dist to a server or computer with JRE einstalled
 
 run with  
 
 ```
 java -jar "GroupTrackerUpdate.jar"
 ```
 
 It should download the pircbot and grouptracker.jar smoothly and run them to where grouptracker.jar includes dependencies from pirc.
 
 If anything is missing or needs improvement, open an issue.
 

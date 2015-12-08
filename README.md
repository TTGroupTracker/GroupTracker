# GroupTracker
Original; GroupTracker by BawbCat from 2014. Please do not distribute.


Requirements:
apache ant. super hard to set up on windows, no help here
recommended IDE: eclipse. netbeans may work for you with some tinkering
in IDE: add pircbot.jar (from /dist/lib) to classpath 
recommended to run using the JDK or JRE to build with ant. Whichever one builds successfully

to start:
1. clone or form the repository
2. edit GroupTracker.java (src/bawbcat/gt) to include channels to join (L32), districts (L34), ops not in regular op list of channel (L36), and the account/authcookie to use when logging in (L103 & 104)
3. build with ant, make sure you added pircbot to your classpath. 
4. make a direct link to the jar file built in dist/GroupTracker.jar and the pircbot.jar in dist/lib.
4. >> go to grouptrackerupdate section of the repository
5. edit src/bawbcat/gtupdater/Updater.java to include a direct link to the grouptracker.jar and pircbot.jar
6. To be added

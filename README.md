# MCSSH-Connection
An client to control an MC Server over SSH.

### Server Side
Before you connect this client to your Minecraft server, you need to do a few things...:

1. Set up an SSH server.
  - There are hundreds of online tutorials for setting up SSH, so I'll leave that up to you.
  - Note that you do _not_ and _should not_ set up SSH on the client.
2. Copy all the files located in the folder "Server Side" and paste them in the folder from which you plan to run the Minecraft Server from.

### Client Side
When you start up MCSSH, you will be greeted by the following screen:

[![Startup Panel][1]][1]

You need to enter in the following information:

 - In "Username", enter the username of the login for your Minecraft/SSH server.
 - In "Host", enter the IP or address of your Minecraft/SSH server.
 - In "Host Port", enter the port number that you would use to access ssh on that server.
 - In "Target Jar", enter the path to the minecraft server jar which you wish to run, including the name of the jar.
 - If you wish to start the server, check that checkbox.
 - You probably want to always remember the configuration.

Hit Connect.

If this is the first time you are running it, you will, after a short time, see a window that contains your public SSH key. You need to append that to your server's `.ssh/authorized_keys` file before continuing. Otherwise, the connection will commence.

At this point, you should see this window appear:

[![connection][2]][2]

You can enter in normal Minecraft commands in the top-left hand corner, and then execute them with the button just below. The client will keep a log of the commands you run (but will dump them after the connection closes). If you wish to stop the server at any point, simply enter "stop" into the command area and execute it. Close the connection after your finished by pressing the "Close Connection" button.


  [1]: http://i.stack.imgur.com/sWva3.png
  [2]: http://i.stack.imgur.com/Ammos.png

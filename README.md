# Minecraft Server Launcher
Couldn't come up with a fitting name, but that's what it's used for.  
It first runs a pre-launch jar and then another jar.  
For example running packwiz-installer and then minecraft.

## The heck is this???
Imagine this:  
You're hosting a modded Minecraft server through a hosting service. Now, you've got a pretty big modpack that you're modifying frequently.  
As it's annoying to manually manage a modpack, you might have looked into or are using packwiz to manage the pack, maybe you've even read about [packwiz-installer](https://packwiz.infra.link/tutorials/installing/packwiz-installer/) or are already using this for your client instance.  
But it turns out you can't use it on your server, because the hosting service you're using only allows you to upload a custom jar file for Minecraft and doesn't allow a pre-launch command.

That's where this comes in!  
This project is a single jar file that will run 2 other jar files, so you can have a pre-launch command on any hosting service (that allows custom jars to be uploaded).

## Am I committing a crime???
Even though I don't see why anybody would have a problem with this, as it doesn't bypass RAM or any other limitations set for the JVM, but please make sure that your host allows you to use this.

You can check their TOS or contact them about what they allow you to upload as a custom jar (and link to this GitHub page as well).  
I don't want people blaming me for their server being deleted or something because the host they're using doesn't allow this for whatever reason.

<br>

[Nether Host](https://nether.host), where I host my private server, allows this, but as long as you are using a paid server:  
![Screenshot of a Discord message from a nether host manager saying that it's fine to use this as long as you're doing it on a paid server](https://github.com/user-attachments/assets/f8bc5998-5d4f-49f6-a717-17a1503cd02a)

If you've asked and can confirm that another host also allows using this, you can make an issue on this GitHub, so I can add it to the readme and people don't need to ask them about it again and again.

## How tho???
First off download the latest release from [here](https://github.com/OffsetMonkey538/minecraft-server-launcher/releases/latest).  
Now upload it to your Minecraft server. This may be done through a file manager on their website, through an FTP client like FileZilla, whatever. Just somehow upload it.  
Next you'll need to set this as the server jar that will be launched. This may be done by modifying a file, changing a value on their panel, renaming the jar to `server.jar`, whatever you're host needs you to do.

Now you'll need the pre-launch and actual server jars. As examples, I will be using `packwiz-installer-bootstrap.jar` as the pre-launch and `minecraft-server.jar` as the actual server.

Finally, you'll need to tell the launcher what the jars are called and what arguments they should have, if any.  
There are two ways of doing this, first is through jvm properties, and second is through files.  
If your host doesn't allow you to add custom jvm arguments, use files.

#### Pre-Launch Jar
For the pre-launch jar, you can either set the `-DpreLaunchJar` property to your pre-launch jar location, or you can put the location in a file called `preLaunchJar.txt`.  
Property: `java -DpreLaunchJar=packwiz-installer-bootstrap.jar -jar minecraft-server-launcher-1.0.jar`.  
File: (preLaunchJar.txt) `packwiz-installer-bootstrap.jar`

#### Pre-Launch Jar Arguments
Arguments are what the launcher passes into the pre-launch jar. When running a jar from the command line, this is where arguments go: `java -Xmx4G -jar myProgram.jar [ARGUMENTS]`  
They go into either the `-DpreLaunchJarArgs` property, or the `preLaunchJarArgs.txt` file.  
Property: `java -DpreLaunchJarArgs="-g -s server https://[your-server]/pack.toml"`  
File: (preLaunchJarArgs.txt) `-g -s server https://[your-server]/pack.toml`

#### Actual Jar
Same thing as with the pre-launch jar, either `-DactualJar` or the `actualJar.txt` file.  
Property: `java -DactualJar=minecraft-server.jar -jar minecraft-server-launcher-1.0.jar`  
File: (actualJar.txt) `minecraft-server.jar`

### Actual Jar Arguments
Just like pre-launch jar arguments, this time using either `-DactualJarArgs` or the `actualJarArgs.txt` file.  
Property: `java -DactualJarArgs=nogui -jar minecraft-server-launcher-1.0.jar`  
File: (actualJarArgs.txt): `nogui`

<br>

So now if you're using using using properties, the launch command should look something like this:
`java [RAM-STUFF] -DpreLaunchJar=packwiz-installer-bootstrap.jar -DpreLaunchJarArgs="-g -s server https://[your-server]/pack.toml" -DactualJar=minecraft-server.jar -DactualJarArgs=nogui -jar minecraft-server-launcher.jar`

If you have any issues, don't hesitate to create an issue on GitHub or contact me in my [Discord Server](https://discord.offsetmonkey538.top).


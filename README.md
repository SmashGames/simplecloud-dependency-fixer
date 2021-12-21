# 1.8 and 1.12 Support is possible again!
This is a Program to tweak unsupported server versions of SimpleCloud to still work with the cloud.

The first Java Argument gives the path of the server jar to fix.

The second Java Argument provides the dependency directory the cloud is using.

You have to append java arguments for the Program to fix the invalid or missing dependencies in your old
jar. To find out, what dependencies you have to fix, just start a server via your previous server jar. 
The error printed out includes something like ``org.bson`` (for example). Therefore, you have to add ``org/bson``
as Java Argument, since this library is not present or supported.

In my case:
```
java -jar DependencyFixer.jar home/_SIMPLECLOUD/storage/minecraftJars/PAPER_1_8_8.jar /home/_SIMPLECLOUD/dependencies com/google/gson org/bson com/mongodb
```

After successfully executing the JAR, a new server jar is created in the directory of the server jar you are trying to fix.
For example my JAR would be in: ``/home/_SIMPLECLOUD/storage/minecraftJars/PAPER_1_8_8_FIXED.jar``
You can just replace your JAR after this and have a functional server jar again.

WARNING: Please make sure to copy your server jar beforehand, so you have a backup in case something goes wrong.

Ping "Dayeeet#0546" in SimpleCloud's official #user-support on Discord to get help.

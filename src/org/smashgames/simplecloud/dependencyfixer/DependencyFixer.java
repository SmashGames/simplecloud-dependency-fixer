package org.smashgames.simplecloud.dependencyfixer;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class DependencyFixer {

    private final File serverJar, dependencies;
    private final String[] changedDirs;

    public DependencyFixer(String serverJarLocation, String dependenciesLocation, String... changedDirs) throws Exception {
        serverJar = new File(serverJarLocation);
        dependencies = new File(dependenciesLocation);
        this.changedDirs = changedDirs;

        if(!serverJar.exists()) throw new UnknownPathException("JAR_LOCATION is not existent");
        if(!dependencies.exists()) throw new UnknownPathException("DEPENDENCY_LOCATION is not existent");

        if(!dependencies.isDirectory()) throw new BadPathException("DEPENDENCY_LOCATION is no directory");
        System.out.println("Validated arguments, starting to fix the jar.");
        startFixing();
    }

    public void startFixing() throws Exception {
        for (File dependencyFile : Objects.requireNonNull(dependencies.listFiles()))
        {
            if(dependencyFile.isDirectory()) continue;
            new DependencyLookup(dependencyFile.getName()).perform();
        }

        ArrayList<File> toCopy = new ArrayList<>();
        for(DependencyLookup bestVersion : DependencyLookup.PerformedLookups.getLookups())
        {
            System.out.println("Initializing copy for " + bestVersion.dependency);
            toCopy.add(new File(dependencies, bestVersion.dependency + ".jar"));
        }
        ZipCopier.copyWithOut(toCopy, serverJar,  changedDirs);


        System.out.println("Done!");
    }

    public static void main(String[] args) throws Exception {

        if(args.length < 2)
        {
            throw new UnknownPathException("Not enough arguments");
        }
        String[] dirsToChange = new String[args.length - 2];
        if(args.length > 2)
        {
            for(int i = 2; i < args.length; i++)
            {
                    dirsToChange[i - 2] = args[i];
            }
        }
        //Executes as java -jar DependencyFixer JAR_LOCATION DEPENDENCY_LOCATION
        new DependencyFixer(args[0], args[1], dirsToChange);
    }

}

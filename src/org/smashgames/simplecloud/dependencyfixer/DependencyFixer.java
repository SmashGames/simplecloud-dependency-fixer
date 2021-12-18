package org.smashgames.simplecloud.dependencyfixer;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DependencyFixer {

    private final File serverJar, dependencies;

    public DependencyFixer(String serverJarLocation, String dependenciesLocation) throws BadPathException, UnknownPathException, IOException {
        serverJar = new File(serverJarLocation);
        dependencies = new File(dependenciesLocation);

        if(!serverJar.exists()) throw new UnknownPathException("JAR_LOCATION is not existent");
        if(!dependencies.exists()) throw new UnknownPathException("DEPENDENCY_LOCATION is not existent");

        if(!dependencies.isDirectory()) throw new BadPathException("DEPENDENCY_LOCATION is no directory");
        System.out.println("Validated arguments, starting to fix the jar.");
        startFixing();
    }

    public void startFixing() throws IOException {
        for (File dependencyFile : Objects.requireNonNull(dependencies.listFiles()))
        {
            if(dependencyFile.isDirectory()) continue;
            new DependencyLookup(dependencyFile.getName()).perform();
        }

        for(DependencyLookup bestVersion : DependencyLookup.PerformedLookups.getLookups())
        {
            System.out.println("Initializing copy for " + bestVersion.dependency);
            ZipCopier.copyWithOut(new File(dependencies, bestVersion.dependency + ".jar"), serverJar, "META-INF");
        }

        System.out.println("Done!");
    }

    public static void main(String[] args) throws UnknownPathException, BadPathException, IOException {

        if(args.length < 2)
        {
            throw new UnknownPathException("Not enough arguments");
        }

        //Executes as java -jar DependencyFixer JAR_LOCATION DEPENDENCY_LOCATION
        new DependencyFixer(args[0], args[1]);
    }

}

package org.smashgames.simplecloud.dependencyfixer;

import java.util.ArrayList;

public class DependencyLookup {

    private final String dependencyName;
    private final String dependencyVersion;
    public final String dependency;
    public DependencyLookup(String fileName)
    {
        dependency = fileName.substring(0, fileName.length() - 4);
        dependencyName = dependency.split("-")[0];
        dependencyVersion = dependency.split("-")[1];
    }

    public void perform()
    {
        if(DependencyLookup.PerformedLookups.hasLookup(dependencyName))
        {
            if(hasHigherVersion(PerformedLookups.getLookup(dependencyName).dependencyVersion))
            {
                DependencyLookup.PerformedLookups.replaceLookup(this);
            }
        }

        else{
            DependencyLookup.PerformedLookups.replaceLookup(this);
        }
    }

    private boolean hasHigherVersion(String otherVersion)
    {
        int[] thisSidedVersionArgs = new int[dependencyVersion.split(".").length];
        for(int i = 0; i < thisSidedVersionArgs.length; i++){
            thisSidedVersionArgs[i] = Integer.parseInt(dependencyVersion.split(".")[i]);
        }

        int[] otherSidedVersionArgs = new int[otherVersion.split(".").length];
        for(int i = 0; i < otherSidedVersionArgs.length; i++){
            otherSidedVersionArgs[i] = Integer.parseInt(otherVersion.split(".")[i]);
        }

        int index = 0;
        int mostArgs = Math.max(thisSidedVersionArgs.length, otherSidedVersionArgs.length);
        while(mostArgs >= index)
        {
            if(thisSidedVersionArgs.length < index) return false;
            if(otherSidedVersionArgs.length < index) return true;
            if(thisSidedVersionArgs[index] > otherSidedVersionArgs[index]) return true;
            if(thisSidedVersionArgs[index] < otherSidedVersionArgs[index]) return false;

            index++;
        }


        return false;
    }

    public static class PerformedLookups{
        private static final ArrayList<DependencyLookup> lookups = new ArrayList<>();

        public static boolean hasLookup(String dependencyName)
        {
            return lookups.stream().anyMatch(dependencyLookup -> dependencyLookup.dependencyName.equals(dependencyName));
        }

        public static void replaceLookup(DependencyLookup replacedWith)
        {
            lookups.remove(lookups.stream().filter(lookup -> replacedWith.dependencyName.equals(lookup.dependencyName)).findFirst().orElse(null));
            lookups.add(replacedWith);
        }

        public static DependencyLookup getLookup(String dependencyName)
        {
            return lookups.stream().filter(dependencyLookup -> dependencyLookup.dependencyName.equals(dependencyName)).findFirst().orElse(null);
        }

        public static ArrayList<DependencyLookup> getLookups()
        {
            return lookups;
        }

    }
}


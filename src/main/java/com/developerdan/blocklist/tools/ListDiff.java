package com.developerdan.blocklist.tools;

import java.util.NavigableSet;
import java.util.TreeSet;

public class ListDiff {
    public static String DomainList(final NavigableSet<Domain> before, final NavigableSet<Domain> after) {
        var patchBuilder = new PatchBuilder();
        TreeSet<Domain> removedDomains = ListDiff.difference(before, after);
        TreeSet<Domain> addedDomains = ListDiff.difference(after, before);
        TreeSet<Domain> allDomains = new TreeSet<>(before);
        allDomains.addAll(after);
        for(Domain entry : allDomains) {
            if (removedDomains.contains(entry)) {
                patchBuilder.removed(entry);
            } else if (addedDomains.contains(entry)) {
                patchBuilder.added(entry);
            } else {
                patchBuilder.noChange(entry);
            }
        }
        return patchBuilder.toString();
    }

    private static TreeSet<Domain> difference(final NavigableSet<Domain> before, final NavigableSet<Domain> after) {
        TreeSet<Domain> removedDomains = new TreeSet<>(before);
        removedDomains.removeAll(after);
        return removedDomains;
    }
}

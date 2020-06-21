package com.developerdan.blocklist.tools;

import java.util.NavigableSet;
import java.util.TreeSet;

public class ListDiff {
    public static String DomainList(final NavigableSet<Domain> before, final NavigableSet<Domain> after) {
        var patchBuilder = new PatchBuilder();
        var removedDomains = ListDiff.difference(before, after);
        var addedDomains = ListDiff.difference(after, before);
        var allDomains = new TreeSet<>(before);
        allDomains.addAll(after);
        for(Domain domain : allDomains) {
            if (removedDomains.contains(domain)) {
                patchBuilder.removed(domain);
            } else if (addedDomains.contains(domain)) {
                patchBuilder.added(domain);
            } else {
                patchBuilder.noChange(domain);
            }
        }
        return patchBuilder.toString();
    }

    private static NavigableSet<Domain> difference(final NavigableSet<Domain> before, final NavigableSet<Domain> after) {
        var removedDomains = new TreeSet<>(before);
        removedDomains.removeAll(after);
        return removedDomains;
    }
}

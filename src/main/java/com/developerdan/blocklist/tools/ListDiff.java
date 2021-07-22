package com.developerdan.blocklist.tools;

import java.util.NavigableSet;
import java.util.TreeSet;

public class ListDiff {

    private ListDiff() { }

    public static String domainList(final NavigableSet<Domain> before, final NavigableSet<Domain> after) {
        var patchBuilder = new PatchBuilder();

        TreeSet<Domain> allDomains = new TreeSet<>(before);
        allDomains.addAll(after);
        for(Domain entry : allDomains) {
            if (!before.contains(entry)) {
                patchBuilder.added(entry);
            } else if (!after.contains(entry)) {
                patchBuilder.removed(entry);
            } else {
                patchBuilder.noChange(entry);
            }
        }
        return patchBuilder.toString();
    }
}

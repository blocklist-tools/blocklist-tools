package com.developerdan.blocklist.tools;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

final class PatchBuilder {
    private static final int SURROUNDING_LINES = 3;
    private static final String NO_CHANGE = " ";
    private static final String REMOVED = "-";
    private static final String ADDED = "+";

    private List<String> patches = new ArrayList<>();
    private StringBuilder patch = new StringBuilder();
    private Deque<String> leadingLinesBuffer = new ArrayDeque<>();
    private int linesSinceChange = SURROUNDING_LINES + 1;

    public void removed(final Domain domain) {
        linesSinceChange = 0;
        addDomain(REMOVED, domain);
    }

    public void added(final Domain domain) {
        linesSinceChange = 0;
        addDomain(ADDED, domain);
    }

    public void noChange(final Domain domain) {
        linesSinceChange++;
        addDomain(NO_CHANGE, domain);
    }

    public List<String> patches() {
        var copy = new ArrayList<>(patches);
        copy.add(patch.toString());
        return copy;
    }

    @Override
    public String toString() {
        return String.join("---\n", patches());
    }

    private void addDomain(final String changeSymbol, final Domain domain) {
        var line = changeSymbol + domain + "\n";
        if (isChange(changeSymbol)) {
            patch.append(drainLineBuffer());
        }

        if (linesSinceChange < SURROUNDING_LINES+1) {
            patch.append(line);
            return;
        }
        if (linesSinceChange == SURROUNDING_LINES+1) {
            patches.add(patch.toString());
            patch = new StringBuilder();
        }
        if (leadingLinesBuffer.size() == SURROUNDING_LINES) {
            leadingLinesBuffer.pop();
        }
        leadingLinesBuffer.add(changeSymbol + domain + "\n");
    }

    private String drainLineBuffer() {
        var buffer = String.join("", leadingLinesBuffer);
        leadingLinesBuffer = new ArrayDeque<>();
        return buffer;
    }

    private boolean isChange(final String changeSymbol) {
        return !NO_CHANGE.equals(changeSymbol);
    }
}

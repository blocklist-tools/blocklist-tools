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

    private final List<String> patches = new ArrayList<>();
    private StringBuilder patch = new StringBuilder();
    private Deque<String> leadingLinesBuffer = new ArrayDeque<>();
    private int linesSinceChange = SURROUNDING_LINES + 1;

    public void removed(final Domain entry) {
        linesSinceChange = 0;
        addDomain(REMOVED, entry);
    }

    public void added(final Domain entry) {
        linesSinceChange = 0;
        addDomain(ADDED, entry);
    }

    public void noChange(final Domain entry) {
        linesSinceChange++;
        addDomain(NO_CHANGE, entry);
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

    private void addDomain(final String changeSymbol, final Domain entry) {
        var line = changeSymbol + entry + "\n";
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
        leadingLinesBuffer.add(changeSymbol + entry + "\n");
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

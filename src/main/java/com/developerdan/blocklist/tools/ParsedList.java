package com.developerdan.blocklist.tools;

import java.util.NavigableSet;

public class ParsedList<E> {
    private NavigableSet<E> records;
    private String parsedSha;
    private String originalSha;

    public ParsedList(NavigableSet<E> records, String parsedSha, String originalSha) {
        this.records = records;
        this.parsedSha = parsedSha;
        this.originalSha = originalSha;
    }

    public NavigableSet<E> getRecords() {
        return records;
    }

    public void setRecords(NavigableSet<E> records) {
        this.records = records;
    }

    public String getParsedSha() {
        return parsedSha;
    }

    public void setParsedSha(String parsedSha) {
        this.parsedSha = parsedSha;
    }

    public String getOriginalSha() {
        return originalSha;
    }

    public void setOriginalSha(String originalSha) {
        this.originalSha = originalSha;
    }
}

package com.developerdan.blocklist.tools;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class DomainParser {
    abstract public Optional<Domain> parseLine(String line);

    public final NavigableSet<Domain> parseFile(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
            return parseStream(stream.parallel());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public final NavigableSet<Domain> parseStream(Stream<String> stream) {
        return stream
                .map(this::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}

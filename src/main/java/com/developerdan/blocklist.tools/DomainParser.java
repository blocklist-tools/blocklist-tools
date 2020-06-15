package com.developerdan.blocklist.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

abstract class DomainParser {
    abstract public Optional<Domain> parseLine(String line);

    public final Stream<Domain> BlocklistReader(String fileName) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
            return stream.parallel()
                    .map(this::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        }
    }
}

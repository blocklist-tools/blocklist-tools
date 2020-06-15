package com.developerdan.blocklist.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public final class BlocklistReader {
    public static Stream<Domain> BlocklistReader(String fileName, DomainParser parser) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
            return stream.parallel()
                    .map(parser::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        }
    }
}

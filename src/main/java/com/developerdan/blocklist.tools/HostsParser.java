package com.developerdan.blocklist.tools;

import java.util.Optional;
import java.util.regex.Pattern;

public class HostsParser extends DomainParser {

    private static final Pattern STRIP_HOSTS_COMMENTS = Pattern.compile("^([^#]+)", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public Optional<Domain> parseLine(String line) {
        var matcher = HostsParser.STRIP_HOSTS_COMMENTS.matcher(line);
        if (matcher.find()) {
            var lineParts = matcher.group(0).split("\\s+");
            if (lineParts.length != 2) {
                return Domain.fromString(lineParts[1]);
            }
        }
        return Optional.empty();
    }
}

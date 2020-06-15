package com.developerdan.blocklist.tools;

import java.util.Optional;
import java.util.regex.Pattern;

public class DomainListParser extends DomainParser {

    private static final Pattern STRIP_HOSTS_COMMENTS = Pattern.compile("^([^#]+)", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public Optional<Domain> parseLine(String line) {
        var matcher = DomainListParser.STRIP_HOSTS_COMMENTS.matcher(line);
        if (matcher.find()) {
            return Domain.fromString(matcher.group(0).strip());
        }
        return Optional.empty();
    }
}

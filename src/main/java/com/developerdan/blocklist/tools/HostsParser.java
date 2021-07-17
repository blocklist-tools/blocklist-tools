package com.developerdan.blocklist.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Pattern;

public final class HostsParser extends BlocklistParser<Domain> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostsParser.class);
    private static final Pattern STRIP_HOSTS_COMMENTS = Pattern.compile("^[\\d\\.:]+\\s+([^#\\s]+)", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public final Optional<Domain> parseLine(String line) {
        var matcher = HostsParser.STRIP_HOSTS_COMMENTS.matcher(line);
        if (matcher.find()) {
            var lineParts = matcher.group(0).split("\\s+", -1);
            if (lineParts.length == 2) {
                return Domain.fromString(lineParts[1]);
            }
        }
        LOGGER.debug("un-parsable hosts line: <{}>", line);
        return Optional.empty();
    }
}

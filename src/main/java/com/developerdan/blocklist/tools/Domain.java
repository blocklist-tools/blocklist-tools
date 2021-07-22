package com.developerdan.blocklist.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.IDN;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

public final class Domain implements Comparable<Domain> {

    private final String name;
    private static final Logger LOGGER = LoggerFactory.getLogger(Domain.class);

    private Domain(String name) {
        this.name = normalize(name);
    }

    public static Optional<Domain> fromUrl(final String url) {
        try {
            var uri = new URI(url);
            return Domain.fromString(uri.getHost());
        } catch (URISyntaxException ex) {
            // not a URL, maybe is just a host
            return Domain.fromString(url);
        }
    }

    public static Optional<Domain> fromString(final String domainInput) {
        var domain = new Domain(domainInput);
        if (domain.isValid()) {
            return Optional.of(domain);
        }
        return Optional.empty();
    }

    public boolean isValid() {
        return !name.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    private static String normalize(final String domain) {
        if (domain == null
            || domain.length() > 255
            || domain.startsWith("-")
            || domain.endsWith("-")
            || domain.endsWith(".")
            || domain.matches(".*[~!%\\*\\(\\)\"\\s'#`<>\\?=&@\\\\\\^\\]\\[{}:\\$\\|/\\+;].*")
            || domain.matches(".*\\.\\d+$")
            || domain.matches(".*[\\s]+.*")
        ) {
            LOGGER.info("Domain <{}> is not valid", domain);
            return "";
        }
        try {
            var normalized = IDN.toASCII(domain);
            normalized = IDN.toASCII(normalized);
            return normalized.toLowerCase(Locale.ENGLISH);
        } catch (IllegalArgumentException ex) {
            LOGGER.info("Domain <{}> is not valid: {}", domain, ex.getMessage());
            return "";
        }
    }

    private ArrayList<String> domainParts()
    {
        var domainParts = Arrays.asList(name.split("\\.", -1));
        Collections.reverse(domainParts);
        return new ArrayList<>(domainParts);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Domain otherDomain = (Domain) other;
        return name.equals(otherDomain.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Domain other) {
        if (other == null) {
            return 1;
        }
        if (this.equals(other)) {
            return 0;
        }

        var domainParts = domainParts();
        var otherDomainParts = other.domainParts();
        var index = 0;
        for (String part: domainParts) {
            if (otherDomainParts.size() <= index) {
                return 1;
            }
            var comparison = part.compareTo(otherDomainParts.get(index));
            if (comparison != 0) {
                return comparison;
            }
            index++;
        }
        // we know that the domains are not equal, and we've already looped
        // through all our sections, so 'other' must be longer
        return -1;
    }
}

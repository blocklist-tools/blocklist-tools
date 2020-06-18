package com.developerdan.blocklist.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.IDN;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public final class Domain {

    private final String domain;
    private static Logger LOGGER = LoggerFactory.getLogger(Domain.class);

    private Domain(String domain) {
        this.domain = normalize(domain);
    }

    public static Optional<Domain> fromString(final String domainInput) {
        var domain = new Domain(domainInput);
        if (domain.isValid()) {
            return Optional.of(domain);
        }
        return Optional.empty();
    }

    public boolean isValid() {
        return !domain.isEmpty();
    }

    @Override
    public String toString() {
        return domain;
    }

    private static String normalize(final String domain) {
        if (domain == null || domain.length() > 255 || domain.endsWith(".")) {
            LOGGER.debug("Domain <{}> is not valid", domain);
            return "";
        }
        try {
            var normalized = IDN.toASCII(domain);
            normalized = IDN.toASCII(normalized, IDN.USE_STD3_ASCII_RULES);
            return normalized.toLowerCase(Locale.ENGLISH);
        } catch (IllegalArgumentException ex) {
            LOGGER.debug("Domain <{}> is not valid: {}", domain, ex.getMessage());
            return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain1 = (Domain) o;
        return domain.equals(domain1.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain);
    }
}

package com.developerdan.blocklist.tools;

import java.net.IDN;
import java.util.Locale;
import java.util.Optional;

public final class Domain {

    private final String domain;

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
            return "";
        }
        String normalized;
        try {
            normalized = IDN.toASCII(domain);
            normalized = IDN.toASCII(normalized, IDN.USE_STD3_ASCII_RULES);
        } catch (IllegalArgumentException ex) {
            return "";
        }

        return normalized.toLowerCase(Locale.ENGLISH);
    }
}

import java.net.IDN;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public final class Domain {

    private static final Pattern STRIP_HOSTS_COMMENTS = Pattern.compile("^([^#]+)", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
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

    public static Optional<Domain> fromHostFileLine(final String line) {
        var matcher = Domain.STRIP_HOSTS_COMMENTS.matcher(line);
        if (matcher.find()) {
            var lineParts = matcher.group(0).split("\\s+");
            if (lineParts.length != 2) {
                return Domain.fromString(lineParts[1]);
            }
        }
        return Optional.empty();
    }

    public static Optional<Domain> fromDomainBlocklistLine(final String line) {
        var matcher = Domain.STRIP_HOSTS_COMMENTS.matcher(line);
        if (matcher.find()) {
            return Domain.fromString(matcher.group(0).strip());
        }
        return Optional.empty();
    }

    public boolean isValid() {
        return !domain.isEmpty();
    }

    private static String normalize(final String domain) {
        if (domain == null || domain.length() > 255) {
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

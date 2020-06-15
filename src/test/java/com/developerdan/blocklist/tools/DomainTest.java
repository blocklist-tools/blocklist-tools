package com.developerdan.blocklist.tools;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DomainTest {

    @Test
    public void canCreateSimpleDomain() {
        var domain = Domain.fromString("example.com");
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString("example.com");
    }

    @Test
    public void canCreateSubDomain() {
        var domain = Domain.fromString("www.example.com");
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString("www.example.com");
    }

    @Test
    public void canParseUnicode() {
        var domain = Domain.fromString("www.с킰.com");
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString("www.xn--q1a4875h.com");
    }

    @Test
    public void canParsePunyCode() {
        var domain = Domain.fromString("www.xn--q1a4875h.com");
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString("www.xn--q1a4875h.com");
    }

    @Test
    public void cannotParseEmptyString() {
        var domain = Domain.fromString("");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseNull() {
        var domain = Domain.fromString(null);
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseHostsLine() {
        var domain = Domain.fromString("0.0.0.0 example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseDomainWithComments() {
        var domain = Domain.fromString("example.com # my comment");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseLeadingHash() {
        var domain = Domain.fromString("#example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseCommentedOutDomain() {
        var domain = Domain.fromString("# example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseWildcard() {
        var domain = Domain.fromString("*.example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParsePercent() {
        var domain = Domain.fromString("%.example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseDoubleQuote() {
        var domain = Domain.fromString("\"example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseSingleQuote() {
        var domain = Domain.fromString("'example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseLeftParen() {
        var domain = Domain.fromString("(example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseRightParen() {
        var domain = Domain.fromString(")example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseTrailingDot() {
        var domain = Domain.fromString("example.com.");
        assertThat(domain).isEmpty();
    }

    @Test
    public void cannotParseLeadingDash() {
        var domain = Domain.fromString("-example.com");
        assertThat(domain).isEmpty();
    }

    @Test
    public void canParseMiddleDash() {
        var domain = Domain.fromString("my-example.com");
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString("my-example.com");
    }

    @Test
    public void cannotParseDomainsTooLong() {
        var longDomain = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa."
                       + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa."
                       + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa."
                       + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa."
                       + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa."
                       + "aa.com"; // 256
        var domain = Domain.fromString(longDomain);
        assertThat(domain).isEmpty();
    }
}

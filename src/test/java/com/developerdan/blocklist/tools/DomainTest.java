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

    @Test
    public void canCompareEqualDomains() {
        var one = Domain.fromString("equals.example.com");
        var two = Domain.fromString("equals.example.com");
        assertThat(one.get()).isEqualTo(two.get());
        assertThat(one.get()).isEqualByComparingTo(two.get());
    }

    @Test
    public void canEqualSelf() {
        var one = Domain.fromString("equals.example.com");
        assertThat(one.get()).isEqualTo(one.get());
        assertThat(one.get()).isEqualByComparingTo(one.get());
    }

    @Test
    public void canCompareNull() {
        var one = Domain.fromString("example.com");
        assertThat(one.get()).isNotEqualTo(null);
        assertThat(one.get()).isNotEqualByComparingTo(null);
    }

    @Test
    public void canCompareDifferentDomains() {
        var one = Domain.fromString("one.example.com");
        var two = Domain.fromString("two.example.com");
        assertThat(one.get()).isNotEqualTo(two.get());
        assertThat(one.get()).isNotEqualByComparingTo(two.get());
        assertThat(one.get()).isLessThan(two.get());
        assertThat(two.get()).isGreaterThan(one.get());
    }

    @Test
    public void canCompareByDomainSections() {
        var one = Domain.fromString("same.example.com");
        var two = Domain.fromString("same.example.edu");
        assertThat(one.get()).isLessThan(two.get());
        assertThat(two.get()).isGreaterThan(one.get());
    }

    @Test
    public void canCompareByDomainSectionsWithDifferentLengths() {
        var one = Domain.fromString("same.example.com");
        var two = Domain.fromString("another.same.example.com");
        assertThat(one.get()).isLessThan(two.get());
        assertThat(two.get()).isGreaterThan(one.get());
    }
}

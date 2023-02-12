package com.developerdan.blocklist.tools;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DomainTest {

    @Test
    void canCreateSimpleDomain() {
        assertValidDomain("example.com", "example.com");
    }

    @Test
    void canCreateSubDomain() {
        assertValidDomain("www.example.com", "www.example.com");
    }

    @Test
    void canParseUnicode() {
        assertValidDomain("www.с킰.com", "www.xn--q1a4875h.com");
    }

    @Test
    void canParseEmoji() {
        assertValidDomain("☺.com", "xn--74h.com");
    }

    @Test
    void canParsePunyCode() {
        assertValidDomain("www.xn--q1a4875h.com", "www.xn--q1a4875h.com");
    }

    @Test
    void canParseDomainWithUnderscore() {
        assertValidDomain("whatcounts_saas.s3.amazonaws.com", "whatcounts_saas.s3.amazonaws.com");
    }

    @Test
    void cannotParseEmptyString() {
        assertInvalidDomain("");
    }

    @Test
    void cannotParseNull() {
        assertInvalidDomain(null);
    }

    @Test
    void cannotParseHostsLine() {
        assertInvalidDomain("0.0.0.0 example.com");
    }

    @Test
    void cannotParseDomainWithComments() {
        assertInvalidDomain("example.com # my comment");
    }

    @Test
    void cannotParseLeadingHash() {
        assertInvalidDomain("#example.com");
    }

    @Test
    void cannotParseCommentedOutDomain() {
        assertInvalidDomain("# example.com");
    }

    @Test
    void cannotParseWildcard() {
        assertInvalidDomain("*.example.com");
    }

    @Test
    void cannotParsePercent() {
        assertInvalidDomain("%.example.com");
    }

    @Test
    void cannotParseDoubleQuote() {
        assertInvalidDomain("\"example.com");
    }

    @Test
    void cannotParseSingleQuote() {
        assertInvalidDomain("'example.com");
    }

    @Test
    void cannotParseLeftParen() {
        assertInvalidDomain("(example.com");
    }

    @Test
    void cannotParseRightParen() {
        assertInvalidDomain(")example.com");
    }

    @Test
    void cannotParseTrailingDot() {
        assertInvalidDomain("example.com.");
        assertInvalidDomain("alisat.biz.");
        assertInvalidDomain("alisat.biz。");
        assertInvalidDomain("dsp.simba.taobao.com.");
        assertInvalidDomain("dsp.simba.taobao.com。");
    }

    @Test
    void cannotParseExclamation() {
        assertInvalidDomain("exam!ple.com");
    }

    @Test
    void cannotParseQuestionMark() {
        assertInvalidDomain("exam?ple.com");
    }

    @Test
    void cannotParseLeadingQuestionMark() {
        assertInvalidDomain("?.example.com");
    }

    @Test
    void cannotParseOpenAngle() {
        assertInvalidDomain("exam<ple.com");
    }

    @Test
    void cannotParseHtml() {
        assertInvalidDomain("<html><head>");
    }

    @Test
    void cannotParseCloseAngle() {
        assertInvalidDomain("exam>ple.com");
    }

    @Test
    void cannotParseColon() {
        assertInvalidDomain("exam:ple.com");
    }

    @Test
    void cannotParseSimiColon() {
        assertInvalidDomain("exam;ple.com");
    }

    @Test
    void cannotParseOpenBracket() {
        assertInvalidDomain("exam[ple.com");
    }

    @Test
    void cannotParseCloseBracket() {
        assertInvalidDomain("exam]ple.com");
    }

    @Test
    void cannotParsePlus() {
        assertInvalidDomain("exam+ple.com");
    }

    @Test
    void cannotParseEquals() {
        assertInvalidDomain("exam=ple.com");
    }

    @Test
    void cannotParseAmpersand() {
        assertInvalidDomain("exa&ple.com");
    }

    @Test
    void cannotParseCarrot() {
        assertInvalidDomain("exa^ple.com");
    }

    @Test
    void cannotParseDollar() {
        assertInvalidDomain("exa$ple.com");
    }

    @Test
    void cannotParseAt() {
        assertInvalidDomain("exa@ple.com");
    }

    @Test
    void cannotParseTildi() {
        assertInvalidDomain("exa~ple.com");
    }

    @Test
    void cannotParseOpenCurly() {
        assertInvalidDomain("exa{ple.com");
    }

    @Test
    void cannotParseCloseCurly() {
        assertInvalidDomain("exa}ple.com");
    }

    @Test
    void cannotParsePipe() {
        assertInvalidDomain("exa|ple.com");
    }

    @Test
    void cannotParseForwardSlash() {
        assertInvalidDomain("exa/ple.com");
    }

    @Test
    void cannotParseBackSlash() {
        assertInvalidDomain("exa\\ple.com");
    }

    @Test
    void cannotParseLeadingDash() {
        assertInvalidDomain("-example.com");
    }

    @Test
    void cannotParseLeadingPeriod() {
        assertInvalidDomain(".example.com");
    }

    @Test
    void cannotParseTrailingDash() {
        assertInvalidDomain("example.com-");
    }

    @Test
    void cannotParseIpAddress() {
        assertInvalidDomain("8.8.8.8");
    }

    @Test
    void cannotParseNumericOnlyTLD() {
        assertInvalidDomain("example.com.123");
    }

    @Test
    void cannotParseMiddleSpace() {
        assertInvalidDomain("exa mple.com");
    }

    @Test
    void cannotParseLeadingSpace() {
        assertInvalidDomain(" example.com");
    }

    @Test
    void cannotParseTrailingSpace() {
        assertInvalidDomain("example.com ");
    }

    @Test
    void cannotParseMiddleTab() {
        assertInvalidDomain("exa\\tmple.com ");
    }

    @Test
    void cannotParseLeadingTab() {
        assertInvalidDomain("\texample.com ");
    }

    @Test
    void cannotParseTrailingTab() {
        assertInvalidDomain("example.com\t");
    }

    @Test
    void cannotParseNewLine() {
        assertInvalidDomain("examp\nle.com");
    }

    @Test
    void cannotParseBackTick() {
        assertInvalidDomain("examp`le.com");
    }

    @Test
    void canParsePartialNumericTLD() {
        var domain = Domain.fromString("example.com.my123");
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString("example.com.my123");
    }

    @Test
    void canParseMiddleDash() {
        var domain = Domain.fromString("my-example.com");
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString("my-example.com");
    }

    @Test
    void cannotParseDomainsTooLong() {
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
    void canCompareEqualDomains() {
        var one = Domain.fromString("equals.example.com");
        var two = Domain.fromString("equals.example.com");
        assertThat(one).isNotEmpty();
        assertThat(two).isNotEmpty();
        assertThat(one).isEqualTo(two);
        assertThat(one.get()).isEqualByComparingTo(two.get());
    }

    @Test
    void canEqualSelf() {
        var one = Domain.fromString("equals.example.com");
        assertThat(one).isNotEmpty().isEqualTo(one);
        assertThat(one.get()).isEqualByComparingTo(one.get());
    }

    @Test
    void canCompareNull() {
        var one = Domain.fromString("example.com");
        assertThat(one).isNotEmpty();
        assertThat(one.get()).isNotEqualByComparingTo(null);
    }

    @Test
    void canCompareDifferentDomains() {
        var one = Domain.fromString("one.example.com");
        var two = Domain.fromString("two.example.com");
        assertThat(one).isNotEmpty();
        assertThat(two).isNotEmpty();
        assertThat(one.get()).isNotEqualTo(two.get());
        assertThat(one.get()).isNotEqualByComparingTo(two.get());
        assertThat(one.get()).isLessThan(two.get());
        assertThat(two.get()).isGreaterThan(one.get());
    }

    @Test
    void canCompareByDomainSections() {
        var one = Domain.fromString("same.example.com");
        var two = Domain.fromString("same.example.edu");
        assertThat(one).isNotEmpty();
        assertThat(two).isNotEmpty();
        assertThat(one.get()).isLessThan(two.get());
        assertThat(two.get()).isGreaterThan(one.get());
    }

    @Test
    void canCompareByDomainSectionsWithDifferentLengths() {
        var one = Domain.fromString("same.example.com");
        var two = Domain.fromString("another.same.example.com");
        assertThat(one).isNotEmpty();
        assertThat(two).isNotEmpty();
        assertThat(one.get()).isLessThan(two.get());
        assertThat(two.get()).isGreaterThan(one.get());
    }

    @Test
    void canGetDomainFromFromHttpUrl() {
        assertDomainFromUrl("http://example.com", "example.com");
    }

    @Test
    void canGetDomainFromFromHttpsUrl() {
        assertDomainFromUrl("https://example.com", "example.com");
    }

    @Test
    void canGetDomainFromFromWwwUrl() {
        assertDomainFromUrl("https://www.example.com", "www.example.com");
    }

    @Test
    void canGetDomainFromFromFtp() {
        assertDomainFromUrl("ftp://example.com", "example.com");
    }

    @Test
    void canGetDomainFromFromLocal() {
        assertDomainFromUrl("http://localhost/test", "localhost");
    }

    @Test
    void canGetDomainFromFromLocalWithPort() {
        assertDomainFromUrl("http://localhost:8080/test", "localhost");
    }

    @Test
    void canGetWithUrlParams() {
        assertDomainFromUrl("http://example.co.uk?foo=bar&test=true", "example.co.uk");
    }

    @Test
    void cannotGetDomainFromDomain() {
        var domain = Domain.fromUrl("example.com");
        assertThat(domain).isEmpty();
    }

    private void assertDomainFromUrl(String url, String expected) {
        var domain = Domain.fromUrl(url);
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString(expected);
    }

    private void assertInvalidDomain(String domainName) {
        var domain = Domain.fromString(domainName);
        assertThat(domain).isEmpty();
    }

    private void assertValidDomain(String domainName, String expected) {
        var domain = Domain.fromString(domainName);
        assertThat(domain).isNotEmpty();
        assertThat(domain.get()).hasToString(expected);
    }
}

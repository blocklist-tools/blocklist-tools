package com.developerdan.blocklist.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DnsQueryTest {

    static String VALID_DOMAIN = "example.com";
    static String NX_DOMAIN = "this-is-not-a-real-domain-12452345.example.com";

    @BeforeEach
    @SuppressWarnings("UnusedMethod")
    public void setDefaultResolver() {
        DnsQuery.resolvers(new String[]{});
    }

    @Test
    void canResolveValidDomainUsingDefaultResolver() throws IOException {
        var result = DnsQuery.dig(getDomain(VALID_DOMAIN), "A");
        assertThat(result.getQueryName()).isEqualTo(VALID_DOMAIN);
        assertThat(result.getRcode()).isEqualTo("NOERROR");
    }

    @Test
    void canResolveNxDomainUsingDefaultResolver() throws IOException {
        var result = DnsQuery.dig(getDomain(NX_DOMAIN), "A");
        assertThat(result.getQueryName()).contains(NX_DOMAIN);
        assertThat(result.getRcode()).contains("NXDOMAIN");
    }

    @Test
    void canResolveAAAA() throws IOException {
        var result = DnsQuery.dig(getDomain(VALID_DOMAIN), "AAAA");
        assertThat(result.getQueryName()).contains(VALID_DOMAIN);
        assertThat(result.getRcode()).contains("NOERROR");
    }

    @Test
    void canDefineDifferentResolver() {
        DnsQuery.resolvers(new String[]{"1.1.1.1", "8.8.8.8"});
        DnsQuery.resolvers(new String[]{"1.1.1.1:53", "8.8.8.8:53"});
    }

    @Test
    void canNotDefineInvalidResolver() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> DnsQuery.resolvers(new String[]{"0.0.0.0.0.0.0"}));
    }

    @Test
    void canHandleUnableToResolve() {
        DnsQuery.resolvers(new String[]{"0.0.0.0"});
        assertThatExceptionOfType(IOException.class)
            .isThrownBy(() -> DnsQuery.dig(getDomain(VALID_DOMAIN), "A"));
    }

    private Domain getDomain(String name) {
        return Domain.fromString(name).orElseThrow();
    }
}

package com.developerdan.blocklist.tools;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DnsTest {

    static String VALID_DOMAIN = "example.com";
    static String NX_DOMAIN = "this-is-not-a-real-domain-12452345.example.com";

    @BeforeEach
    private void setDefaultResolver() {
        Dns.resolvers(new String[]{});
    }

    @Test
    public void canResolveValidDomainUsingDefaultResolver() throws IOException {
        String result = Dns.dig(getDomain(VALID_DOMAIN), "A");
        assertThat(result).contains(VALID_DOMAIN);
        assertThat(result).contains("NOERROR");
    }

    @Test
    public void canResolveNxDomainUsingDefaultResolver() throws IOException {
        String result = Dns.dig(getDomain(NX_DOMAIN), "A");
        assertThat(result).contains(NX_DOMAIN);
        assertThat(result).contains("NXDOMAIN");
    }

    @Test
    public void canResolveAAAA() throws IOException {
        String result = Dns.dig(getDomain(VALID_DOMAIN), "AAAA");
        assertThat(result).contains(VALID_DOMAIN);
        assertThat(result).contains("NOERROR");
    }

    @Test
    public void canDefineDifferentResolver() {
        Dns.resolvers(new String[]{"1.1.1.1", "8.8.8.8"});
        Dns.resolvers(new String[]{"1.1.1.1:53", "8.8.8.8:53"});
    }

    @Test
    public void canNotDefineInvalidResolver() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Dns.resolvers(new String[]{"0.0.0.0.0.0.0"}));
    }

    @Test
    public void canHandleUnableToResolve() {
        Dns.resolvers(new String[]{"0.0.0.0"});
        assertThatExceptionOfType(IOException.class)
            .isThrownBy(() -> Dns.dig(getDomain(VALID_DOMAIN), "A"));
    }

    private Domain getDomain(String name) {
        return Domain.fromString(name).orElseThrow();
    }
}

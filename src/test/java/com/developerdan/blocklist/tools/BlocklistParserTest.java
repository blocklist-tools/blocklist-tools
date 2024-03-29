package com.developerdan.blocklist.tools;

import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class BlocklistParserTest {
    @Test
    public void canParseHostsFile()
    {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var filePath = classloader.getResource("hosts-blocklist.txt").getPath();
        var domains = new HostsParser().parseFile(filePath).getRecords()
            .stream()
            .map(Domain::toString)
            .collect(Collectors.toSet());
        assertThat(domains).contains("example.com");
        assertThat(domains).contains("multiple-spaces.example.com");
        assertThat(domains).contains("tabs-leading.example.com");
        assertThat(domains).contains("tabs-trailing.example.com");
        assertThat(domains).contains("xn--mxail5aa.com");
        assertThat(domains).doesNotContain("1.αρριϵ.com");
        assertThat(domains).contains("1.xn--mxail5aa.com");
        assertThat(domains).contains("trailing-whitespace.example.com");
        assertThat(domains).contains("trailing-tab.example.com");
        assertThat(domains).contains("up-case.example.com");
        assertThat(domains).contains("different-ip.example.com");
        assertThat(domains).doesNotContain("-leading-dash.example.com");
        assertThat(domains).doesNotContain(".leading-dot.example.com");
        assertThat(domains).doesNotContain("no-ip.example.com");
        assertThat(domains).contains("no-space-comment.example.com");
        assertThat(domains).contains("short-ip-v6.example.com");
        assertThat(domains).contains("colon-ip-v6.example.com");
    }

    @Test
    void canParseDomainListFile()
    {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var filePath = classloader.getResource("domain-blocklist.txt").getPath();
        var domains = new DomainListParser().parseFile(filePath).getRecords()
            .stream()
            .map(Domain::toString)
            .collect(Collectors.toSet());
        assertThat(domains).contains("example.com");
        assertThat(domains).contains("multiple-spaces.example.com");
        assertThat(domains).contains("tabs-leading.example.com");
        assertThat(domains).contains("tabs-trailing.example.com");
        assertThat(domains).contains("xn--mxail5aa.com");
        assertThat(domains).doesNotContain("1.αρριϵ.com");
        assertThat(domains).contains("1.xn--mxail5aa.com");
        assertThat(domains).contains("trailing-whitespace.example.com");
        assertThat(domains).contains("trailing-tab.example.com");
        assertThat(domains).contains("up-case.example.com");
        assertThat(domains).doesNotContain("different-ip.example.com");
        assertThat(domains).doesNotContain("-leading-dash.example.com");
        assertThat(domains).doesNotContain(".leading-dot.example.com");
        assertThat(domains).doesNotContain("with-ip.example.com");
        assertThat(domains).contains("no-space-comment.example.com");
    }

    @Test
    void willThrowOnMissingFile()
    {
        assertThatExceptionOfType(UncheckedIOException.class)
            .isThrownBy(() -> new DomainListParser().parseFile("/not-a-real-file.txt"));
    }

    @Test
    void willSortUnsortedLists()
    {
        String[] unsorted = {
            "c.example.com",
            "a.example.com",
            "b.example.com",
            "a.example.com",
            "a.example.edu"
        };
        var sortedDomains = new DomainListParser().parseStream(Arrays.stream(unsorted));
        var sortedStrings = sortedDomains.getRecords()
                                .stream()
                                .map(Domain::toString)
                                .collect(Collectors.toList());
        assertThat(sortedStrings).containsExactly(
            "a.example.com",
            "b.example.com",
            "c.example.com",
            "a.example.edu"
        );
    }

    @Test
    void canDownloadAndParseList()
    {
        var url = "https://www.github.developerdan.com/hosts/lists/ads-and-tracking-extended.txt";
        var domains = new HostsParser().parseUrl(url).getRecords()
                            .stream()
                            .map(Domain::toString)
                            .collect(Collectors.toSet());
        assertThat(domains).contains("block-test.developerdan.com");
    }
}

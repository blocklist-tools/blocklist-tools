package com.developerdan.blocklist.tools;

import com.developerdan.blocklist.tools.exceptions.DownloadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class DomainParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Domain.class);

    abstract public Optional<Domain> parseLine(String line);

    public final NavigableSet<Domain> parseUrl(String url) {
        try {
            LOGGER.info("Downloading blocklist from {}", url);
            var response = httpClient().send(httpRequest(url), HttpResponse.BodyHandlers.ofLines());
            return parseStream(response.body());
        } catch (Throwable ex) {
            LOGGER.error("Unable to download blocklist {}", url);
            throw new DownloadException(ex);
        }
    }

    public final NavigableSet<Domain> parseFile(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
            return parseStream(stream.parallel());
        } catch (IOException ex) {
            LOGGER.error("Unable to read file {}", fileName);
            throw new UncheckedIOException(ex);
        }
    }

    public final NavigableSet<Domain> parseStream(Stream<String> stream) {
        return stream
                .map(this::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private HttpClient httpClient() {
        return HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(60))
            .build();
    }

    private HttpRequest httpRequest(String url) {
        return HttpRequest
            .newBuilder()
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:77.0) Gecko/20100101 Firefox/77.0")
            .uri(URI.create(url))
            .GET()
            .build();
    }
}

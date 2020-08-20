package com.developerdan.blocklist.tools;

import com.developerdan.blocklist.tools.exceptions.BlocklistToolsRuntimeException;
import com.developerdan.blocklist.tools.exceptions.DownloadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BlocklistParser<E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlocklistParser.class);

    abstract public Optional<E> parseLine(String line);

    public final ParsedList<E> parseUrl(String url) {
        try {
            LOGGER.info("Downloading blocklist from {}", url);
            var response = httpClient().send(httpRequest(url), HttpResponse.BodyHandlers.ofLines());
            return parseStream(response.body());
        } catch (Throwable ex) {
            LOGGER.error("Unable to download blocklist {}", url);
            throw new DownloadException(ex);
        }
    }

    public final ParsedList<E> parseFile(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
            return parseStream(stream);
        } catch (IOException ex) {
            LOGGER.error("Unable to read file {}", fileName);
            throw new UncheckedIOException(ex);
        }
    }

    protected final ParsedList<E> parseStream(Stream<String> stream) {
        var streamDigest = messageDigest();
        var parsedDigest = messageDigest();
        var items = stream
                .peek((line) -> streamDigest.update(line.getBytes(StandardCharsets.UTF_8)))
                .map(this::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek((parsed) -> parsedDigest.update(parsed.toString().getBytes(StandardCharsets.UTF_8)))
                .collect(Collectors.toCollection(TreeSet::new));
        return new ParsedList<E>(items, byteArrayToHex(parsedDigest.digest()), byteArrayToHex(streamDigest.digest()));
    }

    private static MessageDigest messageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new BlocklistToolsRuntimeException(ex);
        }
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
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
            .version(HttpClient.Version.HTTP_2)
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:78.0) Gecko/20100101 Firefox/78.0")
            .header("Accept", "text/html,text/plain")
            .uri(URI.create(url))
            .GET()
            .build();
    }
}

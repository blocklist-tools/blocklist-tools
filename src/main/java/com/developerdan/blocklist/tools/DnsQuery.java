package com.developerdan.blocklist.tools;

import org.xbill.DNS.DClass;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DnsQuery {

    private static Resolver resolver;

    public static void resolvers(final String[] serverAddresses) {
        var newResolvers = new ArrayList<Resolver>();
        for (String address: serverAddresses) {
            var parts = address.split(":", -1);
            var ip = parts[0];
            try {
                var newResolver = new SimpleResolver(ip);
                if (parts.length == 2) {
                    var port = parts[1];
                    newResolver.setPort(Integer.parseInt(port));
                }
                newResolvers.add(newResolver);
            } catch (UnknownHostException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        if (newResolvers.isEmpty()) {
            try {
                newResolvers.add( new SimpleResolver());
            } catch (UnknownHostException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        resolver = new ExtendedResolver(newResolvers);
    }

    public static DnsResponse dig(final Domain name, final String queryTpe) throws IOException {
        var domainName = name.toString() + ".";
        try {
            Record question = Record.newRecord(Name.fromString(domainName), Type.value(queryTpe), DClass.IN);
            Message query = Message.newQuery(question);
            var response = getResolver().send(query);
            return DnsResponse.fromMessage(response);
        } catch (TextParseException ex) {
            throw new IllegalArgumentException("Invalid domain name: " + name, ex);
        }
    }

    private static Resolver getResolver() {
        if (resolver == null){
            resolvers(new String[]{});
        }
        return resolver;
    }
}

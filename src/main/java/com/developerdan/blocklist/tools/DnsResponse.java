package com.developerdan.blocklist.tools;

import org.xbill.DNS.Message;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Type;

public class DnsResponse {
    private String rcode;
    private String response;
    private String queryType;
    private String queryName;

    public static DnsResponse fromMessage(Message message) {
        var rCodeValue = Rcode.string(message.getRcode());
        var question = message.getQuestion();
        var queryName = question.getName().toString();
        if (queryName.endsWith(".")) {
            queryName = queryName.substring(0, queryName.length() - 1);
        }
        var queryType = Type.string(question.getType());
        return new DnsResponse(rCodeValue, message.toString(), queryName, queryType);
    }

    private DnsResponse(String rcode, String response, String queryName, String queryType) {
        this.rcode = rcode;
        this.response = response;
        this.queryName = queryName;
        this.queryType = queryType;
    }

    public String getRcode() {
        return rcode;
    }

    public String getResponse() {
        return response;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getQueryName() {
        return queryName;
    }
}

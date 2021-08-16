package com.misa.scraper.config;

import lombok.Builder;
import lombok.Getter;

import java.util.Properties;

@Getter
public class MailConfig {
    private final String host;
    private final String protocol;
    private final String port;
    private final Boolean auth;
    private final Boolean ssl;
    private final String sslTrustRegEx;

    private final Properties properties;

    public Properties getConfigAsProperties(){
        properties.setProperty("mail.store.protocol", protocol);
        properties.setProperty("mail.pop3s.host", host);
        properties.setProperty("mail.pop3s.port", port);
        properties.setProperty("mail.pop3s.auth", auth.toString());
        properties.setProperty("mail.pop3.ssl.enable", ssl.toString());
        properties.setProperty("mail.pop3s.ssl.trust", sslTrustRegEx);

        return properties;
    }

    @Builder
    public MailConfig(String host, String protocol, String port, Boolean auth, Boolean ssl, String sslTrustRegEx) {
        this.host = host;
        this.protocol = protocol;
        this.port = port;
        this.auth = auth;
        this.ssl = ssl;
        this.sslTrustRegEx = sslTrustRegEx;
        this.properties = new Properties();
    }
}

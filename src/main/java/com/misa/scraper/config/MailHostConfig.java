package com.misa.scraper.config;

import lombok.Builder;
import lombok.Getter;

import javax.mail.*;
import javax.mail.search.FlagTerm;

public class MailHostConfig {
    private Session emailSession;
    private String folderName;
    private MailCredentials mailCredentials;
    private MailConfig mailConfig;
    @Getter
    private Store store;
    @Getter
    private Folder folder;
    @Getter
    private FlagTerm flagTerm;

    public void configure() throws MessagingException {
        this.emailSession = Session.getDefaultInstance(mailConfig.getConfigAsProperties());
        this.store = emailSession.getStore(mailConfig.getProtocol());
        this.store.connect(mailConfig.getHost(), mailCredentials.getUsername(), mailCredentials.getPassword());
        this.folder = store.getFolder("INBOX");
        this.folder.open(Folder.READ_WRITE);
    }

    @Builder
    public MailHostConfig(String folderName, MailCredentials mailCredentials, MailConfig mailConfig, FlagTerm flagTerm) {
        this.folderName = folderName;
        this.mailCredentials = mailCredentials;
        this.mailConfig = mailConfig;
        this.flagTerm = flagTerm;
    }

}

package com.misa.scraper;

import com.misa.scraper.config.MailHostConfig;
import com.misa.scraper.config.MailConfig;
import com.misa.scraper.config.MailCredentials;
import com.misa.scraper.controller.ScraperController;
import com.misa.scraper.entity.Mail;
import com.misa.scraper.util.GetProperties;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.List;

public class MailScraper {
    public static void main(String[] args) {

        MailCredentials credentials = MailCredentials.builder()
                .username(args[0])
                .password(args[1]).build();

        MailConfig config = MailConfig.builder()
                .host(GetProperties.get("mail_config.host"))
                .protocol(GetProperties.get("mail_config.protocol"))
                .port(GetProperties.get("mail_config.port"))
                .auth(true)
                .ssl(true)
                .sslTrustRegEx("*").build();

        MailHostConfig mailHostConfig = MailHostConfig.builder()
                .mailConfig(config)
                .mailCredentials(credentials)
                .folderName("INBOX")
                .flagTerm(new FlagTerm(new Flags(Flags.Flag.SEEN), false))
                .build();

        List<Mail> mails = new ArrayList<>();
        try {
            mailHostConfig.configure();

            ScraperController scraperController = new ScraperController(mailHostConfig);
            mails = scraperController.getNewMails();

            mailHostConfig.getFolder().close(false);
            mailHostConfig.getStore().close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        if(mails.isEmpty()) {
            System.out.println("No new mails");
        } else {
            System.out.println("New emails are: ");
            for (Mail mail : mails) {
                System.out.println(mail);
            }
        }
    }
}
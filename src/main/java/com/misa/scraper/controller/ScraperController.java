package com.misa.scraper.controller;

import com.misa.scraper.config.MailHostConfig;
import com.misa.scraper.dao.MailDAOImpl;
import com.misa.scraper.entity.Mail;
import lombok.Getter;
import lombok.Setter;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScraperController {
    private List<Mail> mails;
    @Getter
    @Setter
    private MailHostConfig mailHostConfig;

    public ScraperController(MailHostConfig mailHostConfig) {
        this.mailHostConfig = mailHostConfig;
    }

    public List<Mail> getAllMails() {
        readFromGmail();
        return mails;
    }

    private void readFromGmail() {
        mails = new ArrayList<>();
        try {
            Message[] messages = mailHostConfig.getFolder().search(mailHostConfig.getFlagTerm());
            for(Message message : messages){
                Mail mail = convertMessageToMail(message);
                mails.add(mail);
            }
        }catch (MessagingException | IOException e){
            e.printStackTrace();
        }
    }

    private void readFromGmailAndSave() {
        readFromGmail();
        for(Mail mail : mails)
            new MailDAOImpl().save(mail);
    }

    private Mail convertMessageToMail(Message message) throws MessagingException, IOException {
        Mail mail = Mail.builder()
                .id(0)
                .name(null)
                .recipient(null)
                .subject(message.getSubject())
                .content(message.getContent().toString())
                .sentDate(message.getSentDate().toInstant().toString())
                .build();
        mail.setName(readNameFromAddress(message.getFrom()));
        mail.setRecipient(readEmailFromAddress(message.getFrom()));

        return mail;
    }

    private String readEmailFromAddress(Address[] from) {
        for(Address address : from){
            String recipient = address.toString();
            Pattern pattern = Pattern.compile("(?<=\\<).+?(?=\\>)");
            Matcher matcher = pattern.matcher(recipient);
            if (matcher.find())
                return matcher.group(0);
        }

        return null;
    }

    private String readNameFromAddress(Address[] from) {
        for(Address address : from){
            String recipient = address.toString();
            Pattern pattern = Pattern.compile(".+?(?=\\ <)");
            Matcher matcher = pattern.matcher(recipient);
            if (matcher.find())
                return matcher.group(0);
        }

        return null;
    }

    public List<Mail> getNewMails(){
        List<Mail> filtered = filterMails();
        new MailDAOImpl().saveAll(filtered);

        return filtered;
    }

    private List<Mail> filterMails() {
        readFromGmail();
        List<Mail> filtered = new ArrayList<>();
        List<Mail> persisted = new MailDAOImpl().getAll();
        int count = 0;
        for(Mail mailRead : mails){
            count = 0;
            for(Mail mailPersisted : persisted){
                if(mailRead.compareTo(mailPersisted) == 0) {
                    count++;
                    break;
                }
            }
            if(count == 0)
                filtered.add(mailRead);
        }

        return filtered;
    }
}

package com.misa.scraper.dao;

import com.misa.scraper.entity.Mail;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MailDAO {
    Optional<Mail> getById(int id);
    List<Mail> getByRecipient(String recipient);
    List<Mail> getAll();
    List<Mail> getByDate(Instant instant);
    public Mail getLatest();
    boolean save(Mail mail);
}

package com.misa.scraper.dao;

import com.misa.scraper.db.DataSource;
import com.misa.scraper.entity.Mail;

import java.sql.*;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

public class MailDAOImpl implements MailDAO {

    private final Connection connection;

    public MailDAOImpl() {
        this.connection = DataSource.getInstance().openConnection();
    }

    @Override
    public Optional<Mail> getById(int mailId) {
        try {
            PreparedStatement getById = connection.prepareStatement(
                    "SELECT * FROM mail WHERE id = mailId");
            getById.setInt(1, mailId);
            ResultSet resultSet = getById.executeQuery();

            if(resultSet.next())
                return Optional.ofNullable(getMailFromResultSet(resultSet));
        } catch (SQLException | ParseException e){
            e.printStackTrace();
        } finally {
            DataSource.getInstance().closeConnection();
        }
        return Optional.empty();
    }

    @Override
    public List<Mail> getByRecipient(String recipient) {
        List<Mail> mails = new ArrayList<>();
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(
                    "SELECT * FROM mail WHERE recipient = ?");

            insert.setString(1, recipient);
            ResultSet resultSet = insert.executeQuery();

            while(resultSet.next())
                mails.add(getMailFromResultSet(resultSet));

            return mails;
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            return null;
        }finally {
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public Mail getLatest() {
        try {
            PreparedStatement getCourseById = connection.prepareStatement(
                    "SELECT * FROM mail_scraper.mail ORDER BY sent_date DESC LIMIT 1");
            ResultSet resultSet = getCourseById.executeQuery();
            if(resultSet.next())
                return getMailFromResultSet(resultSet);
        } catch (SQLException | ParseException e){
            e.printStackTrace();
        } finally {
            DataSource.getInstance().closeConnection();
        }
        return null;
    }

    @Override
    public List<Mail> getAll() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mail");

            List<Mail> mails = new ArrayList<>();
            while(resultSet.next())
                mails.add(getMailFromResultSet(resultSet));

            return mails;
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            DataSource.getInstance().closeConnection();
        }

        return null;
    }

    private Mail getMailFromResultSet(ResultSet resultSet) throws SQLException, ParseException {
        Mail mail = Mail.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .recipient(resultSet.getString("recipient"))
                .subject(resultSet.getString("subject"))
                .content(resultSet.getString("content"))
                .sentDate(resultSet.getString("sent_date"))
                .build();

        return mail;
    }

    @Override
    public List<Mail> getByDate(Instant instant) {
        List<Mail> mails = new ArrayList<>();
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(
                    "SELECT * FROM mail WHERE sent_date = ?");

            insert.setString(1, instant.toString());
            ResultSet resultSet = insert.executeQuery();

            while(resultSet.next())
                mails.add(getMailFromResultSet(resultSet));

            return mails;
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            return null;
        }finally {
            DataSource.getInstance().closeConnection();
        }
    }

    @Override
    public boolean save(Mail mail) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(
                    "INSERT INTO mail (name, recipient, subject, content, sent_date) VALUES (?, ?, ?, ?, ?)");

            insert.setString(1, mail.getName());
            insert.setString(2, mail.getRecipient());
            insert.setString(3, mail.getSubject());
            insert.setString(4, mail.getContent());
            insert.setString(5, mail.getSentDate());

            int affectedRows = insert.executeUpdate();
            return affectedRows == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            DataSource.getInstance().closeConnection();
        }
    }

    public void saveAll(List<Mail> mails){
        for(Mail mail : mails) {
            try {
                new MailDAOImpl().save(mail);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

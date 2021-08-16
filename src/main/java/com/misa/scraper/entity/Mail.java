package com.misa.scraper.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Mail implements Comparable<Mail> {
    private int id;
    private String name;
    private String recipient;
    private String subject;
    private String content;
    private String sentDate;

    @Override
    public int compareTo(Mail o) {
        if(
                this.getName().equals(o.getName()) &&
                this.getRecipient().equals(o.getRecipient()) &&
                this.getSentDate().equals(o.getSentDate()) &&
                this.getSubject().equals(o.getSubject())
        )
            return 0;
        else
            return 1;
    }
}

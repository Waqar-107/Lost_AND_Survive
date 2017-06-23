package com.wordpress.farhantanvirutshaw.lostsurvive.gmail;

/**
 * Created by utshaw on 6/23/17.
 */

public class Email {

    private String mailSender = "level2term1@gmail.com";
    private String mailRecipient = "buetmatrix@gmail.com";
    private String subject = "";
    private String bodyText = "";

    public Email(String mailSender, String mailRecipient) {
        this.mailSender = mailSender;
        this.mailRecipient = mailRecipient;
    }

    public Email(String mailSender, String mailRecipient, String subject, String bodyText) {
        this.mailSender = mailSender;
        this.mailRecipient = mailRecipient;
        this.subject = subject;
        this.bodyText = bodyText;
    }
}

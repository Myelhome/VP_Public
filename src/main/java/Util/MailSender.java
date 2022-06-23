package Util;

import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.*;

public class MailSender {
    private static final String MAIL_SENDER_KEY = "mail.sender";
    private static final String MAIL_PASSWORD_KEY = "mail.password";

    private static void sendMailBase(String mailSubject, String mailText, String receiverAddress) {
        try {
            Properties props = new Properties();

            props.put("mail.transport.protocol", "smtps");
            props.put("mail.smtps.host", "smtp.mail.ru");
            props.put("mail.smtps.auth", "true");
            props.put("mail.smtp.sendpartial", "true");
            props.put("mail.smtp.ssl.enable", "true");

            Session session = Session.getDefaultInstance(props);

            MimeMessage message = new MimeMessage(session);

            message.setSubject(mailSubject);
            message.setText(mailText +
                    "\n" +
                    "\n This message was generated automatically" +
                    "\n Please, do not reply to this message");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverAddress));
            message.setSentDate(new Date());
            message.addFrom(new Address[]{new InternetAddress(PropertiesSettingsUtil.get(MAIL_SENDER_KEY))});

            Transport transport = session.getTransport();
            transport.connect(PropertiesSettingsUtil.get(MAIL_SENDER_KEY), PropertiesSettingsUtil.get(MAIL_PASSWORD_KEY));

            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void approvalMail(String receiverAddress, String startDate, String endDate) {
        sendMailBase("Заявка на отпуск",
                "Вашу заявку на отпуск от " + startDate + " по " + endDate + " одобрили." +
                        "\nЖелаем приятного отдыха!",
                receiverAddress);
    }

    public static void declineMail(String receiverAddress, String startDate, String endDate, String declineReason) {
        sendMailBase("Заявка на отпуск",
                "Ваша заявка на отпуск от " + startDate + " по " + endDate + " была отклоненна." +
                        "\nПо причине:\n" + declineReason,
                receiverAddress);
    }

    public static void vacationStartMail(String receiverAddress, String startDate, String endDate) {
        sendMailBase("Заявка на отпуск",
                "Ваш отпуск начался: " + startDate + " , и продлиться до " + endDate + " включительно." +
                        "\nЖелаем приятного отдыха!",
                receiverAddress);
    }

    public static void vacationEndMail(String receiverAddress, int duration, String endDate) {
        sendMailBase("Заявка на отпуск",
                "Ваш отпуск закончился: " + endDate + " , и продлился " + duration + " дней." +
                        "\nС возвращением!",
                receiverAddress);
    }
}
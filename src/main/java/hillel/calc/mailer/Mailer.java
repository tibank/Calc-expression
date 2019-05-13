package hillel.calc.mailer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Mailer {
    private String to;
    private String from;
    private String subject;
    private ConfMailer confMail;

    public Mailer(String to, String from, String subject) {
        this.to = to;
        this.from = from;
        this.subject = subject;
    }

    public int sendMsg(String fileName) {

        Session session;
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", getConfMail().getHost());

        // Get the default Session object.
        if (getConfMail().isUseSSL()) {
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.port", "465");
            properties.setProperty("mail.smtp.ssl.enable", "true");

            session = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        // override the getPasswordAuthentication
                        // method
                        protected PasswordAuthentication
                        getPasswordAuthentication() {
                            return new PasswordAuthentication(getConfMail().getUser(), getConfMail().getPassword());
                        }
                    });
        } else {
            session = Session.getDefaultInstance(properties);
        }


        // Create a MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Create a multipart message
        Multipart multipart = new MimeMultipart();
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();
        // make DataSourse object for
        DataSource source = new FileDataSource(fileName);

        try {
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(getFrom()));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(getTo()));
            // Set Subject: header field
            message.setSubject(getSubject());
            // Make attachment
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
            // Send the complete message parts
            message.setContent(multipart );
            // Send message
            Transport.send(message);
            return 1;
        } catch (AddressException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public ConfMailer getConfMail() {
        return confMail;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setConfMail(ConfMailer confMail) {
        this.confMail = confMail;
    }
}

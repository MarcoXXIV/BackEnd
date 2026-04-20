package com.progetto.ingsw.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendNewPasswordEmail(String toEmail, String nome, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Reset password - Nuova password temporanea");
        message.setText(
                "Ciao " + (nome != null ? nome : "") + ",\n\n" +
                        "abbiamo generato una nuova password temporanea per il tuo account.\n\n" +
                        "Nuova password: " + newPassword + "\n\n" +
                        "Accedi con questa password e cambiala appena entri nel profilo.\n\n" +
                        "Se non hai richiesto tu questa operazione, contatta subito il supporto.\n\n" +
                        "Saluti."
        );

        mailSender.send(message);
    }
}

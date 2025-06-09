package com.msa.emailing.controller;

import com.msa.emailing.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.internal.Logger;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/email")
public class EmailController {

    private static Logger logger = Logger.getLogger(EmailController.class.getName());

    @Autowired
    public EmailService emailService;

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestPart("attachment") MultipartFile attachement
            ) throws IOException, MessagingException {

        logger.warn("Received Attachment: " + attachement.getOriginalFilename());
        File file = convertMultipartFileToPdf(attachement);
        emailService.sendEmail(to, subject, body, file);
        if(file.exists()) {
            file.delete();
        }

        return new ResponseEntity<String>("Email Sent!", HttpStatus.OK);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(
            @RequestParam String to,
            @RequestParam String otp
    ) throws MessagingException {
        File file = blankFile();
        emailService.sendEmail(
                to,
                "Email Update/ Forgot Password OTP",
                otp,
                file
        );
        return new ResponseEntity<String>("Email Sent!", HttpStatus.OK);
    }

    private File blankFile() {
        return new File("blank");
    }

    private File convertMultipartFileToPdf(MultipartFile attachement) throws IOException {
        File tempFile = File.createTempFile("P-OD-", ".pdf");
        attachement.transferTo(tempFile);
        return tempFile;
    }
}

package com.mauricio.apimonitoring.service

import com.mauricio.apimonitoring.dto.EmailRequest
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {

    private val LOG = LoggerFactory.getLogger(EmailService::class.java)

    fun sendEmail(request: EmailRequest) {
        try {
            val message = SimpleMailMessage()
            message.setTo(request.to)
            message.subject = request.subject
            message.text = request.body

            mailSender.send(message)

            LOG.info("Email enviado para ${request.to}")

        } catch (ex: Exception) {
            LOG.error("Erro ao enviar email: ${ex.message}")
            throw RuntimeException("Falha ao enviar email")
        }
    }
}
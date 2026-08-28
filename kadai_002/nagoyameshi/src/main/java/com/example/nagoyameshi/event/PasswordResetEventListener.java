package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.PasswordResetTokenService;

@Component
public class PasswordResetEventListener {

	private final PasswordResetTokenService passwordResetTokenService;
	private final JavaMailSender javaMailSender;

	public PasswordResetEventListener(PasswordResetTokenService passwordResetTokenService, JavaMailSender javaMailSender) {
		
		this.passwordResetTokenService = passwordResetTokenService;
		this.javaMailSender = javaMailSender;
	}

	@EventListener
	public void onPasswordResetEvent(PasswordResetEvent passwordResetEvent) {

		User user = passwordResetEvent.getUser();
		String token = UUID.randomUUID().toString(); //トークンの生成

		passwordResetTokenService.create(user, token);

		String senderAddress = "samuraitest@sandboxf08c3625b2494f98a493b6a4cf521492.mailgun.org"; //送信用アドレス
		String recipientAddress = user.getEmail(); //宛先アドレス
		String subject = "パスワードのリセット"; //件名
		String confirmationUrl = passwordResetEvent.getRequestUrl() + "/edit?token=" + token; //認証確認用のURL組み立て
		String message = "以下のリンクをクリックしてパスワードのリセットを完了してください。"; //メール文

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}

}

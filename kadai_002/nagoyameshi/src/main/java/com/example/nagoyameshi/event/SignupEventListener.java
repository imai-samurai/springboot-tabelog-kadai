package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.VerificationTokenService;

@Component
public class SignupEventListener {

	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;

	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender javaMailSender) {

		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = javaMailSender;
	}

	@EventListener
	public void onSignupEvent(SignupEvent signupEvent) {

		User user = signupEvent.getUser();
		String token = UUID.randomUUID().toString(); //トークンの生成

		verificationTokenService.create(user, token); //登録

		String senderAddress = "samuraitest@sandboxf08c3625b2494f98a493b6a4cf521492.mailgun.org"; //送信用アドレス
		String recipientAddress = user.getEmail(); //宛先アドレス
		String subject = "メール認証"; //件名
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token; //認証確認用のURL組み立て
		String message = "以下のリンクをクリックして会員登録を完了してください。"; //メール文

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}

}

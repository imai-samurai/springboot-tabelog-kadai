package com.example.nagoyameshi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController {
	
	private final StripeService stripeService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	
	@Value("${stripe.webhook-secret}")
	private String webhookSecret;
	
	public StripeWebhookController(StripeService stripeService, UserRepository userRepository, RoleRepository roleRepository) {
		this.stripeService = stripeService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@PostMapping("/stripe/webhook")
	public ResponseEntity<String> webhook(@RequestBody String payload,
											@RequestHeader("Stripe-Signature") String sigHeader) {
		
		Stripe.apiKey = stripeApiKey;
		Event event = null;
		
		try {
			
			event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
			
		} catch(SignatureVerificationException e) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			
		}
		
		if ("checkout.session.completed".equals(event.getType())) {
			
			Session session = stripeService.processSessionCompleted(event);
			
			String userId = session.getMetadata().get("userId");
			String stripeId = session.getCustomer();
			
			User user = userRepository.findById(Integer.parseInt(userId)).orElse(null);
			
			user.setRole(roleRepository.getReferenceById(2));
			user.setStripeCustomerId(stripeId);
			
			userRepository.save(user);
			
		} else if("customer.subscription.deleted".equals(event.getType())) {
			
			Subscription subscription = stripeService.processSubscriptionDeleted(event);
			
			if(subscription != null) {
				
				String stripeCustomerId = subscription.getCustomer();
				User user = userRepository.findByStripeCustomerId(stripeCustomerId).orElse(null);
				
				if(user != null) {
					user.setRole(roleRepository.getReferenceById(1));
					userRepository.save(user);
					System.out.println("【Stripe Webhook】ユーザー名: " + user.getName() + " の解約処理（無料会員への自動ダウングレード）が完了しました。");
					
				}else {
					System.out.println("【Stripe Webhook警告】解約通知を受信しましたが、Stripe顧客ID（" + stripeCustomerId + "）に対応するユーザーがDB内に見つかりません。");
				}
			}
		}
		
		return new ResponseEntity<>("Success", HttpStatus.OK);
		
	}
	
}

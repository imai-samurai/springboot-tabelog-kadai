package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	
	private final VerificationTokenRepository verificationTokenRepository;
	
	public VerificationTokenService(VerificationTokenRepository verificationTokenRepositor) {
		this.verificationTokenRepository = verificationTokenRepositor;
	}
	
	//トークンを登録する
	@Transactional
	public void create(User user, String token) {
		
		VerificationToken existingToken = verificationTokenRepository.findByUser(user);
		
		if(existingToken != null) {
			verificationTokenRepository.delete(existingToken);
			verificationTokenRepository.flush();
		}
		
		VerificationToken verificationToken = new VerificationToken();
		
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		
		verificationTokenRepository.save(verificationToken);
	}
	
	//トークンの文字列で検索した結果を返す
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}
	
}

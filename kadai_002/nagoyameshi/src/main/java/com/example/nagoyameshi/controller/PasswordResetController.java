package com.example.nagoyameshi.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.PasswordResetToken;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.event.PasswordResetEventPublisher;
import com.example.nagoyameshi.repository.PasswordResetTokenRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.PasswordResetTokenService;
import com.example.nagoyameshi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login/passreset")
public class PasswordResetController {
	
	private final PasswordResetTokenService passwordResetTokenService;
	private final PasswordResetEventPublisher passwordResetEventPublisher;
	private final UserRepository userRepository;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	
	public PasswordResetController(PasswordResetTokenService passwordResetTokenService,
									PasswordResetEventPublisher passwordResetEventPublisher,
									UserRepository userRepository,
									UserService userService,
									PasswordEncoder passwordEncoder,
									PasswordResetTokenRepository passwordResetTokenRepository) {
		
		this.passwordResetTokenService = passwordResetTokenService;
		this.passwordResetEventPublisher = passwordResetEventPublisher;
		this.userRepository = userRepository;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
	}
	
	//パスワードリセット画面(email入力)へ遷移
	@GetMapping("")
	public String passReset() {
		return "user/email";
	}
	
	//メール送信
	@PostMapping("")
	public String sendResetMail(@RequestParam(name = "email", required = false) String email,
								RedirectAttributes redirectAttributes,
								HttpServletRequest httpServletRequest,
								Model model) {
		
		
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			model.addAttribute("errorMessage", "このメールアドレスは登録されていません。");
			return "user/email";
		}
		
		String requestUrl = new String(httpServletRequest.getRequestURL()); //動的にURLを取得する
		passwordResetEventPublisher.publishPasswordResetEvent(user, requestUrl);
		
		redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、パスワードの変更をしてください。");
		return "redirect:/";
	}
	
	//パスワード編集画面へ遷移
	@GetMapping("/edit")
	public String editPasswordForm(@RequestParam(name = "token") String token,
									RedirectAttributes redirectAttributes,
									Model model) {
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(token);
		
		if(passwordResetToken != null) {
			User user = passwordResetToken.getUser();
			model.addAttribute("user", user);
			model.addAttribute("token", token);
			
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "リンクが無効です。");
			return "redirect:/login";
		}
		
		return "user/reset";
	}
	
	//パスワードの更新
	@PostMapping("/update") 
	public String update(@RequestParam(name = "token") String token,
						@RequestParam(name = "password") String password,
						@RequestParam(name = "passwordConfirmation") String passwordConfirmation,
						RedirectAttributes redirectAttributes,
						Model model) {
		
		if (!userService.isSamePassword(password, passwordConfirmation)) {
			model.addAttribute("token", token);
			model.addAttribute("errorMessage", "パスワードが一致していません。");
			return "user/reset";
		}
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(token);
		
		if(passwordResetToken != null) {
			User user = passwordResetToken.getUser();
			user.setPassword(passwordEncoder.encode(password));//パスワードをハッシュ化してセットする
			userRepository.save(user);
			passwordResetTokenRepository.delete(passwordResetToken);//トークン削除
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "リンクが無効です。");
			return "redirect:/login";
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "パスワードを変更しました。");
		return "redirect:/login";
	}
	
}

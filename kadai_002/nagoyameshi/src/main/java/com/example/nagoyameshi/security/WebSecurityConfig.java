package com.example.nagoyameshi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Order(1)
	@Bean
	public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http, AdminDetailsServiceImpl adminDetailsService) throws Exception {
		http
				.userDetailsService(adminDetailsService)
				.securityMatcher("/admin/**")
		
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/admin/login").permitAll() // すべてのユーザーにアクセスを許可するURL
						.requestMatchers("/admin/**").hasRole("ADMIN") // 管理者にのみアクセスを許可するURL
						.anyRequest().authenticated() // 上記以外のURLはログインが必要
					)
				.formLogin((form) -> form
						.loginPage("/admin/login") // ログインページのURL
						.loginProcessingUrl("/admin/login") // ログインフォームの送信先URL
						.defaultSuccessUrl("/admin?loggedIn", true) // ログイン成功時のリダイレクト先URL
						.failureUrl("/admin/login?error") // ログイン失敗時のリダイレクト先URL
						.permitAll())
				.logout((logout) -> logout
						.logoutUrl("/admin/logout")
						.logoutSuccessUrl("/admin/?loggedOut") // ログアウト時のリダイレクト先URL
						.permitAll()
						);
		
		return http.build();
	}

	@Order(2)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsServiceImpl userDetailsService) throws Exception {
		http
				
				.userDetailsService(userDetailsService)
				
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/favorites/**", "/stores/*/favorites/**", "/stores/*/reviews/**", "/stores/*/reservations/**", "/reservations/**", "/subscription/portal").hasRole("PREMIUM") // 有料会員のみアクセス可能
						.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/login", "/login/passreset/**", "/stores/**", "/stripe/webhook").permitAll() // すべてのユーザーにアクセスを許可するURL           
						.anyRequest().authenticated() // 上記以外のURLはログインが必要
				)
				.formLogin((form) -> form
						.loginPage("/login") // ログインページのURL
						.loginProcessingUrl("/login") // ログインフォームの送信先URL
						.defaultSuccessUrl("/?loggedIn") // ログイン成功時のリダイレクト先URL
						.failureUrl("/login?error") // ログイン失敗時のリダイレクト先URL
						.permitAll())
				.logout((logout) -> logout
						.logoutSuccessUrl("/?loggedOut") // ログアウト時のリダイレクト先URL
						.permitAll()
				)
				.csrf((csrf) -> csrf
						.ignoringRequestMatchers("/stripe/webhook")
				);
		
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

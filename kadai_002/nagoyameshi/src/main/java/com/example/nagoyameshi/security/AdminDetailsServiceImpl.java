package com.example.nagoyameshi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Admin;
import com.example.nagoyameshi.repository.AdminRepository;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {
	
	private final AdminRepository adminRepository;
	
	public AdminDetailsServiceImpl(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			Admin admin = adminRepository.findByEmail(email);
			if (admin == null) {
				throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
			}
			
			return new AdminDetailsImpl(admin);
			
		} catch(Exception e) {
			throw new UsernameNotFoundException("ユーザー処理中にエラーが発生しました。", e);
		}
	}
}

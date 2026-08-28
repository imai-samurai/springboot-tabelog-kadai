package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;

@Service
public class FavoriteService {
	
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	//登録
	@Transactional
	public void create(Store store, User user) {
		
		Favorite favorite = new Favorite();
		
		favorite.setStore(store);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
		
	}
	
	//削除
	@Transactional
	public void delete(Store store, User user) {
		favoriteRepository.deleteByStoreAndUser(store, user);
	}
	
}

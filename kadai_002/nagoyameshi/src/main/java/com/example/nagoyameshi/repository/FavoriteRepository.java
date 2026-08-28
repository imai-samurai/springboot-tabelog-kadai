package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

	//お気に入り一覧用
	Page<Favorite> findByUserOrderByIdDesc(User user, Pageable pageable);

	//店舗詳細でお気に入りされていいるかの判定
	boolean existsByStoreAndUser(Store store, User user);

	//お気に入りを削除する（主キー番号がわからなくてもユーザーと店舗データで削除する）
	void deleteByStoreAndUser(Store store, User user);

}

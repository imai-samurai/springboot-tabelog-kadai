package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
	
	//最初の５件を取得する(トップページ用)
	List<Store> findTop5ByOrderByIdAsc();
	
	//店舗一覧用
	//店舗名であいまい検索（価格安い、高い順）
	public Page<Store> findByNameLikeOrderByPriceLowerAsc(String keyword, Pageable pageable);
	public Page<Store> findByNameLikeOrderByPriceUpperDesc(String keyword, Pageable pageable);
	
	// 予算（指定金額以下）で絞り込み（価格安い順 / 価格高い順）
	public Page<Store> findByPriceLowerLessThanEqualOrderByPriceLowerAsc(Integer price, Pageable pageable);
	public Page<Store> findByPriceLowerLessThanEqualOrderByPriceUpperDesc(Integer price, Pageable pageable);
	
	// カテゴリで絞り込み（価格安い順 / 価格高い順）
	public Page<Store> findByCategories_IdOrderByPriceLowerAsc(Integer categoryId, Pageable pageable);
	public Page<Store> findByCategories_IdOrderByPriceUpperDesc(Integer categoryId, Pageable pageable);
	
	// 検索指定がない全件表示用の並び替えメソッド
	public Page<Store> findAllByOrderByPriceLowerAsc(Pageable pageable);
	public Page<Store> findAllByOrderByPriceUpperDesc(Pageable pageable);
	
	//管理者用
	public Page<Store> findByNameLike(String keyword, Pageable pageable);
	
}

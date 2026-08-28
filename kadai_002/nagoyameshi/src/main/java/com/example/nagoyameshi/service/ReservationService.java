package com.example.nagoyameshi.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;

@Service
public class ReservationService {
	
	private final ReservationRepository reservationRepository;
	
	public ReservationService(ReservationRepository reservationRepository) {
		
		this.reservationRepository = reservationRepository;
		
	}
	
	//予約の登録
	@Transactional
	public void create(ReservationRegisterForm ReservationRegisterForm, Store store, User user) {
		
		Reservation reservation = new Reservation();
		
		reservation.setStore(store);
		reservation.setUser(user);
		reservation.setReservationDate(ReservationRegisterForm.getReservationDate());
		reservation.setReservationTime(ReservationRegisterForm.getReservationTime());
		reservation.setNumberOfPeople(ReservationRegisterForm.getNumberOfPeople());
		
		reservationRepository.save(reservation);
	}
	
	//予約日の判定（過去の日付か？を判定する　tureなら過去の日付）
	public boolean isPastDateTime(LocalDate reservationDate, LocalTime reservationTime) {
		//nullの時は判定をスルーする
		if (reservationDate == null || reservationTime == null) {
		    return false;
		}
		//日付の判定
		if(reservationDate.isBefore(LocalDate.now())) {
			return true;
		}
		//今日で現在時刻よりも前か判定
		if(reservationDate.equals(LocalDate.now()) && reservationTime.isBefore(LocalTime.now())) {
			return true;
		}
		
		return false; // 判定合格 → 予約へ
	}
	
	//営業時間外か判定
	public boolean isOutsideBusinessHours(ReservationRegisterForm reservationRegisterForm, Store store) {
		//nullの時は判定をスルーする
		if(reservationRegisterForm.getReservationTime() == (null)) {
			return false;
		}
		//営業時間前か？
		if(reservationRegisterForm.getReservationTime().isBefore(store.getHoursOpen())) {
			return true;
		}
		//営業時間過ぎか？
		if(reservationRegisterForm.getReservationTime().isAfter(store.getHoursClose())) {
			return true;
		}
		
		return false; // 判定合格 → 予約へ
	}
	
}

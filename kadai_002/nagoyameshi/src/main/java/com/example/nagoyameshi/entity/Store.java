package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stores")
@Data
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToMany
	@JoinTable(name = "store_category", // 中間テーブルの名前
			joinColumns = @JoinColumn(name = "store_id"), // 自分（Store）のIDを結びつけるカラム
			inverseJoinColumns = @JoinColumn(name = "category_id") // 相手（Category）のIDを結びつけるカラム
	)
	private List<Category> categories;

	@Column(name = "name")
	private String name;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "description")
	private String description;

	@Column(name = "price_upper")
	private Integer priceUpper;

	@Column(name = "price_lower")
	private Integer priceLower;

	@Column(name = "hours_open")
	private LocalTime hoursOpen;

	@Column(name = "hours_close")
	private LocalTime hoursClose;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "regular_holiday")
	private String regularHoliday;

	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

}

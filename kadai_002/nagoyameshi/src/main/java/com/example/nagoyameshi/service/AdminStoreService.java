package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.AdminStoreEditForm;
import com.example.nagoyameshi.form.AdminStoreRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.StoreRepository;

@Service
public class AdminStoreService {

	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;

	public AdminStoreService(StoreRepository storeRepository, CategoryRepository categoryRepository) {
		this.storeRepository = storeRepository;
		this.categoryRepository = categoryRepository;
	}

	//管理者店舗登録
	@Transactional
	public void create(AdminStoreRegisterForm adminStoreRegisterForm) {

		Store store = new Store();
		MultipartFile imageFile = adminStoreRegisterForm.getImageFile();

		List<Integer> categoryIds = adminStoreRegisterForm.getCategoryIds();
		List<Category> categories = categoryRepository.findAllById(categoryIds);

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageName(hashedImageName);
		}

		store.setCategories(categories);
		store.setName(adminStoreRegisterForm.getName());
		store.setDescription(adminStoreRegisterForm.getDescription());
		store.setPriceUpper(adminStoreRegisterForm.getPriceUpper());
		store.setPriceLower(adminStoreRegisterForm.getPriceLower());
		store.setHoursOpen(LocalTime.parse(adminStoreRegisterForm.getHoursOpen()));
		store.setHoursClose(LocalTime.parse(adminStoreRegisterForm.getHoursClose()));
		store.setPostalCode(adminStoreRegisterForm.getPostalCode());
		store.setAddress(adminStoreRegisterForm.getAddress());
		store.setPhoneNumber(adminStoreRegisterForm.getPhoneNumber());
		store.setRegularHoliday(adminStoreRegisterForm.getRegularHoliday());

		storeRepository.save(store);

	}

	// UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		// 最右端の拡張子（jpgやpngなど）を特定する
		String extension = fileNames[fileNames.length - 1];
		// 「1つのUUID」と「.」と「拡張子」をシンプルに結合して返す
		return UUID.randomUUID().toString() + "." + extension;
	}

	// 画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//管理者店舗編集
	@Transactional
	public void update(AdminStoreEditForm adminStoreEditForm) {

		Store store = storeRepository.getReferenceById(adminStoreEditForm.getId());
		store.getCategories().clear();

		MultipartFile imageFile = adminStoreEditForm.getImageFile();

		if (adminStoreEditForm.getCategoryIds() != null) {
			List<Integer> categoryIds = adminStoreEditForm.getCategoryIds();
			List<Category> categories = categoryRepository.findAllById(categoryIds);
			store.setCategories(categories);
		}

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageName(hashedImageName);
		}

		store.setName(adminStoreEditForm.getName());
		store.setDescription(adminStoreEditForm.getDescription());
		store.setPriceUpper(adminStoreEditForm.getPriceUpper());
		store.setPriceLower(adminStoreEditForm.getPriceLower());
		store.setHoursOpen(LocalTime.parse(adminStoreEditForm.getHoursOpen()));
		store.setHoursClose(LocalTime.parse(adminStoreEditForm.getHoursClose()));
		store.setPostalCode(adminStoreEditForm.getPostalCode());
		store.setAddress(adminStoreEditForm.getAddress());
		store.setPhoneNumber(adminStoreEditForm.getPhoneNumber());
		store.setRegularHoliday(adminStoreEditForm.getRegularHoliday());

		storeRepository.save(store);

	}

}

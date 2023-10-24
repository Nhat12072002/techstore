package com.shopme.admin.settings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Settings;

public interface SettingRepository extends CrudRepository<Settings, Integer>, PagingAndSortingRepository<Settings, Integer> {
	@Query("SELECT s FROM Settings s WHERE s.name LIKE %?1%")
	public Page<Settings> findAll(String keyword, Pageable pageable);
}

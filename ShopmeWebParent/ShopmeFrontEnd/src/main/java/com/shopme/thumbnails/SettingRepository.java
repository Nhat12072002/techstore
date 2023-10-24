package com.shopme.thumbnails;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Settings;
@Repository
public interface SettingRepository extends CrudRepository<Settings, Integer>  {
	@Query("SELECT s FROM Settings s WHERE  s.enabled =true")
	public List<Settings> findAllEnabled();
}

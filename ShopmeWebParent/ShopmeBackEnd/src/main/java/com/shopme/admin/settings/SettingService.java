package com.shopme.admin.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.settings.SettingRepository;
import com.shopme.common.entity.Settings;
@Service

public class SettingService {
	@Autowired
	private SettingRepository repo;
	public List<Settings> listAll(){
		return (List<Settings>) repo.findAll();
	}
	public Settings save(Settings settings) {
		return repo.save(settings);
	}
}

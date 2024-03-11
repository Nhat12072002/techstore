package com.shopme.admin.settings;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.settings.SettingRepository;
import com.shopme.admin.users.UserNotFoundException;
import com.shopme.common.entity.Settings;
import com.shopme.common.entity.User;
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
	public Settings get(Integer id) throws SettingNotFoundException {
		// TODO Auto-generated method stub
		try {
		return repo.findById(id).get();
		}catch (NoSuchElementException ex) {
			throw new SettingNotFoundException("Could not find any thumbnail with ID: "+id);
		}
	}
	public void delete(Integer id) throws SettingNotFoundException {
		Long countById=repo.countById(id);
		if(countById==null || countById ==0) {
			throw new SettingNotFoundException("Could not find any thumbnail with ID: "+id);
		}
		repo.deleteById(id);
	}
	public void updateThumbnailEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
		
	}
}

package com.shopme.thumbnails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Settings;
@Service
public class SettingService {
	@Autowired
	public SettingRepository repo;
	public List<Settings> listThumbnail(){
		List<Settings> listEnabledThumbnail =repo.findAllEnabled();
		return listEnabledThumbnail;
		}
}
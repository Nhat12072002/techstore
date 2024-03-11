package com.shopme.admin.settings;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.settings.SettingService;
import com.shopme.admin.users.UserNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.Settings;
import com.shopme.common.entity.User;

@Controller
public class SettingController {
	@Autowired
	private SettingService service;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Settings> listSettings = service.listAll();

		model.addAttribute("listSettings", listSettings);

		return "settings/settings_thumbnail";
	}

	@GetMapping("/settings/new")
	public String newThumbnail(Model model) {
		model.addAttribute("settings", new Settings());
		model.addAttribute("pageTitle", "Create New Thumbnail");

		return "settings/settings_thumbnail_form";
	}

	@PostMapping("/settings/save")
	public String saveThumbnails(Settings settings, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			settings.setLogo(fileName);

			Settings savedThumbnail = service.save(settings);
			String uploadDir = "../settings-logos/" + savedThumbnail.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			service.save(settings);
		}
		redirectAttributes.addFlashAttribute("message", "The thumbnail has been saved successfully!");
		return "redirect:/settings";
	}
	@GetMapping("/settings/edit/{id}")
	public String editThumbnail(@PathVariable(name= "id") Integer id,RedirectAttributes redirectAttributes,Model model) throws SettingNotFoundException {
		try {
		Settings settings= service.get(id);
		model.addAttribute("settings", settings);
		model.addAttribute("pageTitle","Edit Thumbnail (ID:"+id+")");
		return "settings/settings_thumbnail_form";
		} catch (SettingNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
		}
		return "redirect:/settings";
	}
	@GetMapping("/settings/delete/{id}")
	public String deleteThumbnail(@PathVariable(name= "id") Integer id,RedirectAttributes redirectAttributes) throws SettingNotFoundException {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message","The thumbnail ID " + id + " has been deleted sucessfully");
			return "redirect:/settings";
			} catch (SettingNotFoundException ex) {
				redirectAttributes.addFlashAttribute("message",ex.getMessage());
			}
			return "redirect:/settings";
	}
	@GetMapping("/settings/{id}/enabled/{status}")
	public  String updateThumbnailEnableStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes) {
		service.updateThumbnailEnabledStatus(id, enabled);
		String status= enabled ?"enabled":"disabled";
		String message= "The thumbnail ID " +id+ " has been " + status ;
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
}

package com.shopme;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName="user-photos";
		Path userPhotosDir=Paths.get(dirName);
		String userPhotosPath=userPhotosDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + userPhotosPath + "/");
		
		String categoryImagesDirName="../category-images";
		Path categoryImagesDir=Paths.get(categoryImagesDirName);
		String categoryImagesPath=categoryImagesDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/category-images/**").addResourceLocations("file:/" + categoryImagesPath + "/");
		
		String brandLogosDirName="../brand-logos";
		Path brandLogosDir=Paths.get(brandLogosDirName);
		String brandLogosPath=brandLogosDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/" + brandLogosPath + "/");
		
		
		String productImagesDirName="../product-images";
		Path productImagesDir=Paths.get(productImagesDirName);
		String productImagesPath=productImagesDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/product-images/**").addResourceLocations("file:/" + productImagesPath + "/");
		
		String thumbnailImagesDirName="../settings-logos";
		Path thumbnailImagesDir=Paths.get(thumbnailImagesDirName);
		String thumbnailImagesPath=thumbnailImagesDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/settings-logos/**").addResourceLocations("file:/" + thumbnailImagesPath + "/");
		
	}

}

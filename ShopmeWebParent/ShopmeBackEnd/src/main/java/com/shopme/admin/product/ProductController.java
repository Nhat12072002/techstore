package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import com.shopme.common.entity.ProductImage;

@Controller
public class ProductController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1,model,"name","asc",null);
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value="fileImage",required = false) MultipartFile mainImageMultipart,
			@RequestParam(value="extraImage",required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException {
		if(loggedUser.hasRole("Salesperson")) {
			productService.saveProductPrice(product);
			ra.addFlashAttribute("message", "The product has been saved successfully");
			return "redirect:/products";
		}
		setMainImageName(mainImageMultipart, product);
		setExistingExtraImageNames(imageIDs, imageNames, product);
		setNewExtraImageName(extraImageMultiparts, product);
		setProductDetails(detailIDs, detailNames, detailValues, product);
		Product savedProduct = productService.save(product);
		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		deleteExtraImageWereReomovedOnForm(product);
		ra.addFlashAttribute("message", "The product has been saved successfully");
		return "redirect:/products";
	}

	private void deleteExtraImageWereReomovedOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();
				if (!product.containsImageName(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Delete extra image: " + filename);

					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + filename);
					}
				}
			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length == 0)
			return;

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);

	}

	private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0)
			return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);

			if (id != 0) {
				// Trường hợp này là bạn đang cập nhật một chi tiết sản phẩm đã tồn tại
				// Hãy kiểm tra xem chi tiết có tồn tại trong danh sách chi tiết hay không trước
				// khi cập nhật
				boolean detailExists = false;
				for (ProductDetail existingDetail : product.getDetails()) {
					if (existingDetail.getName().equals(name) && existingDetail.getValue().equals(value)) {
						detailExists = true;
						break;
					}
				}
				if (!detailExists) {
					product.addDetail(id, name, value);
				}
			} else if (!name.isEmpty() && !value.isEmpty()) {
				// Trường hợp này là bạn đang thêm một chi tiết mới
				// Hãy kiểm tra xem chi tiết có tồn tại trong danh sách chi tiết hay không trước
				// khi thêm mới
				boolean detailExists = false;
				for (ProductDetail existingDetail : product.getDetails()) {
					if (existingDetail.getName().equals(name) && existingDetail.getValue().equals(value)) {
						detailExists = true;
						break;
					}
				}
				if (!detailExists) {
					product.addDetail(name, value);
				}
			}
		}
	}

	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) {
					System.out.println("Extra image multipart file is empty.");
					continue;
				}
				// Check if extras directory exists
				String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
				if (!Files.exists(Paths.get(uploadDir))) {
					// Create extras directory
					Files.createDirectories(Paths.get(uploadDir));
				}
				// Save extra image
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}

	}

	public void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}

	public void setNewExtraImageName(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (!product.containsImageName(fileName)) {
						product.addExtraImages(fileName);
					}
				}
			}
		}
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnableStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes)
			throws ProductNotFoundException {
		try {
			productService.delete(id);
			String productExtraImageDir = "../product-images/" + id + "/extras";
			String productImageDir = "../product-images/" + id;
			FileUploadUtil.removeDir(productExtraImageDir);
			FileUploadUtil.removeDir(productImageDir);

			redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted sucessfully");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product(ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}

	}

	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			model.addAttribute("product", product);

			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}

	}
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
							@Param("sortfield") String sortField,
							@Param("sortdir") String sortDir,
							@Param("keyword") String keyword) {
		Page<Product> page=productService.listByPage(pageNum,sortField,sortDir,keyword);
		List<Product> listProducts=page.getContent();
		long startCount =(pageNum-1)*ProductService.PRODUCTS_PER_PAGE+1;
		long endCount=startCount+ProductService.PRODUCTS_PER_PAGE-1;
		if(endCount>page.getTotalElements()) {
			endCount=page.getTotalElements();
		}
		String reverseSortDir=sortDir.equals("asc")?"desc":"asc";
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPage",page.getTotalPages());
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount",endCount);
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("listProducts",listProducts);
		model.addAttribute("sortField",sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		model.addAttribute("keyword",keyword);
		return "products/products";
	}
}

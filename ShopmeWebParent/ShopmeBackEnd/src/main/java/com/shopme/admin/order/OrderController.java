package com.shopme.admin.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.customer.CustomerNotFoundException;
import com.shopme.admin.product.ProductNotFoundException;
import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.Product;

@Controller
public class OrderController {

	@Autowired OrderService service;
	@GetMapping("/orders")
	public String listFirstPage(Model model) {
		return listByPage(1,model,"id","asc",null);
	}
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
							@Param("sortfield") String sortField,
							@Param("sortdir") String sortDir,
							@Param("keyword") String keyword) {
		Page<Order> page=service.listByPage(pageNum,sortField,sortDir,keyword);
		List<Order> listOrders=page.getContent();
		long startCount =(pageNum-1)*OrderService.ORDER_PER_PAGE+1;
		long endCount=startCount+OrderService.ORDER_PER_PAGE-1;
		if(endCount>page.getTotalElements()) {
			endCount=page.getTotalElements();
		}
		String reverseSortDir=sortDir.equals("asc")?"desc":"asc";
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPage",page.getTotalPages());
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount",endCount);
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("listOrders",listOrders);
		model.addAttribute("sortField",sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		model.addAttribute("keyword",keyword);
		return "orders/orders";
	}
	@GetMapping("/orders/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Order order = service.get(id);
			model.addAttribute("order", order);

			return "orders/order_detail_modal";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/orders";
		}

	}
	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes)
			throws OrderNotFoundException {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The order ID " + id + " has been deleted sucessfully");
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/orders";
	}
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Order order = service.get(id);		
			model.addAttribute("order", order);
			model.addAttribute("pageTitle", String.format("Edit Order (ID: %d)", id));

			return "orders/order_form";

		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/orders";
		}
		
	}
	@PostMapping("/orders/save")
	public String saveOrder(@ModelAttribute("order") Order order, BindingResult bindingResult, Model model, RedirectAttributes ra) throws OrderNotFoundException {
	    // Kiểm tra lỗi và xử lý nếu cần
	

	    // Chỉ lưu các trường cần thiết
	    Order existingOrder = service.get(order.getId());
	    existingOrder.setPaymentMethod(order.getPaymentMethod());
	    existingOrder.setOrderStatus(order.getOrderStatus());
	    existingOrder.setDeliverDays(order.getDeliverDays());
	    // Lưu lại đối tượng Order
	    service.save(existingOrder);

	    ra.addFlashAttribute("message", "The Order ID " + existingOrder.getId() + " has been updated successfully.");
	    return "redirect:/orders";
	}

}

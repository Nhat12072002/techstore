package com.shopme.checkout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.District;
import com.shopme.common.entity.ShippingRate;

@Service
public class CheckoutService {

	 private final DistrictRepository districtRepository;

	    public CheckoutService(DistrictRepository districtRepository) {
	        this.districtRepository = districtRepository;
	    }

	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
		CheckoutInfo checkoutInfo =new CheckoutInfo();
		
		float productCost =calculateProductCost(cartItems);
		float productTotal=caculateProductTotal(cartItems);
		float shippingCostTotal= calculateShippingCost(cartItems,shippingRate);
		float paymentTotal = productTotal + shippingCostTotal;
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setDeliverDays(shippingRate.getDays());
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		return checkoutInfo;
	}

	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
		float shippingCostTotal = 0.0f;
		for (CartItem item : cartItems) {
			float shippingCost = shippingRate.getRate();
			item.setShippingCost(shippingCost);
			shippingCostTotal += shippingCost;
		}
		return shippingCostTotal;
	}

	private float caculateProductTotal(List<CartItem> cartItems) {
		float total=0.0f;
		for (CartItem item : cartItems) {
			total += item.getSubtotal();
		}
		
		return total;
	}

	private float calculateProductCost(List<CartItem> cartItems) {
		float cost=0.0f;
		for (CartItem item : cartItems) {
			cost += item.getQuantity()*item.getProduct().getCost();
		}
		
		return cost;
	}

	public List<District> getAllDistricts() {
		  return districtRepository.findAllByOrderByNameAsc();
	}

	
}

package com.shopme.admin.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.order.OrderRepository;
import com.shopme.common.entity.Order;

@Service
public class MasterOrderReportService {

	@Autowired private OrderRepository repo;
	private DateFormat dataFormatter;
	public List<ReportItem> getReportDataLast7Days(){
		return getReportDataLastXDay(7);
	}
	private List<ReportItem> getReportDataLastXDay(int days){
		Date endTime = new Date();
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -( days-1));
		Date startTime= cal.getTime();
		System.out.println("start: "+startTime);
		System.out.println("end: "+endTime);

		return getReportDataByDateRange(startTime,endTime);
	}
	private List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime){
		List<Order> listOrders = repo.findByOrderTimeDone(startTime, endTime);
		createReportData(startTime,endTime);
		List<ReportItem> listReportItems=createReportData(startTime, endTime);
		calculateSalesForReportData(listOrders,listReportItems);
		return null;
	}
	private List<ReportItem> createReportData(Date startTime, Date endTime) {
		List<ReportItem> listReportItems = new ArrayList<>();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);
		Date currentDate= startDate.getTime();
		listReportItems.add(new ReportItem());
		do {
			startDate.add(Calendar.DAY_OF_MONTH, 1);
		} while(startDate.before(endDate));
		return listReportItems;
	}
	public void calculateSalesForReportData(List<Order> listOrders,List<ReportItem> listReportItems) {
		for (Order order : listOrders) {
			String orderDateString = dataFormatter.format(order.getOrderTime());
			ReportItem reportItem =new ReportItem(orderDateString);
			int itemIndex = listReportItems.indexOf(reportItem);
			if (itemIndex>=0) {
				reportItem =listReportItems.get(itemIndex);
				reportItem.addGrossSales(order.getTotal());
				reportItem.incraseOrdersCount();
			}
		}
	}
	
}

package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.order.OrderRepository;
import com.shopme.common.entity.Order;

@Service
public class MasterOrderReportService {
	@Autowired
	private OrderRepository repo;
	private DateFormat dateFormatter;

	public List<ReportItem> getReportDataLast7Days() {
		System.out.println("getReportDataLast7Days...");
		return getReportDataLastXDays(7);
	}

	public List<ReportItem> getReportDataLast28Days() {
		System.out.println("getReportDataLast7Days...");
		return getReportDataLastXDays(28);
	}

	private List<ReportItem> getReportDataLastXDays(int days) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();

		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);

		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		return getReportDataByDateRange(startTime, endTime, "days");
	}
	public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime) {
		dateFormatter= new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByDateRange(startTime, endTime, "day");
	}

	private List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, String period) {
		
		List<Order> listOrders = repo.findByOrderTimeDone(startTime, endTime);
		printRawData(listOrders);

		List<ReportItem> listReportItems = createReportData(startTime, endTime, period);

		System.out.println();

		calculateSalesForReportData(listOrders, listReportItems);

		printReportData(listReportItems);

		return listReportItems;
	}

	private void calculateSalesForReportData(List<Order> listOrders, List<ReportItem> listReportItems) {
		for (Order order : listOrders) {
			String orderDateString = dateFormatter.format(order.getOrderTime());

			ReportItem reportItem = new ReportItem(orderDateString);

			int itemIndex = listReportItems.indexOf(reportItem);

			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);

				reportItem.addGrossSales(order.getTotal());
				reportItem.increaseOrdersCount();
			}
		}
	}

	private void printReportData(List<ReportItem> listReportItems) {
		listReportItems.forEach(item -> {
			System.out.printf("%s, %10.2f, %10.2f, %d \n", item.getIdentifier(), item.getGrossSales(),
					item.getNetSales(), item.getOrdersCount());
		});

	}

	private List<ReportItem> createReportData(Date startTime, Date endTime, String period) {
		List<ReportItem> listReportItems = new ArrayList<>();

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startTime);

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(endTime);

		Date currentDate = startDate.getTime();
		String dateString = dateFormatter.format(currentDate);

		listReportItems.add(new ReportItem(dateString));

		do {
			if (period.equals("days")) {
				startDate.add(Calendar.DAY_OF_MONTH, 1);
			} else if (period.equals("months")) {
				startDate.add(Calendar.MONTH, 1);
			}
			currentDate = startDate.getTime();
			dateString = dateFormatter.format(currentDate);

			listReportItems.add(new ReportItem(dateString));

		} while (startDate.before(endDate));

		return listReportItems;
	}

	private void printRawData(List<Order> listOrders) {
		listOrders.forEach(order -> {
			System.out.printf("%-3d | %s | %10.2f | %10.2f \n", order.getId(), order.getOrderTime(), order.getTotal(),
					order.getProductCost());
		});
	}

	public List<ReportItem> getReportDataLast6Months() {

		return getReportDataLastXMonths(6);
	}

	private List<ReportItem> getReportDataLastXMonths(int months) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();

		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);

		dateFormatter = new SimpleDateFormat("yyyy-MM");

		return getReportDataByDateRange(startTime, endTime, "months");
	}

	public List<ReportItem> getReportDataLast12Months() {
		// TODO Auto-generated method stub
		return getReportDataLastXMonths(12);
	}
}
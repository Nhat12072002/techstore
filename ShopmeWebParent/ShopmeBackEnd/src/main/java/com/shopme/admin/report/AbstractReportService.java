package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {
	protected DateFormat dateFormatter;

	public List<ReportItem> getReportDataLast7Days(ReportType reportType) {
		System.out.println("getReportDataLast7Days...");
		return getReportDataLastXDays(7, reportType);
	}

	public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
		System.out.println("getReportDataLast7Days...");
		return getReportDataLastXDays(28, reportType);
	}

	protected List<ReportItem> getReportDataLastXDays(int days,ReportType reportType) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();

		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);

		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	public List<ReportItem> getReportDataLast6Months(ReportType reportType) {

		return getReportDataLastXMonths(6,reportType);
	}

	protected List<ReportItem> getReportDataLastXMonths(int months,ReportType reportType) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();

		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);

		dateFormatter = new SimpleDateFormat("yyyy-MM");

		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, ReportType reportType) {
		dateFormatter= new SimpleDateFormat("yyyy-MM-dd");
		return getReportDataByDateRangeInternal(startTime, endTime,reportType );
	}

	public List<ReportItem> getReportDataLast12Months(ReportType reportType) {
		// TODO Auto-generated method stub
		return getReportDataLastXMonths(12,reportType);
	}
	protected abstract List<ReportItem> getReportDataByDateRangeInternal(Date startDate, Date endDate, ReportType reportType);
}

package com.testing.util;

import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d=new Date();
			String fileName=d.toString().replace(":", "_").replace(" ", "_")+".html";
			String reportPath =Constants.REPORT_PATH+fileName;
 
			ExtentHtmlReporter reporter = new ExtentHtmlReporter(reportPath);

			// initialize ExtentReports and attach the HtmlReporter
			extent = new com.aventstack.extentreports.ExtentReports();
			extent.attachReporter(reporter);			
		}
		return extent;
	}
}

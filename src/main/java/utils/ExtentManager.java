package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance(){
        if (extent == null){
            String reportPath = System.getProperty("user.dir") + "/reports/Report_" + getCurrentTimeStamp() + ".html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("API Report");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }

        return extent;
    }

    static String getCurrentTimeStamp(){
        return new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
    }

}

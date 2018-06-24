package com.maxtree.trackbe4.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.exception.ReportException;
import com.maxtree.tb4beans.PrintableBean;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class TB4Reports {

	private static final Logger log = LoggerFactory.getLogger(TB4Reports.class);

	/**
	 * 
	 * @param beans
	 * @param tranactionUniqueId
	 * @param templateFileName
	 * @param callback
	 * @throws ReportException
	 */
	public void jasperToHtml(List<PrintableBean> beans, int tranactionUniqueId, String templateFileName, Callback callback) throws ReportException {
		try {
			JRDataSource data= new JRBeanCollectionDataSource(beans);  
			// Preparing parameters
			FileInputStream inputStream = new FileInputStream("reports/templates/"+templateFileName);
			JasperReport jasperReport= (JasperReport)JRLoader.loadObject(inputStream);    
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, data);
			File dir = new File("reports/generates/" + tranactionUniqueId);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String destFileName2 = dir.getPath() + "/report.html";
			JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName2);
			inputStream.close();
			log.info("Generated a report.");
			
			callback.onSuccessful();
			
		} catch (JRException e) {
			e.printStackTrace();
			throw new ReportException(e.getMessage());
		} catch (FileNotFoundException e1) {
			throw new ReportException(e1.getMessage());
		} catch (IOException e2) {
			throw new ReportException(e2.getMessage());
		}
	}
	
	/**
	 * 
	 * @param beans
	 * @param tranactionUniqueId
	 * @param templateFileName
	 * @param callback
	 * @throws ReportException
	 */
	public void jasperToPDF(List<PrintableBean> beans, int tranactionUniqueId, String templateFileName, Callback callback) throws ReportException {
		try {
			JRDataSource data= new JRBeanCollectionDataSource(beans);  
			// Preparing parameters
			FileInputStream inputStream = new FileInputStream("reports/templates/"+templateFileName);
			JasperReport jasperReport= (JasperReport)JRLoader.loadObject(inputStream);    
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, data);
			File dir = new File("reports/generates/" + tranactionUniqueId);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String destFileName3 = dir.getPath() + "/report.pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName3);
			inputStream.close();
			log.info("Generated a report.");
			
			callback.onSuccessful();
			
		} catch (JRException e) {
			e.printStackTrace();
			throw new ReportException(e.getMessage());
		} catch (FileNotFoundException e1) {
			throw new ReportException(e1.getMessage());
		} catch (IOException e2) {
			throw new ReportException(e2.getMessage());
		}
	}
	
	/**
	 * 
	 * @param path
	 */
	public void deleteReportFiles(String path) {
		File removableDir = new File(path);//File("reports/generates/" + tranactionUniqueId);
		if (removableDir.exists()) {
			try {
				FileUtils.deleteDirectory(removableDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		/// next
//		
//		Calendar calendar1 = Calendar.getInstance();    
//        int year1 = calendar1.get(Calendar.YEAR);    
//        int month1 = calendar1.get(Calendar.MONTH);    
//        int day1 =calendar1.get(Calendar.DAY_OF_MONTH);//每天   
//        calendar1.set(year1, month1, day1, 00, 05, 00);
//        Date date1 = calendar1.getTime();    
//		
//		
//		
//		Calendar calendar = Calendar.getInstance();    
//        int year = calendar.get(Calendar.YEAR);    
//        int month = calendar.get(Calendar.MONTH);    
//        int day =calendar.get(Calendar.DAY_OF_MONTH);//每天   
//        calendar.set(year, month, day, 00, 06, 00);
//        Date date = calendar.getTime();    
//        System.out.println("====="+date);
//        
//        Timer newTimer = new Timer();
//		TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("sss");
//				  
//			}
//    	};
//    	newTimer.schedule(task, date, 1000);
//    	
		
//		List<ResultsBean> beans = new ArrayList<ResultsBean>();
//		ResultsBean bean = new ResultsBean();
//		bean.setBasicInformation("号牌种类：1-小型汽车\n车牌号：辽BB8K57\nVIN：1222102203DR\n");
//		bean.setChecker("陈顺谊");
//		bean.setDateChecked("2018年5月18日 4点3分");
//		bean.setDateCreated("2018年5月18日 3点33分");
//		bean.setObjection("1.图像不合格\n2.图像不清晰\n");
//		
//		beans.add(bean);
//		
//		try {
//			r.jasperToHtml(beans, 1);
//		} catch (ReportException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}

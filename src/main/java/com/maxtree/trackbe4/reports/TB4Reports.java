package com.maxtree.trackbe4.reports;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.reader.UnicodeReader;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.exception.ReportException;
import com.maxtree.tb4beans.PrintableBean;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * 
 * @author chens
 *
 */
public class TB4Reports {

	private static final Logger log = LoggerFactory.getLogger(TB4Reports.class);

	/**
	 * Export Report To Html File
	 * @param beans
	 * @param userUniqueId
	 * @param templateFileName
	 * @param callback
	 * @throws ReportException
	 */
	public void jasperToHtml(List<PrintableBean> beans, int userUniqueId, String templateFileName, Callback callback) throws ReportException {
		FileInputStream inputStream = null;
		try {
			JRDataSource data= new JRBeanCollectionDataSource(beans);  
			// Preparing parameters
			inputStream = new FileInputStream("reports/templates/"+templateFileName);
			JasperReport jasperReport= (JasperReport)JRLoader.loadObject(inputStream);    
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, data);
			File dir = new File("reports/generates/" + userUniqueId);
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
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Export Report To Pdf File
	 * 
	 * @param beans
	 * @param tranactionUniqueId
	 * @param templateFileName
	 * @param callback
	 * @throws ReportException
	 */
	public void jasperToPDF(List<PrintableBean> beans, int tranactionUniqueId, String templateFileName, Callback callback) throws ReportException {
		FileInputStream inputStream = null;
		try {
			JRDataSource data= new JRBeanCollectionDataSource(beans);  
			// Preparing parameters
			inputStream = new FileInputStream("reports/templates/"+templateFileName);
			JasperReport jasperReport= (JasperReport)JRLoader.loadObject(inputStream);    
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, data);
			File dir = new File("reports/generates/" + tranactionUniqueId);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String destFileName3 = dir.getPath() + "/report.pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName3);
			log.info("Generated a report.");
			callback.onSuccessful();
			
		} catch (JRException e) {
			e.printStackTrace();
			throw new ReportException(e.getMessage());
		} catch (FileNotFoundException e1) {
			throw new ReportException(e1.getMessage());
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Export Page To Image file
	 * 
	 * @param beans
	 * @param userUniqueId
	 * @param templateFileName
	 * @param callback
	 * @throws ReportException
	 */
	public void jasperToPNG(List<PrintableBean> beans, int userUniqueId, String templateFileName, Callback callback) throws ReportException {
		FileInputStream inputStream = null;
		OutputStream ouputStream = null;
		try {
			JRDataSource data= new JRBeanCollectionDataSource(beans);  
			// Preparing parameters
			inputStream = new FileInputStream("reports/templates/"+templateFileName);
			JasperReport jasperReport= (JasperReport)JRLoader.loadObject(inputStream);    
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, data);
			File dir = new File("reports/generates/" + userUniqueId);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String destFileName2 = dir.getPath() + "/report.png";
			File file = new File(destFileName2);
			ouputStream = new FileOutputStream(file);
	        BufferedImage rendered_image = null;      
	        rendered_image = (BufferedImage)JasperPrintManager.printPageToImage(jasperPrint, 0, 1.6f); 
	        ImageIO.write(rendered_image, "png", ouputStream);     
			log.info("Generated a report.");
			callback.onSuccessful();
			
		} catch (JRException e) {
			e.printStackTrace();
			throw new ReportException(e.getMessage());
		} catch (FileNotFoundException e1) {
			throw new ReportException(e1.getMessage());
		} catch (IOException e2) {
			throw new ReportException(e2.getMessage());
		} finally {
			if(ouputStream != null) {
				try {
					ouputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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

	public static void main(String[] args) throws IOException {
	 
		
		
//			BufferedReader br = new BufferedReader(new FileReader("devices/Sample_CamOCX_HTML_Device_IE.html"));
//			String everything = "";
//			try {
//			    StringBuilder sb = new StringBuilder();
//			    String line = br.readLine();
//			    while (line != null) {
//			        sb.append(line);
//			        sb.append(System.lineSeparator());
//			        line = br.readLine();
//			    }
//			    everything = sb.toString();
//			    
//			    System.out.println(everything);
//			    
//			} finally {
//			    br.close();
//			}
		String everything = "";
		 File f  = new File("devices/Sample_CamOCX_HTML_Device_IE.html");  
	     FileInputStream in = new FileInputStream(f);  
	        // 指定读取文件时以UTF-8的格式读取  
//	      BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
	        BufferedReader br = new BufferedReader(new UnicodeReader(in));  
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();  
	        while(line != null)  
	        {  
	            System.out.println(line); 
	            sb.append(line);
		        sb.append(System.lineSeparator());
	            line = br.readLine();  
	        }  
	        everything = sb.toString();
	        System.out.println(everything);
 
	        br.close();
	        in.close();
	        
 
		FileOutputStream outSTr = new FileOutputStream(new File("devices/new.html"));
		BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
		Buff.write(everything.getBytes());
        Buff.flush();
        Buff.close();
		
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

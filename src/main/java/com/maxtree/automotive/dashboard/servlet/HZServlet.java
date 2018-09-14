package com.maxtree.automotive.dashboard.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxtree.automotive.dashboard.service.DocumentService;
import com.maxtree.automotive.dashboard.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/hz")
public class HZServlet extends HttpServlet {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	TransactionService transactionService;
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setCharacterEncoding("GBK");
		request.setCharacterEncoding("GBK");
		
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");

		String json = request.getParameter("mydata");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		// convert JSON string to Map
		Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
		String licensePlateNumber = map.get("号牌号码");
		String barcode = map.get("业务流水号码");
		String plateType = map.get("号牌种类");
		String vin = map.get("车辆识别代号");
		String uuid = map.get("业务档号");
		// check null
		if(StringUtils.isEmpty(licensePlateNumber)) {
			response.getWriter().write("{\"error\":\"1\",\"msg\":\"号牌号码不能为空。\"}");
		}
		else if(StringUtils.isEmpty(barcode)) {
			response.getWriter().write("{\"error\":\"1\",\"msg\":\"业务流水号码不能为空。\"}");
		}
		else if(StringUtils.isEmpty(plateType)) {
			response.getWriter().write("{\"error\":\"1\",\"msg\":\"号牌种类不能为空。\"}");
		}
		else if(StringUtils.isEmpty(vin)) {
			response.getWriter().write("{\"error\":\"1\",\"msg\":\"车辆识别代号不能为空。\"}");
		}
		else {
			if(licensePlateNumber.length() != 7 && licensePlateNumber.length() != 8) {
				response.getWriter().write("{\"error\":\"1\",\"msg\":\"号牌号码长度不正确。\"}");
			}
			else if(barcode.length() != 13) {
				response.getWriter().write("{\"error\":\"1\",\"msg\":\"业务流水号码长度不正确。\"}");
			}
			else if(plateType.length() != 4) {
				response.getWriter().write("{\"error\":\"1\",\"msg\":\"号牌种类长度不正确。\"}");
			}
			else if(vin.length() != 17) {
				response.getWriter().write("{\"error\":\"1\",\"msg\":\"车辆识别代号长度不正确。\"}");
			}
		}
		
		
//		transactionService.findByUUID(uuid, vin)
//		transactionService.update(transaction);
		
		
		
		
//		JSONObject jObj;
//		try {
//			jObj = new JSONObject(request.getParameter("mydata"));
//			Iterator<String> it = jObj.keys(); //gets all the keys
//			while(it.hasNext())
//			{
//			    String key = it.next(); // get key
//			    Object o = jObj.get(key); // get value
////			    session.putValue(key, o); // store in session
//			    System.out.println("key-o:"+key+","+o);
//			}
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//			response.getWriter().write("{\"result\":\"1\"}");
//		} // this parses the json
		
		
	}
}

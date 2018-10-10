package com.maxtree.automotive.dashboard;

import java.sql.Connection;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.service.TransactionService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionTest {
	
	 @Autowired
	 private TransactionService service;
	
	 @Test
	 public void getConnection() {
		 // case1
		 Connection conn = service.getConnection();
		 assertNotNull(conn);
	 }

	 @Test
	 public void findById() {
		 // case1
		 int transactionUniqueId = 1;
		 String vin = "1";
		 Transaction trans = service.findById(transactionUniqueId, vin);
		 assertNull(trans);
		 
		 // case2
		 transactionUniqueId = 1;
		 vin = "1";
		 trans = service.findById(transactionUniqueId, vin);
		 assertNull(trans);
	 }
	
}

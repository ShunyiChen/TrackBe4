package com.maxtree.automotive.dashboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.maxtree.automotive.dashboard.service.TransactionService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TB4Test {
	
	@Autowired
    private TransactionService employeeService;
	
	@Test
	public void contextLoads() {
		 System.out.println(employeeService);
	}

}

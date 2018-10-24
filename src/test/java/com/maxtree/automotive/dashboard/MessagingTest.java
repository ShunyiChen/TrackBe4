package com.maxtree.automotive.dashboard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.service.MessagingService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagingTest {

	 @Autowired
	 private MessagingService service;
	
	 @Test
	 public void findById() {
		 List<Notification> notifications = service.findAllNotifications(2,"",true);
		 
	 }
}

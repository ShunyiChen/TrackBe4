package com.maxtree.automotive.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;

import com.maxtree.automotive.dashboard.service.LoggingService;

@ServletComponentScan
@SpringBootApplication
public class TB4Application extends SpringBootServletInitializer implements CommandLineRunner {

	// define the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(TB4Application.class);

    private static TB4Application app = null;
    
    public static TB4Application getInstance() {
    	return app;
    }
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    public LoggingService loggingService;
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TB4Application.class);
    }
    
    public static void main(String args[]) {
        LOGGER.info("Logger enabled: Entering main {}\n\n","你好");
    	
        SpringApplication.run(TB4Application.class, args);

        LOGGER.info("Exiting main");
    }

    @Override
    public void run(String... strings) throws Exception {
    	app = this;
    }

    public static final String PERMISSION_DENIED_MESSAGE = "没有权限。";
    public static final String NAME = "TrackBe4";
	public static final String VERSION = "1.1.0";
	public static final String BUILD_ID = "20190823";
}

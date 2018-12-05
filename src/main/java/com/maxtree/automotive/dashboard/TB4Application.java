package com.maxtree.automotive.dashboard;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;

import com.maxtree.automotive.dashboard.service.LoggingService;

@ServletComponentScan
@SpringBootApplication
public class TB4Application extends SpringBootServletInitializer implements CommandLineRunner {

	// define the logger
    private static Logger log = Logger.getLogger(TB4Application.class);

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
    	
    	log.info("Logger enabled: Entering main \n\n");
    	
        SpringApplication.run(TB4Application.class, args);
        
        log.info("Exiting main");
    }

    @Override
    public void run(String... strings) throws Exception {
    	app = this;
//    	SystemConfiguration sc = Yaml.readSystemConfiguration();
//    	if(sc.getCreateDBTableOnStartup().equalsIgnoreCase("yes")) {
//    		App dbbuilder = new App();
//        	dbbuilder.build(jdbcTemplate.getDataSource().getConnection());
//    	}
    }
    
    public static final String PERMISSION_DENIED_MESSAGE = "没有权限。";
    public static final String NAME = "TrackBe4";
	public static final String VERSION = "1.0 beta";
	public static final String BUILD_ID = "20181206 4:04AM";
}

package com.maxtree.automotive.dashboard;

import com.maxtree.automotive.dashboard.jpa.entity.Camera;
import com.maxtree.automotive.dashboard.jpa.entity.Logging;
import com.maxtree.automotive.dashboard.jpa.repository.CameraRepository;
import com.maxtree.automotive.dashboard.jpa.repository.LoggingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.regex.Pattern;


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
    public CameraRepository cameraRepository;

    @Autowired
    public LoggingRepository loggingRepository;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TB4Application.class);
    }
    
    public static void main(String args[]) {
        LOGGER.info("\n\n{}\n\n","Welcome!欢迎！ようこそ！");
    	
        SpringApplication.run(TB4Application.class, args);

//        String pattern = ".*辽[Bb].*";
//        boolean matches = Pattern.matches(pattern, "辽B B8K57");
//        System.out.println(matches);

        System.out.println("Application is started!");
        LOGGER.info("Application is started!");
    }

    @Override
    public void run(String... strings) throws Exception {
    	app = this;
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            Iterable<Camera> iter = cameraRepository.findAll();
            iter.forEach(e -> {
                System.out.println("e="+e.getName());
            });
        };
    }

    public static final String PERMISSION_DENIED_MESSAGE = "没有权限。";
    public static final String NAME = "TrackBe4";
	public static final String VERSION = "2.0.0 Alpha";
	public static final String BUILD_ID = "20190823";
}

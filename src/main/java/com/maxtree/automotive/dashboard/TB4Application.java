package com.maxtree.automotive.dashboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class TB4Application extends SpringBootServletInitializer implements CommandLineRunner {

	// define the logger
    private static Logger log = Logger.getLogger(TB4Application.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
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
//    	log.info("==============   QUERY FOR MULTIPLE ROWS   ================");
//    	String sql = "SELECT * FROM USR WHERE USERNAME = ?";
//    	List<Usr> lstUser = jdbcTemplate.query(sql, new Object[] {"ROOT"}, new BeanPropertyRowMapper<Usr>(Usr.class));
//    	
//    	sql = "SELECT * FROM ROLEMEMBER";// WHERE ROLEMEMBERUNIQUEID=? AND USERUNIQUEID = ? AND ROLEUNIQUEID = ?";
//		List<RoleMember> lstRoleMember = jdbcTemplate.query(sql,   new BeanPropertyRowMapper<RoleMember>(RoleMember.class));

//		sql = "SELECT * FROM ROLERIGHT";// WHERE ROLEMEMBERUNIQUEID=? AND USERUNIQUEID = ? AND ROLEUNIQUEID = ?";
//		List<RoleRight> lstRoleRight = jdbcTemplate.query(sql,  new BeanPropertyRowMapper<RoleRight>(RoleRight.class));

		
		
//    	sql = "SELECT * FROM ROLE";
//    	List<Role> lstRole = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class));
//    	sql = "SELECT * FROM PERMISSION";
//    	List<Permission> lstPermission = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Permission.class));
//    	
//    	for (Usr user : lstUser) {
//    		log.info(user.toString());
//    	}
//    	for (Role role : lstRole) {
//    		log.info(role.toString());
//    	}
//    	for (Permission permission : lstPermission) {
//    		log.info(permission.toString());
//    	}
//		for (RoleMember rm : lstRoleMember) {
//			log.info(rm.toString());
//		}
		
//		for (RoleRight rr : lstRoleRight) {
//			log.info(rr.getRoleRightUniqueId()+"");
//		}
    }
    
    
    
    public static final String PERMISSION_DENIED_MESSAGE = "没有权限。";
    public static final String NAME = "TrackBe4";
	public static final String VERSION = "1.0 for beta";
	public static final String BUILD_ID = "20180419 8:39AM";
}

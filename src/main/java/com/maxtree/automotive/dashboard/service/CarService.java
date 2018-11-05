package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Car;

@Component
public class CarService {
	
	private static final Logger log = LoggerFactory.getLogger(CarService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 */
	public List<Car> findAll() {
		String sql = "SELECT * FROM CARS ORDER BY CARUNIQUEID";
		List<Car> results = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper<Car>(Car.class));
		return results;
	}
	
	/**
	 * 
	 * @param carUniqueId
	 * @return
	 */
	public Car findById(int carUniqueId) {
		String sql = "SELECT * FROM CARS WHERE CARUNIQUEID=?";
		List<Car> results = jdbcTemplate.query(sql, new Object[] {carUniqueId}, new BeanPropertyRowMapper<Car>(Car.class));
		if(results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param barcode
	 * @return
	 */
	public Car findByBarcode(String barcode) {
		String sql = "SELECT * FROM CARS WHERE BARCODE=?";
		List<Car> results = jdbcTemplate.query(sql, new Object[] {barcode}, new BeanPropertyRowMapper<Car>(Car.class));
		if(results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param embeddedServer
	 * @return
	 */
	public int insert(Car car) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_CAR_SQL = "INSERT INTO CARS(BARCODE,PLATETYPE,PLATENUMBER,VIN) VALUES(?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_CAR_SQL, new String[] { "caruniqueid" });
				ps.setString(1, car.getBarcode());
				ps.setString(2, car.getPlateType());
				ps.setString(3, car.getPlateNumber());
				ps.setString(4, car.getVin());
				return ps;
			}
		}, keyHolder);
		int caruniqueid = keyHolder.getKey().intValue();
		return caruniqueid;
	}
	
	/**
	 * 
	 * @param car
	 */
	public void update(Car car) {
		String sql = "UPDATE CARS SET BARCODE=?,PLATETYPE=?,PLATENUMBER=?,VIN=? WHERE CARUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {car.getBarcode(),car.getPlateType(),car.getPlateNumber(),car.getVin(),car.getCarUniqueId()});
	}
	
	/**
	 * 
	 * @param carUniqueId
	 */
	public void delete(int carUniqueId) {
		String sql = "DELETE FROM CARS WHERE CARUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {carUniqueId});
	}
	
	/**
	 * 
	 * @param vin
	 */
	public void delete(String vin) {
		String sql = "DELETE FROM CARS WHERE VIN=?";
		jdbcTemplate.update(sql, new Object[] {vin});
	}
}

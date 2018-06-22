package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.SiteCapacity;

@Component
public class SiteService {

	private static final Logger log = LoggerFactory.getLogger(SiteService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 */
	public List<Site> findAll() {
		String sql = "SELECT * FROM SITE";
		List<Site> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Site>(Site.class));
		for (Site site : results) {
		
			SiteCapacity siteCapacity = getSiteCapacity(site.getSiteUniqueId());
			site.setSiteCapacity(siteCapacity);
		}
		return results;
	}
	
	/**
	 * 
	 * @param siteUniqueId
	 * @return
	 */
	public Site findById(int siteUniqueId) {
		String sql = "SELECT * FROM SITE WHERE SITEUNIQUEID = ?";
		List<Site> results = jdbcTemplate.query(sql, new Object[] {siteUniqueId}, new BeanPropertyRowMapper<Site>(Site.class));
		if (results.size() > 0) {
			
			Site site = results.get(0);
			SiteCapacity siteCapacity = getSiteCapacity(site.getSiteUniqueId());
			site.setSiteCapacity(siteCapacity);
			return site;
		}
		return new Site();
	}
	
	/**
	 * 
	 * @param siteUniqueId
	 * @return
	 */
	public SiteCapacity getSiteCapacity(int siteUniqueId) {
		String sql = "SELECT * FROM SITECAPACITY WHERE SITEUNIQUEID=?";
		List<SiteCapacity> lstProfiles = jdbcTemplate.query(sql, new Object[] {siteUniqueId}, new BeanPropertyRowMapper<SiteCapacity>(SiteCapacity.class));
		if (lstProfiles.size() > 0) {
			return lstProfiles.get(0);
		}
		return new SiteCapacity();
	}
	
	/**
	 * 
	 * @param siteUniqueId
	 * @param oldTimes
	 * @param newTimes
	 * @param newUsedSize
	 * @return
	 */
	public int updateCapacity(int siteUniqueId, long oldTimes, long newTimes, long newUsedSize) {
		String sql = "UPDATE SITECAPACITY SET USEDSPACE=?,UPDATETIMEMILLIS=? WHERE UPDATETIMEMILLIS=? AND SITEUNIQUEID=?";
		int opt = jdbcTemplate.update(sql, new Object[] {
				newUsedSize,
				newTimes,
				oldTimes,
				siteUniqueId});
		return opt;
	}
	
	/**
	 * 
	 * @param siteUniqueId
	 * @return
	 */
	public List<Community> assignedCommunities(Integer siteUniqueId) {
		String sql = "SELECT B.* FROM COMMUNITYSITES AS A LEFT JOIN COMMUNITIES AS B ON A.COMMUNITYUNIQUEID = B.COMMUNITYUNIQUEID WHERE A.SITEUNIQUEID=? AND B.COMMUNITYNAME IS NOT NULL";
		List<Community> results = jdbcTemplate.query(sql, new Object[] {siteUniqueId}, new BeanPropertyRowMapper<Community>(Community.class));
		return results;
	}
	
	/**
	 * 
	 * @param site
	 * @return
	 */
	public Site save(Site site) {
		String INSERT_TRANS_SQL = "INSERT INTO SITE(SITENAME,SITETYPE,HOSTADDR,PORT,DEFAULTREMOTEDIRECTORY,USERNAME,PASSWORD,MODE,CHARSET,RUNNINGSTATUS) VALUES(?,?,?,?,?,?,?,?,?,?)";
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "siteuniqueid" });
				ps.setString(1, site.getSiteName());
				ps.setString(2, site.getSiteType());
				ps.setString(3, site.getHostAddr());
				ps.setInt(4, site.getPort());
				ps.setString(5, site.getDefaultRemoteDirectory());
				ps.setString(6, site.getUserName());
				ps.setString(7, site.getPassword());
				ps.setString(8, site.getMode());
				ps.setString(9, site.getCharset());
				ps.setInt(10, site.getRunningStatus());
				return ps;
			}
		}, keyHolder);
		int siteUniqueId = keyHolder.getKey().intValue();
		
		String sql = "INSERT INTO SITECAPACITY(SITEUNIQUEID,CAPACITY,USEDSPACE,UPDATETIMEMILLIS,UNIT,UNITNUMBER) VALUES(?,?,?,?,?,?)";
		int opt = jdbcTemplate.update(sql, new Object[] {
				siteUniqueId,
				site.getSiteCapacity().getCapacity(),
				site.getSiteCapacity().getUsedSpace(),
				site.getSiteCapacity().getUpdateTimeMillis(),
				site.getSiteCapacity().getUnit(),
				site.getSiteCapacity().getUnitNumber()});
	 	return site;
	}
	
	/**
	 * 
	 * @param site
	 * @return
	 */
	public Site update(Site site) {
		String sql = "UPDATE SITE SET SITENAME=?,SITETYPE=?,HOSTADDR=?,PORT=?,DEFAULTREMOTEDIRECTORY=?,USERNAME=?,PASSWORD=?,MODE=?,CHARSET=?,RUNNINGSTATUS=? WHERE SITEUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {site.getSiteName(), site.getSiteType(), site.getHostAddr(), site.getPort(), site.getDefaultRemoteDirectory(),site.getUserName(),site.getPassword(),site.getMode(),site.getCharset(),site.getRunningStatus(), site.getSiteUniqueId()});
	 
	 	sql = "UPDATE SITECAPACITY SET CAPACITY=?,USEDSPACE=?,UPDATETIMEMILLIS=?,UNIT=?,UNITNUMBER=? WHERE SITECAPACITYUNIQUEID=?";
	 	opt = jdbcTemplate.update(sql, new Object[] {
	 			site.getSiteCapacity().getCapacity(),
	 			site.getSiteCapacity().getUsedSpace(),
	 			site.getSiteCapacity().getUpdateTimeMillis(),
	 			site.getSiteCapacity().getUnit(),
	 			site.getSiteCapacity().getUnitNumber(),
	 			site.getSiteCapacity().getSiteCapacityUniqueId()});
	 	log.info("Update result:"+opt);
	 	return site;
	}
	
	/**
	 * 
	 * @param communityUniqueId
	 * @param sites
	 */
	public void updateCommunitySites(int communityUniqueId, List<Site> sites) {
		String sql = "DELETE FROM COMMUNITYSITES WHERE COMMUNITYUNIQUEID = ?";
		jdbcTemplate.update(sql, communityUniqueId);
		final String inserQuery = "INSERT INTO COMMUNITYSITES(COMMUNITYUNIQUEID, SITEUNIQUEID) VALUES(?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Site site = sites.get(i);
				ps.setInt(1, communityUniqueId);
				ps.setInt(2, site.getSiteUniqueId());
			}

			@Override
			public int getBatchSize() {
				return sites.size();
			}
		});
		log.info("UserSites batch update has done.");
	}
 
	/**
	 * 
	 * @param siteUniqueId
	 * @param users
	 */
	public void updateSiteCommunities(int siteUniqueId, List<Community> communities) {
		String sql = "DELETE FROM COMMUNITYSITES WHERE SITEUNIQUEID = ?";
		jdbcTemplate.update(sql, siteUniqueId);
		final String inserQuery = "INSERT INTO COMMUNITYSITES(COMMUNITYUNIQUEID,SITEUNIQUEID) VALUES(?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Community community = communities.get(i);
				ps.setInt(1, community.getCommunityUniqueId());
				ps.setInt(2, siteUniqueId);
			}

			@Override
			public int getBatchSize() {
				return communities.size();
			}
		});
		log.info("updateSiteUsers batch update has done.");
	}
	
	
	/**
	 * 
	 * @param communityUniqueId
	 * @return
	 */
	public List<Site> getSites(int communityUniqueId) {
		String sql = "SELECT B.* FROM COMMUNITYSITES AS A LEFT JOIN SITE AS B ON A.SITEUNIQUEID=B.SITEUNIQUEID WHERE A.COMMUNITYUNIQUEID=?";
		List<Site> results = jdbcTemplate.query(sql, new Object[] {communityUniqueId}, new BeanPropertyRowMapper<Site>(Site.class));
		for (Site site : results) {
			SiteCapacity siteCapacity = getSiteCapacity(site.getSiteUniqueId());
			site.setSiteCapacity(siteCapacity);
		}
		return results;
	}
	
	/**
	 * 
	 * @param siteUniqueId
	 */
	public void delete(int siteUniqueId) {
		String sql = "DELETE FROM SITECAPACITY WHERE SITEUNIQUEID = ?";
		jdbcTemplate.update(sql, siteUniqueId);
		
		sql = "DELETE FROM COMMUNITYSITES WHERE SITEUNIQUEID = ?";
		jdbcTemplate.update(sql, siteUniqueId);
		
		sql = "DELETE FROM SITE WHERE SITEUNIQUEID = ?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {siteUniqueId});
	 	log.info("Delete result:"+opt);
	}
}

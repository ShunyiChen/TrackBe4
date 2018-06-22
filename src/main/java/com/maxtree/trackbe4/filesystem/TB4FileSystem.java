package com.maxtree.trackbe4.filesystem;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
@Component
public class TB4FileSystem {

	
	private static final Logger log = LoggerFactory.getLogger(TB4FileSystem.class);
	
	/**
	 * 
	 * @param site
	 * @return
	 */
	public boolean testConnection(Site site) {
		try {
			FileSystemManager fsManager;
			fsManager = VFS.getManager();
			String protocol = null;
			String name = null;
			if (site.getSiteType().equals("FTP") || site.getSiteType().equals("FTPS")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + "://" + site.getUserName() + ":" + site.getPassword() + "@" + site.getHostAddr() + ":"
						+ site.getPort() + site.getDefaultRemoteDirectory();
			} else if (site.getSiteType().equals("File")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + ":///" + site.getHostAddr();
			}
			FileObject rootFile = fsManager.resolveFile(name);
			fsManager.closeFileSystem(rootFile.getFileSystem());
			return rootFile.exists();
		
		} catch (FileSystemException e) {
			log.info(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 上传
	 * 
	 * @param site
	 * @param targetPath
	 * @return
	 * @throws FileException
	 */
	public OutputStream receiveUpload(Site site, String targetPath) throws FileException {
		FileSystemManager fsManager = null;
		FileObject file = null;
		try {
			fsManager = VFS.getManager();
			String protocol = null;
			String name = null;
			if (site.getSiteType().equals("FTP") || site.getSiteType().equals("FTPS")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + "://" + site.getUserName() + ":" + site.getPassword() + "@" + site.getHostAddr() + ":"
						+ site.getPort() + site.getDefaultRemoteDirectory();
			} else if (site.getSiteType().equals("File")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + ":///" + site.getHostAddr();
			}
			targetPath = new String(targetPath.getBytes("UTF-8"),"ISO-8859-1");
			file = fsManager.resolveFile(name + "/"+ targetPath);
			
			if (!file.exists()) {
				file.createFile();
			}
			return file.getContent().getOutputStream(); // Return the output stream to write to
		} catch (FileSystemException e) {
			log.info(e.getMessage());
			throw new FileException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.info(e.getMessage());
			throw new FileException(e.getMessage());
		} finally {
			try {
				fsManager.closeFileSystem(file.getFileSystem());
			} catch (java.lang.NullPointerException e) {
				String msg = "Cannot connect to the site, please contact the administrator in TB4 team.";
				log.info(msg);
				throw new FileException(msg);
			}
		}
	}
 
	/**
	 * 下载
	 * @param site
	 * @param targetPath
	 * @return
	 * @throws FileException
	 */
	public FileObject resolveFile(Site site, String targetPath) throws FileException {
		FileSystemManager fsManager = null;
		FileObject file = null;
		try {
			fsManager = VFS.getManager();
			String protocol = null;
			String name = null;
			if (site.getSiteType().equals("FTP") || site.getSiteType().equals("FTPS")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + "://" + site.getUserName() + ":" + site.getPassword() + "@" + site.getHostAddr() + ":"
						+ site.getPort() + site.getDefaultRemoteDirectory();
			} else if (site.getSiteType().equals("File")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + ":///" + site.getHostAddr();
			}
			targetPath = new String(targetPath.getBytes("UTF-8"),"ISO-8859-1");
			file = fsManager.resolveFile(name + "/"+ targetPath);
			
			return file; // Return the output stream to write to
		} catch (FileSystemException e) {
			log.info(e.getMessage());
			throw new FileException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.info(e.getMessage());
			throw new FileException(e.getMessage());
		} finally {
			try {
				fsManager.closeFileSystem(file.getFileSystem());
			} catch (java.lang.NullPointerException e) {
				String msg = "Cannot connect to the site, please contact the administrator in TB4 team.";
				log.info(msg);
				throw new FileException(msg);
			}
		}
	}
	
	/**
	 * 删除文件
	 * 
	 * @param site
	 * @param targetPath
	 * @throws FileException
	 */
	public void deleteFile(Site site, String targetPath) throws FileException {
		FileSystemManager fsManager = null;
		FileObject file = null;
		try {
			fsManager = VFS.getManager();
			String protocol = null;
			String name = null;
			if (site.getSiteType().equals("FTP") || site.getSiteType().equals("FTPS")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + "://" + site.getUserName() + ":" + site.getPassword() + "@" + site.getHostAddr() + ":"
						+ site.getPort() + site.getDefaultRemoteDirectory();
			} else if (site.getSiteType().equals("File")) {
				protocol = site.getSiteType().toLowerCase();
				name = protocol + ":///" + site.getHostAddr();
			}
			targetPath = new String(targetPath.getBytes("UTF-8"),"ISO-8859-1");
			file = fsManager.resolveFile(name + "/"+ targetPath);
			
			file.delete();
			
		} catch (FileSystemException e) {
			log.info(e.getMessage());
			throw new FileException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.info(e.getMessage());
			throw new FileException(e.getMessage());
		} finally {
			try {
				fsManager.closeFileSystem(file.getFileSystem());
			} catch (java.lang.NullPointerException e) {
				String msg = "Cannot connect to the site, please contact the administrator in TB4 team.";
				log.info(msg);
				throw new FileException(msg);
			}
		}
	}
	
	/**
	 * 更新站点已用大小
	 * 
	 * @param siteUniqueId
	 * @param increment     增量字节数
	 */
	public void checkAndUpdateDisk(int siteUniqueId, long increment) {
		System.out.println("Checking Disk");
		Site s = ui.siteService.findById(siteUniqueId);
		long newUsedSize = s.getSiteCapacity().getUsedSpace() + increment;
		int affectedRow = ui.siteService.updateCapacity(siteUniqueId, s.getSiteCapacity().getUpdateTimeMillis(), System.currentTimeMillis(), newUsedSize);
		if (affectedRow == 0) {
			checkAndUpdateDisk(siteUniqueId, increment);
		}
	}
	
	
	public static void main(String[] args) throws FileSystemException, UnsupportedEncodingException {
//		// Locate the Jar file
//		FileSystemManager fsManager = VFS.getManager();
////		FileSystemOptions options = new FileSystemOptions();
////		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, true);
//		
////		String name = "ftp://user1:123456@127.0.0.1:21/";
////		String name = "ftps://ftps_user:123456@127.0.0.1:21/";	
//		String name = "ftp"+"://"+"chens"+":"+"Wonderful2018"+"@"+"10.48.5.75"+":"+"21"+"/";
////		String name = "file"+":///"+"C:\\Users\\chens\\Documents\\development\\maxtree\\TrackBe4\\tmp\\uploads\\1";
////		String name = "file"+":///"+"E:\\Maxtree\\trackbe4\\tmp\\uploads";
//		
//		FileObject rootFile = fsManager.resolveFile(name );
//
////		System.out.println(rootFile.exists());
//		
//		// List the children of the Jar file
//		FileObject[] children = rootFile.getChildren();
//		System.out.println( "Children of " + rootFile.getName().getURI() );
//		for ( int i = 0; i < children.length; i++ ) {
//			String baseName = children[i].getName().getBaseName();  
////			System.out.println(children[i].getContent().);
//			String ext = Files.getFileExtension(baseName);
////			System.out.println(baseName+"  "+ext+"  "); //prints txt
//			for (String d: children[i].getContent().getAttributeNames()) {
//				System.out.println(d);
//			}
////            System.out.println(" " + baseName + "  --  " + new String(baseName.getBytes("iso-8859-1"),"UTF-8"));  
//		}
		
		TB4FileSystem sys = new TB4FileSystem();
		Site site = new Site();
		site.setSiteType("FTP");
		site.setUserName("chens");
		site.setPassword("Wonderful2018");
		site.setHostAddr("10.48.5.75");
		site.setPort(21);
		site.setDefaultRemoteDirectory("/");
		String targetPath = "1/primary/MaxTree截图20171220113538.png";
		targetPath = new String(targetPath.getBytes("UTF-8"),"ISO-8859-1");
		try {
			sys.receiveUpload(site, targetPath);
		} catch (FileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}

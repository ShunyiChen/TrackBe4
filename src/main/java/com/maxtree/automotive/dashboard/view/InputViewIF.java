package com.maxtree.automotive.dashboard.view;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.view.front.BasicInfoPane;
import com.maxtree.automotive.dashboard.view.front.BusinessTypePane;
import com.maxtree.automotive.dashboard.view.front.CapturePane;
import com.maxtree.automotive.dashboard.view.front.ThumbnailGrid;

/**
 * 
 * @author Chen
 *
 */
public interface InputViewIF extends FrontendViewIF {
	
	public User loggedInUser();

	public int batch();

	public String uuid();

	public String vin();

	public Site editableSite();

	public BasicInfoPane basicInfoPane();
	
	public BusinessTypePane businessTypePane();
	
	public ThumbnailGrid thumbnailGrid();
	
	public CapturePane capturePane();
	
	public void openTransaction(Transaction transaction,int deletableMessageUniqueId, Callback callback);
}

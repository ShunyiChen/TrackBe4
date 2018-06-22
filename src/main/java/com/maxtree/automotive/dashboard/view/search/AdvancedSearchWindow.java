package com.maxtree.automotive.dashboard.view.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AdvancedSearchWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public AdvancedSearchWindow() {
		this.setWidth("682px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(true);
		this.setCaption("高级查询");
		
//		User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		mainLayout = new VerticalLayout(); 
//		mainLayout.setSpacing(false);
//		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
		
		// Find results
		form = new FormLayout();
		form.setWidth("90%");
		form.setHeightUndefined();
		allOfTheseWords.setWidth("100%");
		theExactPhrase.setWidth("100%");
		anyOfTheseWords.setWidth("100%");
		noneOfTheseWords.setWidth("100%");
		form.addComponents(allOfTheseWords, theExactPhrase, anyOfTheseWords, noneOfTheseWords);
		
		// 每页显示行数
		displayRowCount = new HorizontalLayout();
		displayRowCount.setSpacing(false);
		displayRowCount.setMargin(false);
		displayRowCount.setWidthUndefined();
		List<Integer> data = new ArrayList<>();
		data.add(20);
		data.add(50);
		data.add(100);
		resultsPerPage = new ComboBox<>(null, data);
		resultsPerPage.setEmptySelectionAllowed(false);
		resultsPerPage.setTextInputAllowed(false);
		resultsPerPage.setSelectedItem(20);
		resultsPerPage.setWidth("80px");
		
		Label txt = new Label("每页显示数据行数:");
		displayRowCount.addComponents(txt, Box.createHorizontalBox(3), resultsPerPage);
		displayRowCount.setComponentAlignment(txt, Alignment.MIDDLE_LEFT);
		displayRowCount.setComponentAlignment(resultsPerPage, Alignment.MIDDLE_LEFT);
		
		// 默认第一行
		firstRow = new CriterionHLayout(mainLayout, null, true);
     
		// 按钮
		buttonPane = new HorizontalLayout();
		buttonPane.setWidth("90%");
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		Button btnClearAll = new Button("全部清空");
		btnSearch = new Button("查询");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("270px");
		subButtonPane.setHeightUndefined();
		subButtonPane.addComponents(btnCancel, btnClearAll, btnSearch);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnClearAll, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnSearch, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(form, displayRowCount, Box.createVerticalBox(5), firstRow, Box.createVerticalBox(5), buttonPane, Box.createVerticalBox(5));
		mainLayout.setComponentAlignment(form, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(displayRowCount, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(firstRow, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		btnClearAll.addClickListener(e -> {
			
			allOfTheseWords.setValue("");
			theExactPhrase.setValue("");
			anyOfTheseWords.setValue("");
			noneOfTheseWords.setValue("");
			resultsPerPage.setValue(20);
			firstRow = new CriterionHLayout(mainLayout, null, true);
			
			mainLayout.removeAllComponents();
			
			mainLayout.addComponents(form, displayRowCount, Box.createVerticalBox(5), firstRow, Box.createVerticalBox(5), buttonPane, Box.createVerticalBox(5));
			mainLayout.setComponentAlignment(form, Alignment.TOP_LEFT);
			mainLayout.setComponentAlignment(displayRowCount, Alignment.TOP_LEFT);
			mainLayout.setComponentAlignment(firstRow, Alignment.TOP_LEFT);
			mainLayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
		});
		
	}
	
	private List<Transaction> doSearchWithMultiThreads(SearchModel searchModel) {
		// 多线程查询
		searchModel.setWithAllOfTheWords(allOfTheseWords.getValue());
		searchModel.setWithTheExactPhrase(theExactPhrase.getValue());
		searchModel.setWithAtLeastOneOfTheWords(anyOfTheseWords.getValue());
		searchModel.setWithoutTheWords(noneOfTheseWords.getValue());
		searchModel.setResultsPerPage(resultsPerPage.getValue());
		
		Iterator<Component> iter = mainLayout.iterator();
		
		searchModel.getLstCriterions().clear();
		
		while(iter.hasNext()) {
			Component comp = iter.next();
			if (comp instanceof CriterionHLayout) {
				CriterionHLayout hlayout = (CriterionHLayout) comp;
				CriterionModel m = new CriterionModel();
				hlayout.set(m);
				searchModel.getLstCriterions().add(m);
			}
		}
		
		// TODO 
		// search
		List<Transaction> rows = null;
		try {
			rows = ui.transactionService.advancedSearch(searchModel);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	/**
	 * 
	 * @param model
	 */
	private void fill(SearchModel model) {
		allOfTheseWords.setValue(model.getWithAllOfTheWords());
		theExactPhrase.setValue(model.getWithTheExactPhrase());
		anyOfTheseWords.setValue(model.getWithAtLeastOneOfTheWords());
		noneOfTheseWords.setValue(model.getWithoutTheWords());
		resultsPerPage.setValue(model.getResultsPerPage());
		
		mainLayout.removeAllComponents();
		
		firstRow = null;
		
		mainLayout.addComponents(form, displayRowCount, Box.createVerticalBox(5));//, firstRow, Box.createVerticalBox(5), buttonPane, Box.createVerticalBox(5));
		mainLayout.setComponentAlignment(form, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(displayRowCount, Alignment.TOP_LEFT);
//		mainLayout.setComponentAlignment(firstRow, Alignment.TOP_LEFT);
//		mainLayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
		
		int size = model.getLstCriterions().size();
		if (size > 0) {
			for (CriterionModel c : model.getLstCriterions()) {
				if (firstRow == null) {
					firstRow = new CriterionHLayout(mainLayout, c, true);
					mainLayout.addComponents(firstRow);
					mainLayout.setComponentAlignment(firstRow, Alignment.TOP_LEFT);
				} else {
					HorizontalLayout newRow = new CriterionHLayout(mainLayout, c);
					mainLayout.addComponents(newRow);
					mainLayout.setComponentAlignment(newRow, Alignment.TOP_LEFT);
				}
			}
		} else {
			firstRow = new CriterionHLayout(mainLayout, null, true);
			mainLayout.addComponents(firstRow);
			mainLayout.setComponentAlignment(firstRow, Alignment.TOP_LEFT);
		}
		mainLayout.addComponents(Box.createVerticalBox(5), buttonPane, Box.createVerticalBox(5));
		mainLayout.setComponentAlignment(buttonPane, Alignment.BOTTOM_RIGHT);
	}
	
	public static void open(ResultCallback callback, SearchModel searchModel) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AdvancedSearchWindow w = new AdvancedSearchWindow();
        
        w.fill(searchModel);
        
        w.btnSearch.addClickListener(e -> {
			List<Transaction> results =  w.doSearchWithMultiThreads(searchModel);
			w.close();
			callback.onSuccessful(results);
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField allOfTheseWords = new TextField("匹配全部关键字:", "");
	private TextField theExactPhrase = new TextField("匹配精准词汇:","");
	private TextField anyOfTheseWords = new TextField("匹配任意关键字:","");
	private TextField noneOfTheseWords = new TextField("不匹配关键字:","");
	private ComboBox<Integer> resultsPerPage = null;
	private FormLayout form = null;
	private CriterionHLayout firstRow = null;
	private HorizontalLayout displayRowCount = null;
	private HorizontalLayout buttonPane = null;
	private VerticalLayout mainLayout =null;
	private Button btnSearch;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}

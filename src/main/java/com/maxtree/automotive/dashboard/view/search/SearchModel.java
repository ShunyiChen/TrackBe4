package com.maxtree.automotive.dashboard.view.search;

import java.util.ArrayList;
import java.util.List;

public class SearchModel {

	public String getWithAllOfTheWords() {
		return withAllOfTheWords;
	}

	public void setWithAllOfTheWords(String withAllOfTheWords) {
		this.withAllOfTheWords = withAllOfTheWords;
	}

	public String getWithTheExactPhrase() {
		return withTheExactPhrase;
	}

	public void setWithTheExactPhrase(String withTheExactPhrase) {
		this.withTheExactPhrase = withTheExactPhrase;
	}

	public String getWithAtLeastOneOfTheWords() {
		return withAtLeastOneOfTheWords;
	}

	public void setWithAtLeastOneOfTheWords(String withAtLeastOneOfTheWords) {
		this.withAtLeastOneOfTheWords = withAtLeastOneOfTheWords;
	}

	public String getWithoutTheWords() {
		return withoutTheWords;
	}

	public void setWithoutTheWords(String withoutTheWords) {
		this.withoutTheWords = withoutTheWords;
	}

	public List<CriterionModel> getLstCriterions() {
		return lstCriterions;
	}

	public void setLstCriterions(List<CriterionModel> lstCriterions) {
		this.lstCriterions = lstCriterions;
	}

	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	private String withAllOfTheWords = "";
	private String withTheExactPhrase = "";
	private String withAtLeastOneOfTheWords = "";
	private String withoutTheWords = "";
	private List<CriterionModel> lstCriterions = new ArrayList<>();
	private Integer resultsPerPage = 20;
}

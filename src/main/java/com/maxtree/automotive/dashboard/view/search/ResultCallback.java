package com.maxtree.automotive.dashboard.view.search;

import java.util.List;

import com.maxtree.automotive.dashboard.domain.Transaction;

public interface ResultCallback {

	void onSuccessful(List<Transaction> results);
}

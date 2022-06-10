/*
 * ============================================================================
 *
 * Copyright (c) 2022 YouTransactor
 *
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of YouTransactor
 * ("Confidential Information"). You  shall not disclose or redistribute such
 * Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with YouTransactor.
 *
 * This software is provided by YouTransactor AS IS, and YouTransactor
 * makes no representations or warranties about the suitability of the software,
 * either express or implied, including but not limited to the implied warranties
 * of merchantability, fitness for a particular purpose or non-infringement.
 * YouTransactor shall not be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages suffered by licensee as the
 * result of using, modifying or distributing this software or its derivatives.
 *
 * ==========================================================================
 */
package com.youtransactor.sampleapp.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youTransactor.uCube.rpc.Currency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CurrencyAdapter extends BaseAdapter {

	private final List<Currency> currencyList = new ArrayList<>();

	@Override
	public int getCount() {
		return currencyList.size();
	}

	@Override
	public Object getItem(int position) {
		return currencyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(Currency currency) {
		currencyList.add(currency);
		Collections.sort(currencyList, new Currency.ByLabelComparator());
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TextView(parent.getContext());
			convertView.setPadding(20, 20, 20, 20);
		}

		((TextView) convertView).setText(currencyList.get(position).getLabel());

		return convertView;
	}

}

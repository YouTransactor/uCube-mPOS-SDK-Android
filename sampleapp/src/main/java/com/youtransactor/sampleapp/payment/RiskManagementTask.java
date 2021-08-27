/*
 * Copyright (C) 2011-2020, YouTransactor. All Rights Reserved.
 * <p/>
 * Use of this product is contingent on the existence of an executed license
 * agreement between YouTransactor or one of its sublicensee, and your
 * organization, which specifies this software's terms of use. This software
 * is here defined as YouTransactor Intellectual Property for the purposes
 * of determining terms of use as defined within the license agreement.
 */
package com.youtransactor.sampleapp.payment;

import android.app.Activity;
import android.app.AlertDialog;

import com.youTransactor.uCube.ITaskCancelListener;
import com.youTransactor.uCube.ITaskMonitor;
import com.youTransactor.uCube.TaskEvent;
import com.youTransactor.uCube.Tools;
import com.youTransactor.uCube.payment.task.IRiskManagementTask;
import com.youTransactor.uCube.payment.PaymentContext;
import com.youTransactor.uCube.rpc.EMVApplicationDescriptor;

public class RiskManagementTask implements IRiskManagementTask {

	private Activity activity;
	private ITaskMonitor monitor;
	private PaymentContext paymentContext;
	private byte[] tvr;
	private AlertDialog alertDialog;

	public RiskManagementTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	public byte[] getTVR() {
		return tvr;
	}

	@Override
	public PaymentContext getContext() {
		return paymentContext;
	}

	@Override
	public void setContext(PaymentContext context) {
		this.paymentContext = context;
	}

	@Override
	public void execute(ITaskMonitor monitor) {
		this.monitor = monitor;

		activity.runOnUiThread(() -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			builder.setTitle("Risk management");
			builder.setCancelable(false);
			builder.setMessage("Is card stolen ?");

			builder.setPositiveButton("Yes", (dialog, which) -> {
				dialog.dismiss();

				end(new byte[] {0, 0b10000, 0, 0, 0});
			});

			builder.setNegativeButton("No", (dialog, which) -> {
				dialog.dismiss();

				end(new byte[] {0, 0, 0, 0, 0});
			});

			alertDialog = builder.create();
			alertDialog.show();
		});
	}

	@Override
	public void cancel(ITaskCancelListener taskCancelListener) {
		if(alertDialog != null && alertDialog.isShowing())
			alertDialog.dismiss();

		new Thread(() -> monitor.handleEvent(TaskEvent.CANCELLED)).start();
		taskCancelListener.onCancelFinish(true);
	}

	private void end(byte[] tvr) {
		this.tvr = tvr;

		EMVApplicationDescriptor selectedApplication = paymentContext.selectedApplication;
		if (selectedApplication != null) {
			String selectedAID = Tools.bytesToHex(selectedApplication.getAid()).substring(0, 10);

			switch (selectedAID) {
				case "A000000003":
					paymentContext.applicationVersion = 140;
					break;
				case "A000000004":
					paymentContext.applicationVersion = 202;
					break;
				case "A000000042":
					paymentContext.applicationVersion = 203;
					break;
			}
		}

		new Thread(() -> monitor.handleEvent(TaskEvent.SUCCESS)).start();
	}


}

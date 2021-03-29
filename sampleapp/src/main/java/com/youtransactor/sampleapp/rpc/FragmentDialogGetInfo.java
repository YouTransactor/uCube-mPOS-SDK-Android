/*
 * Copyright (C) 2011-2020, YouTransactor. All Rights Reserved.
 * <p/>
 * Use of this product is contingent on the existence of an executed license
 * agreement between YouTransactor or one of its sublicensee, and your
 * organization, which specifies this software's terms of use. This software
 * is here defined as YouTransactor Intellectual Property for the purposes
 * of determining terms of use as defined within the license agreement.
 */
package com.youtransactor.sampleapp.rpc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.youTransactor.uCube.rpc.DeviceInfos;
import com.youtransactor.sampleapp.R;
import com.youtransactor.sampleapp.YTProduct;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.util.Objects;

public class FragmentDialogGetInfo extends DialogFragment {

    private DeviceInfos deviceInfos;
    private YTProduct ytProduct;

    public FragmentDialogGetInfo(DeviceInfos deviceInfos, YTProduct ytProduct) {
        this.deviceInfos = deviceInfos;
        this.ytProduct = ytProduct;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle("uCube Informations")
                .setPositiveButton("ok", (dialog, whichButton) -> dialog.dismiss());

        setCancelable(false);

        View rootView = Objects.requireNonNull(getActivity())
                .getLayoutInflater().inflate(R.layout.fragment_get_info, null);

        //UI
        TextView terminalSerialNumber = rootView.findViewById(R.id.terminal_serial_number);
        TextView terminalState = rootView.findViewById(R.id.terminal_state);
        TextView batteryState = rootView.findViewById(R.id.battery_state);
        TextView svppVersion = rootView.findViewById(R.id.SVPP_version);
        TextView partNumber = rootView.findViewById(R.id.part_number);
        TextView OSVersion = rootView.findViewById(R.id.OS_version);
        TextView NFCModuleState = rootView.findViewById(R.id.NFC_Module_state);
        TextView emvConfigurationVersion = rootView.findViewById(R.id.emv_configuration_version);
        TextView emvClessConfigurationVersion = rootView.findViewById(R.id.emv_cless_configuration_version);
        TextView automaticPowerOffTimeOut = rootView.findViewById(R.id.automatic_power_off_time_out);
        TextView USBCapability = rootView.findViewById(R.id.USB_capability);
        TextView NFCCapability = rootView.findViewById(R.id.NFC_capability);
        TextView NFCFirmwareState = rootView.findViewById(R.id.NFC_Firmware_State);
        TextView NFCFirmwareVersion = rootView.findViewById(R.id.NFC_Firmware_Version);
        TextView EMVL1_CLESS_LIB_VERSION = rootView.findViewById(R.id.EMVL1_Cell_Lib_Version);
        TextView bleFirmwareVersion = rootView.findViewById(R.id.ble_version);
        TextView resourcesVersion = rootView.findViewById(R.id.resources_file_version);
        TextView merchantLocale = rootView.findViewById(R.id.merchant_locale);
        TextView supportedLocaleList = rootView.findViewById(R.id.supported_locale_list);

        if(ytProduct == YTProduct.uCubeTouch) {
            rootView.findViewById(R.id.nfc_section).setVisibility(View.GONE);

        } else if (ytProduct == YTProduct.uCube) {
            rootView.findViewById(R.id.nfc_section).setVisibility(View.VISIBLE);

            NFCModuleState.setText(nfcState(deviceInfos.getNfcModuleState()));

            if (deviceInfos.getNfcModuleState() != 0) {
                NFCFirmwareState.setText(deviceInfos.getNfcFirmwareState());
                NFCFirmwareVersion.setText(deviceInfos.getNfcFirmware());
                EMVL1_CLESS_LIB_VERSION.setText(deviceInfos.getEmvL1ClessLibVersion());
            } else {
                NFCCapability.setText("NONE");
                NFCFirmwareState.setVisibility(View.GONE);
                NFCFirmwareVersion.setVisibility(View.GONE);
                EMVL1_CLESS_LIB_VERSION.setVisibility(View.GONE);
            }

        }

        terminalSerialNumber.setText(deviceInfos.getSerial());
        terminalState.setText(deviceInfos.getTerminalState());
        svppVersion.setText(deviceInfos.getSvppFirmware());
        partNumber.setText(deviceInfos.getPartNumber());
        OSVersion.setText(deviceInfos.getOsVersion());
        if(StringUtils.isEmpty(deviceInfos.getBleFirmwareVersion())) {
            rootView.findViewById(R.id.ble_section).setVisibility(View.GONE);
        }else {
            rootView.findViewById(R.id.ble_section).setVisibility(View.VISIBLE);
            bleFirmwareVersion.setText(deviceInfos.getBleFirmwareVersion());
        }

        if(StringUtils.isEmpty(deviceInfos.getResourcesVersion())) {
            rootView.findViewById(R.id.resource_file_section).setVisibility(View.GONE);
        }else {
            rootView.findViewById(R.id.resource_file_section).setVisibility(View.VISIBLE);
            resourcesVersion.setText(deviceInfos.getResourcesVersion());
        }

        emvConfigurationVersion.setText(deviceInfos.getIccEmvConfigVersion());
        emvClessConfigurationVersion.setText(deviceInfos.getNfcEmvConfigVersion());
        USBCapability.setText(deviceInfos.isUsbCapability() ? "USB Capability" : "NO USB Capability");

        if(deviceInfos.getBatteryState() < 0)
            batteryState.setText("unknown");
        else
            batteryState.setText(deviceInfos.getBatteryState()+ "%");

        if(deviceInfos.getAutoPowerOffTimeout() < 0)
            automaticPowerOffTimeOut.setText("unknown");
        else
            automaticPowerOffTimeOut.setText(deviceInfos.getAutoPowerOffTimeout() + " Sec");

        if(deviceInfos.getMerchantLocale() != null) {
            merchantLocale.setText(deviceInfos.getMerchantLocale());
        }

        if(deviceInfos.getSupportedLocaleList() != null && !deviceInfos.getSupportedLocaleList().isEmpty()) {
            StringBuilder locales = new StringBuilder();
            for(String locale : deviceInfos.getSupportedLocaleList()) {
                locales.append(locale).append(", ");
            }

            supportedLocaleList.setText(locales.toString());
        }

        b.setView(rootView);
        return b.create();
    }

    private String nfcState(byte nfcModuleState) {
        String nfcStateLabel ="";

        switch (nfcModuleState) {
            case 0x00:
                nfcStateLabel = "no mpos module available";
                break;
            case 0x01:
                nfcStateLabel = "mpos module initializing";
                break;
            case 0x02:
                nfcStateLabel = "mpos initialization done";
                break;
            case 0x03:
                nfcStateLabel = "mpos module is ready";
                break;
            case 0x04:
                nfcStateLabel = "mpos module is in bootloader mode";
                break;
            case 0x05:
                nfcStateLabel = "mpos bootloader initializing";
                break;
            case 0x06:
                nfcStateLabel = "mpos module faces an internal error";
                break;
            case 0x07:
                nfcStateLabel = "mpos module firmware not loaded";
                break;
            case 0x08:
                nfcStateLabel = "mpos firmware update is ongoing";
                break;
            case 0x09:
                nfcStateLabel = "mpos app firmware is corrupted";
                break;
            case 0x10:
                nfcStateLabel = "mpos bootloader firmware is corrupted";
                break;
        }

        return nfcStateLabel;
    }

}


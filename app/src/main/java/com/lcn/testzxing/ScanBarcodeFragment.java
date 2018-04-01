package com.lcn.testzxing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.lcn.testzxing.events.BarcodeResultEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class ScanBarcodeFragment extends Fragment {

    public static final String BUNDLE_SCAN_TYPE = "BUNDLE_SCAN_TYPE";
    public enum SCAN_TYPE {ALL, QR_CODE}
    private SCAN_TYPE scanType = SCAN_TYPE.ALL;

    private BarcodeView barcodeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            scanType = (SCAN_TYPE) bundle.getSerializable(BUNDLE_SCAN_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_bar_code, container, false);
        barcodeView = view.findViewById(R.id.scanBarcodeFragment_barcodeView);
        barcodeView.decodeContinuous(callback);
        setBarcodeViewScanType();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    private void setBarcodeViewScanType() {
        switch (scanType) {
            case QR_CODE:
                Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
                barcodeView.setDecoderFactory(new DefaultDecoderFactory(formats));
                break;
            case ALL:
            default:
                break;
        }
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            String barcode = result.getText();
            if (TextUtils.isEmpty(barcode)) {
                return;
            }
            postBarcodeResultEvent(barcode);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    private void postBarcodeResultEvent(String resultText) {
        BarcodeResultEvent event = new BarcodeResultEvent();
        event.setBarcode(resultText);
        EventBus.getDefault().post(event);
    }

}

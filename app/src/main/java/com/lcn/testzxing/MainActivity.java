package com.lcn.testzxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static com.lcn.testzxing.ScanBarcodeFragment.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int RESULT_CODE_CAMERA = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mainActivity_scanAllBarcodeButton).setOnClickListener(this);
        findViewById(R.id.mainActivity_scanQrCodeButton).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case RESULT_CODE_CAMERA:
                onScanBarCodeResult(data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void onScanBarCodeResult(Intent intent) {
        if (intent == null) {
            return;
        }

        String barcode = intent.getStringExtra(ScanBarcodeActivity.INTENT_BAR_CODE_RESULT);
        layoutBarCodeText(barcode);
    }

    private void layoutBarCodeText(String barcode) {
        TextView textView = findViewById(R.id.mainActivity_barcodeTextView);
        textView.setText(barcode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainActivity_scanAllBarcodeButton:
                startScanBarcodeActivity(SCAN_TYPE.ALL);
                break;
            case R.id.mainActivity_scanQrCodeButton:
                startScanBarcodeActivity(SCAN_TYPE.QR_CODE);
                break;
        }
    }

    private void startScanBarcodeActivity(SCAN_TYPE type) {
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_SCAN_TYPE, type);
        intent.putExtras(bundle);
        startActivityForResult(intent, RESULT_CODE_CAMERA);
    }
}

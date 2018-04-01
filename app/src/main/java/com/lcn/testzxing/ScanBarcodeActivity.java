package com.lcn.testzxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lcn.testzxing.events.BarcodeResultEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ScanBarcodeActivity extends AppCompatActivity {

    public static final String INTENT_BAR_CODE_RESULT = "INTENT_BAR_CODE_RESULT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code);

        if (savedInstanceState == null) {
            ScanBarcodeFragment fragment = new ScanBarcodeFragment();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(ScanBarcodeFragment.BUNDLE_SCAN_TYPE, ScanBarcodeFragment.SCAN_TYPE.ALL);
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.scanBarcodeActivity_frameLayout, fragment).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BarcodeResultEvent event) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_BAR_CODE_RESULT, event.getBarcode());
        setResult(RESULT_OK, intent);
        finish();
    }

}

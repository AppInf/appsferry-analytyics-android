package com.appsferry.app;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.appsferry.analytics.AfAnalytics;
import com.appsferry.analytics.reporter.model.EventType;
import com.appsferry.app.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.switchPrivacy.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        binding.btnReport.setOnClickListener(v -> {
            EventInfo eventInfo = new EventInfo();
            eventInfo.name = "any name";
            eventInfo.id = SystemClock.currentThreadTimeMillis();
            eventInfo.extra.put("ext1", "Extra A");
            eventInfo.extra.put("beautiful", true);
            AfAnalytics.report(eventInfo, "test_eid", EventType.action);
        });
    }

    public static class EventInfo {
        public String name = "";
        public long id = 10098;
        public Map<String, Object> extra = new HashMap<>();

        public EventInfo() {
        }

        public EventInfo(String name, long id, Map<String, Object> extra) {
            this.name = name;
            this.id = id;
            this.extra = extra;
        }
    }

}

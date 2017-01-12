/*
 * Copyright 2017 Entertainment portal of Americas LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appcon.statisticscollectors;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appcon.magmaNx.asset.NxMediaAsset;
import com.appcon.magmaNx.statistics.NxMediaState;
import com.appcon.magmaNx.statistics.NxStatisticsCollector;
import com.appcon.magmaNx.statistics.NxStatisticsData;

public class EventsTesterActivity extends AppCompatActivity {

    NxStatisticsData status;
    long position = 0;
    NxStatisticsCollector statisticsCollector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_tester);

        TextView statusTxtV = (TextView) findViewById(R.id.status);
        statusTxtV.setText(NxMediaState.CREATED.getValue());

        TextView positionTxtV = (TextView) findViewById(R.id.position);
        positionTxtV.setText("0");

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(onPositionUpdate);

        instantiateStatistic();
    }

    SeekBar.OnSeekBarChangeListener onPositionUpdate = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            position = i;
            TextView positionTxtV = (TextView) findViewById(R.id.position);
            positionTxtV.setText("" + position);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            status.setPosition(position);
            status.setState(NxMediaState.SEEKED);
            statisticsCollector.update(status);

            status.setState(NxMediaState.PLAYING);
            statisticsCollector.update(status);
        }
    };

    private void instantiateStatistic() {
        // Initialize the status object
        status = new NxStatisticsData();

        // First you should instantiate the statisticsCollector. Statistics Collection ID should be
        // provided to you by your Magma NX Agent.
        // For enhanced data analysis it is important to identify distinct devices. For that reason
        // all times a session is created and an update is sent the same unique tag for each device
        // should be attached.
        // For that reason we suggest you pass de device serial number, which is reachable using
        // SERIAL constant of Android.os.Build class.
        // Some devices doesn't provide such a number. You can use the MAC Address instead or create
        // a pseudo unique hash that and store it using local files of my by SQLite.
        statisticsCollector =
                new NxStatisticsCollector("bWFnbWEtaW5zdGFuY2U6ZGVtbzthY2NvdW50OjAwMDAwO3Rva2VuOndlZnNmYXNkZzNnMzRnZmVyNDNn", Build.SERIAL);

        // You need to specify the Media Asset data you wants to monitor.
        // 3 parameters are mandatory: assetId, accountId, isLive
        NxMediaAsset asset = new NxMediaAsset("asset_name", "000000", true);
        status.setMediaAsset(asset);

        // After setting the Media Asset data, you should call setState using the status "CREATED"
        // to indicate the player instance just been created, and send it by updating the status.
        status.setState(NxMediaState.CREATED);
        statisticsCollector.update(status);


        statisticsCollector.setDataCollector(new NxStatisticsCollector.NxDataCollector() {
            @Override
            public NxStatisticsData onCollect() {
                return status;
            }
        });

    }

    public void handleStatusChange(View view) {
        TextView textView = (TextView) findViewById(R.id.status);
        String tag = (String) view.getTag();

        textView.setText(tag);

        if (tag.equals(NxMediaState.CREATED.getValue())) {
            status.setState((NxMediaState.CREATED));
        } else if (tag.equals(NxMediaState.PLAYING.getValue())) {
            status.setState((NxMediaState.PLAYING));
        } else if (tag.equals(NxMediaState.STOPPED.getValue())) {
            status.setState((NxMediaState.STOPPED));
        } else if (tag.equals(NxMediaState.BUFFERING.getValue())) {
            status.setState((NxMediaState.BUFFERING));
        } else if (tag.equals(NxMediaState.COMPLETED.getValue())) {
            status.setState((NxMediaState.COMPLETED));
        } else if (tag.equals(NxMediaState.MEDIA_ERROR.getValue())) {
            status.setState((NxMediaState.MEDIA_ERROR));
        } else if (tag.equals(NxMediaState.SECURITY_ERROR.getValue())) {
            status.setState((NxMediaState.SECURITY_ERROR));
        } else if (tag.equals(NxMediaState.SEEKED.getValue())) {
            status.setState((NxMediaState.SEEKED));
        }

        statisticsCollector.update(status);

    }
}

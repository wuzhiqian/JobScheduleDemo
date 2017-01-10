package com.wzq.jobscheduledemo;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.usage.NetworkStats;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final int RECEIVE_BOOT_COMPLETED_REQUEST_CODE = 1;
    int jobId = 1;
    JobScheduler jobScheduler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    RECEIVE_BOOT_COMPLETED_REQUEST_CODE);
        }
        else
        {
            doSchedule();
        }


    }

    private void doSchedule()
    {
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(++jobId, new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
        builder.setPeriodic(5000);
        builder.setPersisted(true);
        builder.setRequiresCharging(false);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RECEIVE_BOOT_COMPLETED_REQUEST_CODE)
        {
            boolean isGranted = true;
            if(grantResults.length <= 0)
                isGranted = false;
            else {
                for (int grant : grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                        break;
                    }
                }
            }
            if(isGranted)
            {
                doSchedule();
            }
            else
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                            RECEIVE_BOOT_COMPLETED_REQUEST_CODE);
                }
            }
        }
    }
}

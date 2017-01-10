package com.wzq.jobscheduledemo;


import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.List;

/**
 * Created by wzq on 17-1-10.
 */

public class JobSchedulerService extends JobService {

    private Handler jobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(JobSchedulerService.this, "打不死的程序", Toast.LENGTH_SHORT).show();
            jobFinished((JobParameters)msg.obj, false);
            return true;
        }
    });
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(JobSchedulerService.this, "小强挂了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onStartJob(JobParameters params) {
        jobHandler.sendMessage(Message.obtain(jobHandler, 1, params));
        if(!checkService())
        {
            startService(new Intent(JobSchedulerService.this, TestService.class));
            bindService(new Intent(JobSchedulerService.this, TestService.class), conn,  BIND_AUTO_CREATE);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobHandler.removeMessages(1);
        return false;
    }
    private boolean checkService() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.processName.equals(getPackageName() + ":testService")) {
                return true;
            }
        }
        return false;
    }

}

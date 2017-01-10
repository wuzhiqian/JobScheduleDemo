package com.wzq.jobscheduledemo;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;

/**
 * Created by wzq on 17-1-10.
 */

public class TestService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private TestBinder testBinder;

    public TestService(String name) {
        super(name);
    }


    public TestService() {
        this("TestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        testBinder = new TestBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return testBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(TestService.this, "开始小强", Toast.LENGTH_SHORT).show();
        try {
            testBinder.print();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    class TestBinder extends TestAidl.Stub {

        @Override
        public void print() throws RemoteException {
            Toast.makeText(TestService.this, "试试小强", Toast.LENGTH_SHORT).show();
        }
    }



}

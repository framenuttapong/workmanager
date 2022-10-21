package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork(){
        for(int i = 0; i < 5; i++){
            if(stop) return null;
            Log.i("vac", "doWork 1: "+ i);
            Data data = new Data.Builder().putInt("num", i).build();
            setProgressAsync(data); //show progress
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        Data data = new Data.Builder().putInt("ret", 123).build();
        return Result.success(data); //show result
    }

    boolean stop;

    @Override
    public void onStopped() {
        super.onStopped();
        stop = true;
        Log.i("vac", "onStopped: ");
    }
}
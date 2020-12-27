package com.example.colornoteplus;

import android.util.Log;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Task implements Runnable {

    private CyclicBarrier barrier;
    private Long sleepTime;
    private MyTask task;

    public Task(CyclicBarrier barrier, Long sleepTime, MyTask task) {
        this.barrier = barrier;
        this.sleepTime = sleepTime;
        this.task = task;
    }

    public interface MyTask{
        void runTask();
        void runPostTask();
    }

    @Override
    public void run() {

        try {
            Thread.sleep(sleepTime);

            if (task != null){
                Log.d("SYNC","Task is Running");
                task.runTask();
                Log.d("SYNC","Task completed");
            }

            barrier.await();

        }catch (InterruptedException | BrokenBarrierException e){
            e.printStackTrace();
        }

        if (task != null){
            Log.d("SYNC","Post task is Running");
            task.runPostTask();
            Log.d("SYNC","Post task is Starting");
        }

    }
}

package com.example.ksoap2.wsclient;

import android.os.AsyncTask;

/**
 * Callback interface to monitor lifecycle of an {@link AsyncTask}
 * 
 * @author Thiranjith
 */
public interface IProgressTracker {

    void onStartProgress();

    void onStopProgress();
}

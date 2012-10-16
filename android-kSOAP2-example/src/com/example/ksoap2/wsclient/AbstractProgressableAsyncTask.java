package com.example.ksoap2.wsclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author Thiranjith
 * 
 * @param <P>
 * @param <R>
 */
public abstract class AbstractProgressableAsyncTask<P, R> extends AsyncTask<P, Void, R> {

    private final String TAG = getClass().getName();

    private OnAsyncTaskCompleteListener<R> taskCompletionListener;
    private IProgressTracker progressTracker;
    // Most recent exception (used to diagnose failures)
    private Exception mostRecentException;

    public AbstractProgressableAsyncTask() {
    }

    public final void setOnTaskCompletionListener(OnAsyncTaskCompleteListener<R> taskCompletionListener) {
        this.taskCompletionListener = taskCompletionListener;
    }

    public final void setProgressTracker(IProgressTracker progressTracker) {
        if (progressTracker != null) {
            this.progressTracker = progressTracker;
        }
    }

    @Override
    protected final void onPreExecute() {
        if (progressTracker != null) {
            this.progressTracker.onStartProgress();
        }
    }

    /**
     * Invoke the web service request
     */
    @Override
    protected final R doInBackground(P... parameters) {
        mostRecentException = null;
        R result = null;

        try {
            result = performTaskInBackground(parameters[0]);
        } catch (Exception e) {
            Log.e(TAG, "Failed to invoke the web service: ", e);
            mostRecentException = e;
        }

        return result;
    }

    protected abstract R performTaskInBackground(P parameter) throws Exception;

    /**
     * @param result
     *            to be sent back to the observer (typically an {@link Activity} running on the UI Thread). This can be <code>null</code> if
     *            an error occurs while attempting to invoke the web service (e.g. web service was unreachable, or network I/O issue etc.)
     */
    @Override
    protected final void onPostExecute(R result) {
        if (progressTracker != null) {
            progressTracker.onStopProgress();
        }

        if (taskCompletionListener != null) {
            if (result == null || mostRecentException != null) {
                taskCompletionListener.onTaskFailed(mostRecentException);

            } else {
                taskCompletionListener.onTaskCompleteSuccess(result);
            }
        }

        // clean up listeners since we are done with this task
        progressTracker = null;
        taskCompletionListener = null;
    }
}

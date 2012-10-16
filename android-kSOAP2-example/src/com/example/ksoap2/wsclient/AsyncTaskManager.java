package com.example.ksoap2.wsclient;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Manages lifecycle for asynchronous tasks
 * 
 * @author Thiranjith
 */
public final class AsyncTaskManager implements IProgressTracker {

    private final ProgressDialog progressDialog;

    public AsyncTaskManager(Context context) {
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    /**
     * Executes a task in the background thread, while displaying a busy dialog (non cancellable).
     * 
     * @param task
     *            {@link AbstractProgressableAsyncTask}
     * @param request
     *            request for the background task
     * @param progressLabel
     *            label to be displayed when the progress dialog is being displayed.
     * @param onTaskCompletedListener
     *            {@link OnAsyncTaskCompleteListener} to be notified once the task is completed.
     */
    public <T, P> void executeTask(AbstractProgressableAsyncTask<P, T> task, P request, CharSequence progressLabel,
            OnAsyncTaskCompleteListener<T> onTaskCompletedListener) {
        this.progressDialog.setMessage(progressLabel);

        task.setOnTaskCompletionListener(onTaskCompletedListener);
        task.setProgressTracker(this);
        task.execute(request);
    }

    // ------------------------------------------------------------------------
    // Progress Handlers
    // ------------------------------------------------------------------------

    @Override
    public void onStartProgress() {
        progressDialog.show();
    }

    @Override
    public void onStopProgress() {
        progressDialog.dismiss();
    }
}

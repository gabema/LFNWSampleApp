package gabema.lfnwsampleapp;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

public final class AsyncTaskUtility
{
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static <T> void execute(AsyncTask<T, ?, ?> task, T... params)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			if (s_executor == null)
				s_executor = getExecutor();

			task.executeOnExecutor(s_executor, params);
		}
		else
		{
			task.execute(params);
		}
	}

	private static synchronized Executor getExecutor() {
		if (s_executor == null)
			s_executor = Executors.newCachedThreadPool();
		return s_executor;
	}

	private static Executor s_executor = Executors.newCachedThreadPool();
	private AsyncTaskUtility() { }
}

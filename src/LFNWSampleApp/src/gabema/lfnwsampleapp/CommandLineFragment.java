package gabema.lfnwsampleapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public final class CommandLineFragment extends Fragment
{
	public CommandLineFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.command_line, null);
		m_output = (TextView) view.findViewById(R.id.output);
		m_output.setHint("output here...");

		m_command = (EditText) view.findViewById(R.id.command);
		m_command.setHint("command here...");
		m_command.setOnEditorActionListener(createActionListener());

		return view;
	}

	public void startExecuteCommand(String command)
	{
		if (m_runCommandTask != null)
			m_runCommandTask.cancel(true);

		m_runCommandTask = new AsyncTask<String, Void, String>()
		{
			@Override
			protected String doInBackground(String... params)
			{
				StringBuffer output = new StringBuffer();

				// from: http://gimite.net/en/index.php?Run%20native%20executable%20in%20Android%20App
				try {
				    // Executes the command.
				    Process process = Runtime.getRuntime().exec(params[0]);

				    // Reads stdout.
				    // NOTE: You can write to stdin of the command using
				    //       process.getOutputStream().
				    BufferedReader reader = new BufferedReader(
				            new InputStreamReader(process.getInputStream()));
				    int read;
				    char[] buffer = new char[4096];
				    while ((read = reader.read(buffer)) > 0)
				        output.append(buffer, 0, read);

				    reader.close();

				    // Waits for the command to finish.
				    process.waitFor();

				} catch (IOException e) {
					// show stacktrace in output window
					e.printStackTrace();
					output.append(e.getMessage());
				} catch (InterruptedException e) {
					// show stacktrace in output window
					e.printStackTrace();
					output.append(e.getMessage());
				}

				return output.toString();
			}

			@Override
			protected void onPostExecute(String result)
			{
				m_output.setText(result);
			}
		};
		AsyncTaskUtility.<String>execute(m_runCommandTask, command);
	}

	private OnEditorActionListener createActionListener() {
		return new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				{
					startExecuteCommand(m_command.getText().toString());
					return true;
				}

				return false;
			}
		};
	}

	protected static final String TAG = "CommandLineFragment";

	private TextView m_output;
	private EditText m_command;
	private AsyncTask<String, Void, String> m_runCommandTask;
}

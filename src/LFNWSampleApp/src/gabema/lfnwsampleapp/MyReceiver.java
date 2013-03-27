package gabema.lfnwsampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
	public MyReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		MainActivity mainActivity = (MainActivity) context;
		CommandLineFragment commandLineFragment = (CommandLineFragment) mainActivity.getSupportFragmentManager().findFragmentByTag("" + R.id.commandLineFragment);

		commandLineFragment.startExecuteCommand(intent.getStringExtra("command"));

		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		// throw new UnsupportedOperationException("Not yet implemented");
	}
}

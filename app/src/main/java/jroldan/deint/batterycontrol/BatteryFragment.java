package jroldan.deint.batterycontrol;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryFragment extends Fragment {

    private ProgressBar pbLevel;
    private TextView txvLevel;
    private ImageView imgStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        pbLevel = (ProgressBar)rootView.findViewById(R.id.pbLevel);
        txvLevel = (TextView) rootView.findViewById(R.id.txvLevel);
        imgStatus = (ImageView)rootView.findViewById(R.id.imgStatus);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Create an IntentFilter for the Intent.ACTION_BATTERY_CHANGED action
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // Registering the BroadcastReceiver
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        // When the Fragment ends the BroadcastReceiver gets unregistered
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    /**
     * Broadcast which depends on the Fragment lifecycle
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Reading the information that comes with the Intent: level, status...
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = (float)level / (float)scale;

            pbLevel.setProgress((int)(batteryPct*100));
            txvLevel.setText((int)(batteryPct*100) + "%");

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    imgStatus.setImageResource(R.mipmap.ic_charging);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    imgStatus.setImageResource(R.mipmap.ic_full);
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    imgStatus.setImageResource(R.mipmap.ic_unplugged);
                    break;
                default:
                    imgStatus.setImageResource(R.mipmap.ic_launcher);
                    break;
            }
        }
    };
}

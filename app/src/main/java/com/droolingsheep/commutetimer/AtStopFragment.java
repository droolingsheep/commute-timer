package com.droolingsheep.commutetimer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by asbransc on 3/17/15.
 */
public class AtStopFragment extends Fragment implements View.OnClickListener {

    private static final String DIRECTION = "direction";
    private Direction mDirection;
    private Button mAtStopButton;

    public AtStopFragment() {
        //Required public constructor
    }

    public static AtStopFragment newInstance(Direction dir) {
        AtStopFragment f = new AtStopFragment();
        Bundle args = new Bundle();
        args.putSerializable(DIRECTION, dir);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mDirection = (Direction) args.getSerializable(DIRECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_at_stop, container, false);
        mAtStopButton = (Button) v.findViewById(R.id.at_stop_button);
        mAtStopButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == mAtStopButton) {
            if (mDirection.isTransfer()) {
                CommuteRecord.getInstance().recordTime(Step.AT_STOP_2);
            } else {
                CommuteRecord.getInstance().recordTime(Step.AT_STOP_1);
            }
            getFragmentManager().beginTransaction().replace(R.id.container, LineChoiceFragment.newInstance(mDirection)).commit();
        }
    }
}

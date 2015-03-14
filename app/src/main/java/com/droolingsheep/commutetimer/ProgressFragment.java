package com.droolingsheep.commutetimer;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment implements View.OnClickListener {

    private static final String ROUTE = "route";
    private Button mButton;
    //TODO make this an enum and make "At Stop" its own fragment.
    //TODO handle transfers
    private int[] mSteps = {R.string.at_stop,
                            R.string.on_bus,
                            R.string.off_bus,
                            R.string.arrive};
    private int mCurrentStep = 0;
    private Route mRoute;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProgressFragment.
     * @param route
     */
    public static ProgressFragment newInstance(Route route) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putSerializable(ROUTE, route);
        fragment.setArguments(args);
        return fragment;
    }

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRoute = (Route) getArguments().getSerializable(ROUTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        mButton = (Button) v.findViewById(R.id.progress_button);
        mButton.setText(mSteps[0]);
        mButton.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        if (++mCurrentStep < mSteps.length) {
            mButton.setText(mSteps[mCurrentStep]);
        }
    }
}

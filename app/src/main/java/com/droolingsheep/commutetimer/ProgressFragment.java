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
    private static final String DIRECTION = "direction";
    private Button mButton;
    //TODO make this an enum and make "At Stop" its own fragment.
    //TODO handle transfers
    private int mCurrentStep = 0;
    private Route mRoute;
    private Button mTransferButton;
    private Direction mDirection;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProgressFragment.
     * @param route
     */
    public static ProgressFragment newInstance(Direction direction, Route route) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putSerializable(ROUTE, route);
        args.putSerializable(DIRECTION, direction);
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
            mDirection = (Direction) getArguments().getSerializable(DIRECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        mButton = (Button) v.findViewById(R.id.progress_button);
        mButton.setOnClickListener(this);
        mTransferButton = (Button) v.findViewById(R.id.transfer_button);
        mTransferButton.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        if (v == mButton) {
            switch (mCurrentStep) {
                case 0:
                    mCurrentStep = 1;
                    mButton.setText(R.string.off_bus);
                    break;
                case 1:
                    mCurrentStep = 2;
                    mButton.setText(R.string.arrive);
                    if (mDirection == Direction.WORK || mDirection == Direction.HOME) {
                        mTransferButton.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    //TODO record arrival time
                    //TODO bring up summary fragment
                    getActivity().finish();
                    break;
            }
        } else if (v == mTransferButton) {
            Direction transferDirection;
            switch(mDirection) {
                case HOME:
                    transferDirection = Direction.HOME_TRANSFER;
                    break;
                case WORK:
                    transferDirection = Direction.WORK_TRANSFER;
                    break;
                default:
                    throw new IllegalStateException("Direction must be HOME or WORK");
            }
            getFragmentManager().beginTransaction().replace(R.id.container, LineChoiceFragment.newInstance(transferDirection)).commit();
        }
    }
}

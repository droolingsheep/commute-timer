package com.droolingsheep.commutetimer;


import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//TODO add tracking of board/alight location
//TODO add tracking of bus number
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment implements View.OnClickListener {

    private static final String ROUTE = "route";
    private static final String DIRECTION = "direction";
    private Button mButton;
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
        SpannableStringBuilder transferLabel = new SpannableStringBuilder();
        Resources res = getResources();
        transferLabel.append(res.getString(R.string.transfer));
        TextAppearanceSpan largeSpan = new TextAppearanceSpan(getActivity(), R.style.large_text);
        TextAppearanceSpan smallSpan = new TextAppearanceSpan(getActivity(), R.style.small_text);
        transferLabel.setSpan(largeSpan, 0, transferLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        transferLabel.append('\n');
        int smallSpanStart = transferLabel.length();
        transferLabel.append("(");
        transferLabel.append(res.getString(R.string.at_stop));
        transferLabel.append(")");
        transferLabel.setSpan(smallSpan, smallSpanStart, transferLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //There's a bug in Lollipop that breaks spannable strings if "all caps" is set :(
        mTransferButton.setText(transferLabel);
        return v;
    }


    @Override
    public void onClick(View v) {
        if (v == mButton) {
            switch (mCurrentStep) {
                case 0:
                    mCurrentStep = 1;
                    if (mDirection.isTransfer()) {
                        CommuteRecord.getInstance().recordTime(Step.BOARD_2);
                    } else {
                        CommuteRecord.getInstance().recordTime(Step.BOARD_1);
                    }
                    mButton.setText(R.string.off_bus);
                    break;
                case 1:
                    mCurrentStep = 2;
                    if (mDirection.isTransfer()) {
                        CommuteRecord.getInstance().recordTime(Step.ALIGHT_2);
                    } else {
                        CommuteRecord.getInstance().recordTime(Step.ALIGHT_1);
                    }
                    mButton.setText(R.string.arrive);
                    if (!mDirection.isTransfer()) {
                        mTransferButton.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    CommuteRecord.getInstance().recordTime(Step.ARRIVE);
                    CommuteRecord.saveInstance();
                    //TODO bring up summary fragment instead of restarting
                    getActivity().recreate();
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
            CommuteRecord.getInstance().recordTime(Step.AT_STOP_2);
            getFragmentManager().beginTransaction().replace(R.id.container, LineChoiceFragment.newInstance(transferDirection)).commit();
        }
    }
}

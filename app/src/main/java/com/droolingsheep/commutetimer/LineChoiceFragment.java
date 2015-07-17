package com.droolingsheep.commutetimer;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChoiceFragment extends Fragment {

    private static final String DIRECTION = "direction";
    private Direction mDirection;
    private Route[] mRoutes;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LineChoiceFragment.
     * @param arg
     */
    public static LineChoiceFragment newInstance(Direction arg) {
        LineChoiceFragment fragment = new LineChoiceFragment();
        Bundle args = new Bundle();
        args.putSerializable(DIRECTION, arg);
        fragment.setArguments(args);
        return fragment;
    }

    public LineChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDirection = (Direction) getArguments().getSerializable(DIRECTION);
            switch(mDirection) {
                case WORK:
                    mRoutes = Route.sWorkRoutes;
                    break;
                case HOME:
                    mRoutes = Route.sHomeRoutes;
                    break;
                case WORK_TRANSFER:
                    mRoutes = Route.sWorkTransferRoutes;
                    break;
                case HOME_TRANSFER:
                    mRoutes = Route.sHomeTransferRoutes;
                    break;
                default:
                    throw new IllegalStateException("mDirection was not initialized");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_line_choice, container, false);
        ListView lineList = (ListView) v.findViewById(R.id.line_list);

        lineList.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.line_tile, mRoutes));
        lineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment f = ProgressFragment.newInstance(mDirection, mRoutes[position]);
                if (mDirection.isTransfer()) {
                    CommuteRecord.getInstance().setTransferRoute(mRoutes[position]);
                } else {
                    CommuteRecord.getInstance().setRoute(mRoutes[position]);
                    //Only set the direction on the first line choice
                    CommuteRecord.getInstance().setDirection(mDirection);
                }
                getFragmentManager().beginTransaction().replace(R.id.container, f).commit();
            }
        });
        return v;
    }


}

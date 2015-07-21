package com.droolingsheep.commutetimer;

import android.app.Fragment;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by asbransc on 7/20/15.
 */
public class HistoryFragment extends Fragment {

    private ListView mHistoryList;
    private CursorAdapter mAdapter;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        mHistoryList = (ListView) v.findViewById(R.id.history_list);

        CommuteRecordDbHelper historyDb = new CommuteRecordDbHelper(getActivity());
        Cursor cursor = historyDb.getAllHistory();
        Log.i("HistoryFragment", String.valueOf(cursor.getCount()));
        mAdapter = new CursorAdapter(getActivity(), cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.item_history, parent, false);
                populateView(v, cursor);
                return v;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                populateView(view, cursor);
            }

            private void populateView(View v, Cursor c) {
                long leaveTime = c.getLong(c.getColumnIndex(CommuteRecordDbHelper.KEY_LEAVE_TIME));
                int direction = c.getInt(c.getColumnIndex(CommuteRecordDbHelper.KEY_DIRECTION));
                String route1Name = c.getString(c.getColumnIndex(CommuteRecordDbHelper.KEY_ROUTE_1_NAME));
                String route2Name = c.getString(c.getColumnIndex(CommuteRecordDbHelper.KEY_ROUTE_2_NAME));
                TextView dateView = (TextView) v.findViewById(R.id.date);
                TextView dirView = (TextView) v.findViewById(R.id.direction);
                TextView route1NameView = (TextView) v.findViewById(R.id.route_1_name);
                TextView route2NameView = (TextView) v.findViewById(R.id.route_2_name);
                SimpleDateFormat dateOnly = new SimpleDateFormat("c y-MM-dd", Locale.US);
                dateView.setText(dateOnly.format(new Date(leaveTime)));
                switch(direction) {
                    case 0:
                        //to work
                        dirView.setText(R.string.work);
                        break;
                    case 1:
                        //to home
                        dirView.setText(R.string.home);
                        break;
                    default:
                        //other
                        dirView.setText(R.string.other);
                }
                route1NameView.setText(route1Name);
                route2NameView.setText(route2Name);
            }
        };
        mHistoryList.setAdapter(mAdapter);
        return v;
    }
}

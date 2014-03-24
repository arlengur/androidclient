package com.onstar.gobn.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onstar.gobn.android.entity.Gtbt;
import com.onstar.gobn.android.entity.Site;
import com.onstar.gobn.android.entity.Stack;
import com.onstar.gobn.android.parser.XmlProcess;

/**
 * ExpandableList is a class to create ExpandableListView.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */
public class ExpandableList extends Fragment implements OnClickListener {
    /** Reference to an instance of the ExpandableAdapter. */
    private transient ExpandableAdapter esa_;
    /** Variable to store data for the ExpandableListView. */
    private transient List<Stack> stacks_;
    /** Variable to update the state variables in the main Thread. */
    private transient Handler handler_;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        stacks_ = new ArrayList<Stack>();
        handler_ = new Handler();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.exp_fragment, null);

        if (stacks_.isEmpty()) {
            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    handler_.post(loadListData(true, v));
                    stacks_.addAll(getData(v));
                    handler_.post(notifyList());
                    handler_.post(loadListData(false, v));
                }
            });
            t.start();
        }

        esa_ = new ExpandableAdapter(inflater.getContext(), stacks_);
        final ExpandableListView exList_ = (ExpandableListView) v.findViewById(R.id.expServerList);
        exList_.setGroupIndicator(null);
        exList_.setDivider(getActivity().getResources().getDrawable(R.color.white));
        exList_.setDividerHeight(1);
        exList_.setChildDivider(getActivity().getResources().getDrawable(R.color.white));
        exList_.setAdapter(esa_);

        // Button handler
        final Button updateBtn = (Button) v.findViewById(R.id.updateAllBtn);
        updateBtn.setOnClickListener(this);

        return v;
    }

    /**
     * Updates the view states for a single element.
     * @param start - flag of start or end update
     * @param view - view element
     * @return runnable
     */
    private Runnable loadListData(final boolean start, final View view) {
        final Runnable temp = new Runnable() {
            @Override
            public void run() {
                final ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressBarBig);
                if (start) {
                    pb.setVisibility(View.VISIBLE);
                } else {
                    pb.setVisibility(View.INVISIBLE);
                }
            }
        };
        return temp;
    }

    /**
     * Updates the view states for all elements.
     * @param start - flag of start or end update
     * @param view - view element
     * @return runnable
     */
    private Runnable updateListView(final boolean start, final View view) {
        final Runnable temp = new Runnable() {
            @Override
            public void run() {
                final RelativeLayout rl = (RelativeLayout) view.getParent();
                final ProgressBar pb = (ProgressBar) rl.findViewById(R.id.progressBarBig);
                if (start) {
                    view.setClickable(false);
                    pb.setVisibility(View.VISIBLE);
                } else {
                    view.setClickable(true);
                    pb.setVisibility(View.INVISIBLE);
                }
            }
        };
        return temp;
    }

    /**
     * Notify expandable list view.
     * @return runnable
     */
    private Runnable notifyList() {
        final Runnable temp = new Runnable() {
            @Override
            public void run() {
                esa_.notifyDataSetChanged();
            }
        };
        return temp;
    }

    /**
     * Displays a message if there are errors.
     * @param msg - message with errors
     * @return runnable
     */
    private Runnable showMessage(final String msg) {
        final Runnable temp = new Runnable() {
            @Override
            public void run() {
                final DialogFragment dialogError = new DialogError(msg);
                dialogError.show(getFragmentManager(), null);
            }
        };
        return temp;
    }

    @Override
    public void onClick(final View v) {
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                handler_.post(updateListView(true, v));

                stacks_.clear();
                stacks_.addAll(getData(v));

                handler_.post(notifyList());
                handler_.post(updateListView(false, v));
            }
        });
        t.start();
    }

    /**
     * Receives new data from the server.
     * @return updated data from the server
     */
    private List<Stack> getData(View v) {
        final AssetManager assetManager = getActivity().getResources().getAssets();
        final XmlProcess xmlProcess = new XmlProcess(assetManager);
        xmlProcess.execute();

        final List<Stack> newStacks = new ArrayList<Stack>();
        try {
            Gtbt gtbt = xmlProcess.get();
            handler_.post(updateDate(v, gtbt.getTimeStamp()));
            List<Site> sites = gtbt.getSites();
            for(Site site:sites){
                newStacks.addAll(site.getStacks());
            }
        } catch (InterruptedException e) {
            // NOPMD
        } catch (ExecutionException e) {
            // NOPMD
        }

        if (xmlProcess.getError() != null) {
            handler_.post(showMessage(xmlProcess.getError()));
        }
        return newStacks;
    }

    /**
     * Notify expandable list view.
     * @return runnable
     */
    private Runnable updateDate(final View v, final String date) {
        final Runnable temp = new Runnable() {
            @Override
            public void run() {
                final TextView titleDate = (TextView) v.findViewById(R.id.serverTitleDate);
                titleDate.setText("updated: "+date);
            }
        };
        return temp;
    }
}

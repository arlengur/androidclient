package com.onstar.gobn.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.onstar.gobn.android.entity.Cluster;
import com.onstar.gobn.android.entity.Gtbt;
import com.onstar.gobn.android.entity.Server;
import com.onstar.gobn.android.entity.Site;
import com.onstar.gobn.android.entity.Stack;
import com.onstar.gobn.android.parser.XmlProcess;

/**
 * ExpandableAdapter is a class to create an adapter for the class ExpandableListView.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter implements OnClickListener {
    /** Variable to store server status. */
    public static final String SERVER_UP = "UP";
    /** Variable to store server monitor status. */
    public static final String SERVER_NOTMONITORED = "NOTMONITORED";
    /** Variable describes server monitor status. */
    public static final String STACK_NOTMONITORED = "<font color=#354D73> -- Not monitored -- </font>";
    /** Variable to store data for the ExpandableListView. */
    private transient List<Stack> stacks_;
    /** Variable for the LayoutInflater. */
    private transient LayoutInflater lInflater_;
    /** Variable for the Context. */
    private transient Context context_;
    /** Variable to update the state variables in the main Thread. */
    private transient Handler handler_;
    /** Variable to store link of the ExpandableAdapter. */
    private transient ExpandableAdapter adapter_;

    /**
     * Constructor for the ExpandableAdapter.
     * @param context - variable for the Context
     * @param stacks - variable to store data for the ExpandableListView
     */
    public ExpandableAdapter(final Context context, final List<Stack> stacks) {
        super();
        stacks_ = stacks;
        lInflater_ = LayoutInflater.from(context);
        context_ = context;
        adapter_ = this;
        handler_ = new Handler();
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, final View view, final ViewGroup vg) {
        final View tempView;
        if (view == null) {
            tempView = lInflater_.inflate(R.layout.grouprow, vg, false);
        } else {
            tempView = view;
        }

        final RelativeLayout rl = (RelativeLayout) tempView.findViewById(R.id.itemLayout);
        rl.removeView(tempView.findViewById(R.id.linlayout));

        final LinearLayout ll = new LinearLayout(context_);
        ll.setId(R.id.linlayout);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        final RelativeLayout.LayoutParams llParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        llParam.addRule(RelativeLayout.BELOW, R.id.serverName);
        rl.addView(ll, llParam);

        final Stack stack = stacks_.get(groupPosition);
        final StringBuffer serverName = new StringBuffer(stack.getId());

        if ("OK".equals(stack.getStatus())) {
            ((RadioButton) tempView.findViewById(R.id.serverStatus)).setChecked(true);
        } else {
            if (SERVER_NOTMONITORED.equals(stack.getStatus())) {
                serverName.append(STACK_NOTMONITORED);
            }
            ((RadioButton) tempView.findViewById(R.id.serverStatus)).setChecked(false);
        }
        ((TextView) tempView.findViewById(R.id.serverName)).setText(Html.fromHtml(serverName.toString()));

        final List<Cluster> clusters = stack.getClusters();
        for (Cluster cluster : clusters) {
            setGroupRadioView(cluster.getServers(), ll);
        }

        // Button handler
        final Button updateBtn = (Button) tempView.findViewById(R.id.updateBtn);
        updateBtn.setTag(groupPosition);
        updateBtn.setOnClickListener(this);

        return tempView;
    }

    @Override
    public View getChildView(final int groupPosition,
                             final int childPosition,
                             final boolean isLastChild,
                             final View view,
                             final ViewGroup vg) {
        final View tempView;
        if (view == null) {
            tempView = lInflater_.inflate(R.layout.tablelayout, null);
        } else {
            tempView = view;
        }

        final TableLayout tablel = (TableLayout) tempView.findViewById(R.id.tableLayout);
        tablel.removeAllViews();
        tablel.setBackground(context_.getResources().getDrawable(R.color.slateGray));

        final Stack stack = stacks_.get(groupPosition);
        final List<Cluster> clusters = stack.getClusters();

        for (Cluster cluster : clusters) {
            final TableRow row = new TableRow(context_);
            final TextView text = new TextView(context_);
            text.setText(" " + cluster.getType());
            text.setTextColor(context_.getResources().getColor(R.color.white));
            row.addView(text);

            setChlView(cluster.getServers(), row);
            tablel.addView(row);
        }
        return tempView;
    }

    @Override
    public Object getGroup(final int groupPosition) {
        return stacks_.get(groupPosition).getId();
    }

    @Override
    public int getGroupCount() {
        return stacks_.size();
    }

    @Override
    public long getGroupId(final int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(final int groupPosition, final int childPosition) {
        return stacks_.get(groupPosition).getClusters().get(childPosition).getType();
    }

    @Override
    public long getChildId(final int groupPosition, final int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return ((stacks_ == null) || stacks_.isEmpty());
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    /**
     * Sets the data in the group element.
     * @param servers - store data for the group element
     * @param ll - LinearLayout variable
     */
    private void setGroupRadioView(final List<Server> servers, final LinearLayout ll) {
        for (Server server : servers) {
            final RadioButton rb = new RadioButton(context_);
            rb.setLayoutParams(ll.getLayoutParams());
            rb.setClickable(false);
            rb.setFocusable(false);

            if (SERVER_UP.equals(server.getStatus())) {
                rb.setChecked(true);
            } else {
                rb.setChecked(false);
            }
            ll.addView(rb);
        }
    }

    /**
     * Sets the data in the child element.
     * @param servers - store data for the child element
     * @param row - view element
     */
    private void setChlView(final List<Server> servers, final TableRow row) {
        for (Server server : servers) {
            final RadioButton rb = new RadioButton(context_);
            rb.setClickable(false);
            rb.setFocusable(false);

            if (SERVER_UP.equals(server.getStatus())) {
                rb.setChecked(true);
            } else {
                rb.setChecked(false);
            }
            row.addView(rb);
        }
    }

    /**
     * Updates the view states for current element.
     * @param start - flag of start or end update
     * @param view - view element
     * @return runnable
     */
    private Runnable updateListView(final boolean start, final View view) {
        final Runnable temp = new Runnable() {
            @Override
            public void run() {
                final RelativeLayout rl = (RelativeLayout) view.getParent().getParent();
                final ProgressBar pb = (ProgressBar) rl.findViewById(R.id.progressBarNorm);
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
     * @param adapter - variable to store link of the ExpandableAdapter
     * @return runnable
     */
    private Runnable notifyList(final ExpandableAdapter adapter) {
        final Runnable temp = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };
        return temp;
    }

    /**
     * Updates date.
     * @return runnable
     */
    private Runnable updateDate(final String date) {
        final Runnable newDate = new Runnable() {
            @Override
            public void run() {
                View tempView = lInflater_.inflate(R.layout.exp_fragment, null);
                final TextView titleDate = (TextView) tempView.findViewById(R.id.serverTitleDate);
                titleDate.setText("updated: " + date);
            }
        };
        return newDate;
    }

    @Override
    public void onClick(final View v) {
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                handler_.post(updateListView(true, v));

                final Integer position = (Integer) v.getTag();
                final Stack stack = stacks_.get(position);
                final AssetManager assetManager = context_.getResources().getAssets();
                final XmlProcess xmlProcess = new XmlProcess(assetManager, stack.getId());
                xmlProcess.execute();

                List<Stack> newStacks = new ArrayList<Stack>();
                try {
                    final Gtbt gtbt = xmlProcess.get();
                    handler_.post(updateDate(gtbt.getTimeStamp()));
                    for (Site site : gtbt.getSites()) {
                        newStacks.addAll(site.getStacks());
                    }
                } catch (InterruptedException e) {
                    //NOPMD
                } catch (ExecutionException e) {
                    //NOPMD
                }
                // Do not noting if List is empty
                if (!newStacks.isEmpty()) {
                    stacks_.set(position, newStacks.get(0));
                }
                handler_.post(notifyList(adapter_));
                handler_.post(updateListView(false, v));
            }
        });
        t.start();
    }
}

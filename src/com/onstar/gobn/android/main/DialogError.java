package com.onstar.gobn.android.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

/**
 * DialogError is a dialog class.
 *
 * Copyright 2014 OnStar Corporation All Rights Reserved.
 *
 * This software is proprietary to OnStar Corporation and is protected by intellectual property laws and international
 * intellectual property treaties. Your access to this software is governed by the terms of your license agreement with
 * OnStar. Any other use of the software is strictly prohibited.
 */
public class DialogError extends DialogFragment implements OnClickListener {
    /** Variable with error message. */
    private transient String message_;

    /**
     * Constructor for the DialogError.
     * @param message - variable with error message
     */
    public DialogError(final String message) {
        super();
        message_ = message;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity()).setTitle("Error:")
                .setNeutralButton("OK", this).setMessage(message_);
        return adb.create();
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        // NOPMD
    }
}

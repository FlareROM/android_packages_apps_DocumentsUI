/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.documentsui.inspector;

import android.annotation.StringRes;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

import com.android.documentsui.base.DocumentInfo;
import com.android.documentsui.R;
import java.util.function.Consumer;

/**
 * Organizes and Displays the basic details about a file
 */
public class DetailsView extends LinearLayout implements Consumer<DocumentInfo> {

    private final LayoutInflater mInflater;
    private final Map<Integer, TextView> rows = new HashMap();

    public DetailsView(Context context) {
        this(context, null);
    }

    public DetailsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setRow(@StringRes int keyId, String value) {
        if(rows.containsKey(keyId)) {
            rows.get(keyId).setText(value);
        } else {
            View row = mInflater.inflate(R.layout.table_row, null);
            TextView title = (TextView) row.findViewById(R.id.key);
            title.setText(getResources().getString(keyId));
            TextView info = (TextView) row.findViewById(R.id.value);
            info.setText(value);
            addView(row);
            rows.put(keyId, info);
        }
    }

    private String formatSize(long bytes) {
        double kb = bytes/1024.0;
        double mb = kb/1024.0;
        double gb = mb/1024.0;
        double tb = gb/1024.0;
        String docSize;
        if (bytes < 1024) {
            docSize = String.valueOf(Math.round(bytes*100)/100.0) + " B";
        } else if (kb < 1024) {
            docSize = String.valueOf(Math.round(kb*100)/100.0) + " KB";
        } else if (mb < 1024) {
            docSize = String.valueOf(Math.round(mb*100)/100.0) + " MB";
        } else if (gb < 1024) {
            docSize = String.valueOf(Math.round(gb*100)/100.0) + " GB";
        } else {
            docSize = String.valueOf(Math.round(tb*100)/100.0) + " TB";
        }
        return docSize;
    }

    @Override
    public void accept(DocumentInfo info) {
        setRow(R.string.sort_dimension_file_type, info.mimeType);
        setRow(R.string.sort_dimension_size, formatSize(info.size));
        setRow(R.string.sort_dimension_date, String.valueOf(info.lastModified));
    }
}
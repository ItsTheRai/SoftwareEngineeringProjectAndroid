package com.uni.rai.softwareengineeringproject;

import com.example.rai.myapplication.backend.salesInformationApi.model.SalesDataShortCollection;

/**
 * Created by rai on 12/11/15.
 */
public interface OnTaskCompleted {
    void onTaskCompleted(SalesDataShortCollection salesDataShortCollection);
}

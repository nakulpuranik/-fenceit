package com.forksystems.fenceit.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by nakul on 13/8/17.
 */
public interface IResponse {
    void successResponse(int requestType,String response);
    void failureResponse(int requestType,VolleyError error);
}

package com.forksystems.fenceit.services;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.forksystems.fenceit.AppController;
import com.forksystems.fenceit.interfaces.IResponse;
import com.forksystems.fenceit.utils.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nakul on 13/8/17.
 * Should be used for communicating with API or with any other request
 */
public class APICommunication {

    public static final String TAG = APICommunication.class.getSimpleName();
    IResponse mResponseCallback = null;
    Context mContext;

    APICommunication(IResponse resultCallback, Context context) {
        mResponseCallback = resultCallback;
        mContext = context;
    }

    /**
     * This method handles all the json parameters as an input to request
     * Can also accepts post or get request type
     * @param requestType Specify GET/POST request
     * @param urlAction URL to hit
     * @param requestParameters input paramters should be of type jsonObject
     */
    public void makeJsonRequest(final int requestType,String urlAction, JSONObject requestParameters) {
        try {
            final String requestUrl = Constants.BASE_URL + urlAction;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,requestUrl, requestParameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (null != mResponseCallback) {
                                mResponseCallback.successResponse(requestType, response.toString());
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(null != mResponseCallback){
                        mResponseCallback.failureResponse(requestType,error);
                    }
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjectRequest,TAG);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method do not accept json as input parameters
     * @param requestType Specify request type GET/POST
     * @param urlAction API action do not include base URL
     * @param requestParameters input parameters as MAP
     */
    public void makeRequest(final int requestType,String urlAction, final Map requestParameters) {
        try {
            final String requestUrl = Constants.BASE_URL + urlAction;
            StringRequest stringRequest = new StringRequest(requestType,requestUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(null != mResponseCallback){
                        mResponseCallback.successResponse(requestType,response);
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(null != mResponseCallback){
                        mResponseCallback.failureResponse(requestType,error);
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    return requestParameters;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest, TAG);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cancel All request/s
     */
    public void cancelAllRequest(){
        try{
            AppController.getInstance().cancelAllRequest(TAG);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

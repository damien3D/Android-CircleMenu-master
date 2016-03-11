package com.szugyi.circlemenusample;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
	 
    public static final String TAG = AppController.class.getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


 
    private static AppController mInstance;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppController.context = getApplicationContext();
    }
 
    public static synchronized AppController getInstance() {
        return mInstance;
    }
 
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
 
        return mRequestQueue;
    }
 
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,new LruBitmapCache());
        }
        return this.mImageLoader;
    }
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Context getAppContext() {
        return AppController.context;
    }
    
//	public static ImageListener getImageListener(final ImageView view,
//			final int defaultImageResId, final int errorImageResId) {
//		return new ImageListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				if (errorImageResId != 0) {
//					view.setImageResource(errorImageResId);
//				}
//			}
//
//			@Override
//			public void onResponse(ImageContainer response, boolean isImmediate) {
//				if (response.getBitmap() != null) {
//					view.setImageBitmap(response.getBitmap());
//				} else if (defaultImageResId != 0) {
//					view.setImageResource(defaultImageResId);
//				}
//			}
//		};
//	}

}
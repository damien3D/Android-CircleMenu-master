package com.szugyi.circlemenusample;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
//import com.glifit.app.AppController;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DisplayFullGlif extends Activity implements OnClickListener{

	public String mycomment;
	TextView commentbutton;
	ImageView likebutton;
	RelativeLayout listviewer;
	RelativeLayout listlike;
	private static final String url = "http://176.31.253.89/glifit/app/app.php";
	ArrayList<Comments> commentsList;
	CommentAdapter adapter;
	private PullToRefreshListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		setContentView(R.layout.displayfullgliflist);

		final String PREFS_NAME = "GLIF_PREF";

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		final int mUserID = settings.getInt("userID", 0);
//		Log.v("idUser", "userID in displayfullglif is " + Integer.toString(mUserID));

		Bundle bundle = getIntent().getExtras();
//		final String targetID = bundle.getString("targetID");
		final String glifID = bundle.getString("glifID");
		final String message = bundle.getString("message");
		final String username = bundle.getString("username");
		final String creationDate = bundle.getString("creationDate");
		final String commentNumber = bundle.getString("commentNumber");
		final String likeNumber = bundle.getString("likeNumber");
		final String viewNumber = bundle.getString("viewNumber");
		final String photoPath = bundle.getString("photoPath");
		final String profilePic = bundle.getString("profilePic");

//		Log.v("targetID in DisplayFullGlif",targetID);
//		Log.v("glifID in DisplayFullGlif",glifID);
//		Log.v("username in DisplayFullGlif",username);
//		Log.v("creationDate in DisplayFullGlif",creationDate);
//		Log.v("commentNumber in DisplayFullGlif",commentNumber);
//		Log.v("likeNumber in DisplayFullGlif",likeNumber);
//		Log.v("viewNumber in DisplayFullGlif",viewNumber);

		commentsList = new ArrayList<Comments>();

		GetComment(glifID);

//		Log.v("full glif","activity created");

		listView = (PullToRefreshListView)findViewById(R.id.commentListView);

		// Set a listener to be invoked when the list should be refreshed.
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

				new GetMyGlifRefresh().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

//				Toast.makeText(getApplicationContext(), "Load More!", Toast.LENGTH_SHORT).show();
//				new GetMyGlifLoadMore().execute();
			}

		});

		adapter = new CommentAdapter(getApplicationContext(), R.layout.row_comments, commentsList);
		listView.setAdapter(adapter);

//		View header = getLayoutInflater().inflate(R.layout.displayfullglifheader, null);
		View header = getLayoutInflater().inflate(R.layout.displayfullglifheader, null, false);
		listView.getRefreshableView().addHeaderView(header);

		TextView messagedisplay= (TextView) header.findViewById(R.id.glifMessage);
		messagedisplay.setText(message);
		Linkify.addLinks(messagedisplay, Linkify.ALL);

		TextView datedisplay = (TextView) header.findViewById(R.id.creationDate);
		datedisplay.setText(creationDate);

		TextView _username = (TextView) header.findViewById(R.id.user);
		_username.setText(username);

		ImageLoader imageLoader = AppController.getInstance().getImageLoader();

		NetworkImageView photoProfile = (NetworkImageView) header.findViewById(R.id.profilePic);
		photoProfile.setImageUrl(profilePic, imageLoader);

		NetworkImageView imagedisplay = (NetworkImageView) header.findViewById(R.id.background);
		imagedisplay.setImageUrl(photoPath, imageLoader);
		imagedisplay.setRotation(90);

		TextView commentNB = (TextView) header.findViewById(R.id.commentNb);
		commentNB.setText(commentNumber);

		TextView likeNB = (TextView) header.findViewById(R.id.likeNb);
		likeNB.setText(likeNumber);

		TextView viewNB = (TextView) header.findViewById(R.id.viewNb);
		viewNB.setText(viewNumber);

		View footer = getLayoutInflater().inflate(R.layout.displayfullgliffooter, null);
		listView.getRefreshableView().addFooterView(footer);

		final EditText editCommentMessage = (EditText) footer.findViewById(R.id.mycomment);
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		likebutton = (ImageView) header.findViewById(R.id.likebutton);
		likebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				likebutton.setImageResource(R.drawable.ic_action_favorite_color);

				int mUserID = 0;
				final String PREFS_NAME = "GLIF_PREF";
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				mUserID = settings.getInt("userID", 0);

				// add the parameters and post a like
				final HashMap<String, String> params = new HashMap<String, String>();
				params.put("action", "postLike");
				params.put("idUser", Integer.toString(mUserID));
				params.put("idGlif",glifID);

				JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
//						try {
//	                        Log.v("Like:%n %s", response.toString(6));
//
//	                    } catch (JSONException e) {
//	                        e.printStackTrace();
//	                    }
//
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Error: ", "error");
					}
				});


				AppController.getInstance().addToRequestQueue(req);
				AppController.getInstance().getRequestQueue().getCache().remove(url);

			}

		});

		listviewer = (RelativeLayout) header.findViewById(R.id.views);
		listviewer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//Intent intent = new Intent(getApplicationContext(), ViewListActivity.class);

				//intent.putExtra("idGlif",String.valueOf(glifID));

				//startActivity(intent);

			}

		});

		ImageView report = (ImageView) header.findViewById(R.id.report);
		report.setOnClickListener(this);

		report.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(DisplayFullGlif.this);

				builder.setPositiveButton(R.string.confirm_report,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						CharSequence text = "Thanks for reporting";
						int duration = Toast.LENGTH_SHORT;

						Toast toast = Toast.makeText(DisplayFullGlif.this, text, duration);
						toast.show();
						// add the parameters and post a like
						final HashMap<String, String> params = new HashMap<String, String>();
						params.put("action", "reportglif");
						params.put("idGlif",String.valueOf(glifID));

						JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
//										try {
//					                        Log.v("report:%n %s", response.toString(6));
//
//					                    } catch (JSONException e) {
//					                        e.printStackTrace();
//					                    }

							}


						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("Error: ", error.getMessage());
							}
						});
						AppController.getInstance().addToRequestQueue(req);

						AppController.getInstance().getRequestQueue().getCache().remove(url);
					}

				});

				builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {

					}
				});

				builder.setMessage(R.string.report);
				builder.show();
			}
		});

		listlike = (RelativeLayout) header.findViewById(R.id.like);
		listlike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//Intent intent = new Intent(getApplicationContext(), LikeListActivity.class);

				//intent.putExtra("idGlif",String.valueOf(glifID));

				//startActivity(intent);

			}

		});

		commentbutton = (TextView) footer.findViewById(R.id.commentbutton);
		commentbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mycomment = editCommentMessage.getText().toString();

				PostComment(mycomment, glifID, mUserID);

				imm.hideSoftInputFromWindow(editCommentMessage.getWindowToken(), 0);

//				commentsList = new ArrayList<Comments>();

				new PostMyComment().execute();
//				AppController.getInstance().getRequestQueue().getCache().remove(url);

			}

		});

	}

	private class GetMyGlifRefresh extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(500);
				commentsList = new ArrayList<Comments>();

				Bundle bundle = getIntent().getExtras();
				String glifID = bundle.getString("glifID");

				GetComment(glifID);
			} catch (InterruptedException e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
//			mListItems.addFirst("Added after refresh man");
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
//			Log.v("profile","refresh complete");
			listView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	private class PostMyComment extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(500);
				commentsList = new ArrayList<Comments>();

				Bundle bundle = getIntent().getExtras();
				String glifID = bundle.getString("glifID");

				GetComment(glifID);
			} catch (InterruptedException e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
//			mListItems.addFirst("Added after refresh man");
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
//			Log.v("profile","refresh complete");
			listView.onRefreshComplete();

			super.onPostExecute(result);

			recreate();
		}
	}

	private void GetComment(String glifID) {


		final HashMap<String, String> _params = new HashMap<String, String>();
		_params.put("action", "getcomments");
		_params.put("idGlif", glifID);

		JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(_params), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

//				Log.v("Respo on display comment direct:%n %s", response.toString());

				try {
//					Log.v("Respo on display comment:%n %s", response.toString(6));

					// Parsing JSON to array²
					JSONArray theCommentList = response.getJSONArray("comments");
//					Log.v("test my comment ",Integer.toString(theCommentList.length()));

					for (int i = theCommentList.length()-1; i >= 0; i--) {

						JSONObject obj = theCommentList.getJSONObject(i);
						Comments postComment = new Comments();

						String musername = obj.getString("username");
						String mcomment = obj.getString("message");

//							Log.v("username",musername);
//							Log.v("message",mcomment);

						postComment.setMessage(mcomment);
						postComment.setAuthor(musername);

						// adding glif to glif array
						commentsList.add(postComment);

					}

					// create an ArrayAdaptar from the String Array
					adapter = new CommentAdapter(getBaseContext(), R.layout.row_comments, commentsList);
//						ListView listView = (ListView) findViewById(R.id.commentListView);
					// Assign adapter to ListView
					listView.setAdapter(adapter);

					// Do not Forget That Bad Modafuca !!!
					adapter.notifyDataSetChanged();


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),getResources().getString(R.string.refresh),Toast.LENGTH_SHORT).show();

					// create an ArrayAdaptar from the String Array
//					adapter = new CommentAdapter(getApplicationContext(), R.layout.row_comments, commentsList);
//					ListView listView = (ListView) findViewById(R.id.commentListView);
					// Assign adapter to ListView
					adapter = new CommentAdapter(getBaseContext(), R.layout.row_comments, commentsList);
					listView.setAdapter(adapter);

					// Do not Forget That Bad Modafuca !!!
					adapter.notifyDataSetChanged();
				}

			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
				Toast.makeText(getApplicationContext(),"sorry comments could not be loaded",Toast.LENGTH_LONG).show();

			}
		});

		AppController.getInstance().addToRequestQueue(req);
		AppController.getInstance().getRequestQueue().getCache().remove(url);

	}

	private void PostComment(String comment, String glifID, int mUserID) {

		// Sending volley Request req and parsing the data HashMap<String,
		// <String>>
		final HashMap<String, String> mparams = new HashMap<String, String>();
		mparams.put("action", "postcomment");
		mparams.put("message", comment);
		mparams.put("idUser", Integer.toString(mUserID));
		mparams.put("idGlif", glifID);

		JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(mparams), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
//				try {
//					Log.v("Respo on post comment :%n %s", response.toString(6));
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}

			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
			}
		});

//		GetComment(glifID);

		AppController.getInstance().addToRequestQueue(req);
		AppController.getInstance().getRequestQueue().getCache().remove(url);

//		GetComment(glifID);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
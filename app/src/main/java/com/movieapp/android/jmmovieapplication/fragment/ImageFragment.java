package com.movieapp.android.jmmovieapplication.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.movieapp.android.jmmovieapplication.R;
import com.movieapp.android.jmmovieapplication.util.JSONUtil;
import com.movieapp.android.jmmovieapplication.util.Record;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    View mView;
    String sPoster;
    String url_image;
    String Title;
    String Genre;
    String Released;
    String Plot;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvPlot;

    private OnFragmentInteractionListener mListener;

    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        mView =view;
        tvTitle     = (TextView) view.findViewById(R.id.title);
        tvContent   = (TextView) view.findViewById(R.id.content);
        tvPlot     = (TextView) view.findViewById(R.id.plot);

        if( savedInstanceState == null){

        } else{
            url_image = savedInstanceState.getString("url_image");
            Title = savedInstanceState.getString("Title");
            Genre = savedInstanceState.getString("Genre");
            Released = savedInstanceState.getString("Released");
            Plot = savedInstanceState.getString("Plot");
            new DownloadImageTask((ImageView) mView.findViewById(R.id.imageView_Poster)).execute(url_image);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("test", "onSaveInstanceState url_image:  " + url_image);
        outState.putString("url_image", url_image);
        outState.putString("Title",Title);
        outState.putString("Genre",Genre);
        outState.putString("Released",Released);
        outState.putString("Plot",Plot);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void changeData(String index)
    {
        /*Toast.makeText(getActivity(),"index:" + index,Toast.LENGTH_SHORT).show();*/
        url_image = index;
        new UpdateMovieInfoTask().execute(index);
    }

    private class UpdateMovieInfoTask extends AsyncTask<String,Void,Record> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Searching...");
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.show();
        }

        @Override
        protected Record doInBackground(String... params) {
            String urlString;
            try{
                urlString  =  "http://www.omdbapi.com/?i=XXX&plot=short&r=json";
                urlString = urlString.replace("XXX", params[0]);
                Record json = JSONUtil.jsonHttpRequestGET(urlString);
                return json;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Record record) {
            if(record == null){
                return;
            }
            Log.e("result", "record ==>"+ record.toString());
            if(record.containsKey("Poster")) {
                url_image   = record.get("Poster").toString();
                Title       = record.get("Title").toString();
                Genre       = record.get("Genre").toString();
                Released    = record.get("Released").toString();
                Plot        = record.get("Plot").toString();

                Log.d("result", "url_image ==>" + url_image);
            } else {
                url_image = null;
            }
            if( url_image != null) {
                progressDialog.dismiss();
                new DownloadImageTask((ImageView) mView.findViewById(R.id.imageView_Poster)).execute(url_image);
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bitmapImage;
        ProgressDialog pd;

        public DownloadImageTask(ImageView bmImage) {
            this.bitmapImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Poster downloading...");
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            String wIcon = "";
            String wIcon_diff = "";
            Drawable drawable = null;

            Log.d("result", "urldisplay :" + urldisplay);
            if(urldisplay == null){
                return mIcon11;
            }

            if(urldisplay.equals("N/A")){
                Log.d("result", "urldisplay == N/A" );
                return mIcon11;
            } else {
                try {
                    mIcon11 = BitmapFactory.decodeStream(new URL(urldisplay).openStream());
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if( bitmap == null){
                Toast.makeText(getActivity(),"Poster image is not set.",Toast.LENGTH_SHORT).show();
            } else {
                bitmapImage.setImageBitmap(bitmap);
                tvTitle.setText(Title);
                tvContent.setText("("+ Genre + ", "+ Released +")");
                tvPlot.setText(Plot);
            }

            try {
                if ((pd != null) && pd.isShowing()) {
                    pd.dismiss();
                }
            } catch (final IllegalArgumentException e) {
            } catch (final Exception e) {
            } finally {
                pd = null;
            }

        }
    }
}

package com.movieapp.android.jmmovieapplication.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.movieapp.android.jmmovieapplication.R;

import com.movieapp.android.jmmovieapplication.util.Movie;
import com.movieapp.android.jmmovieapplication.util.JSONUtil;
import com.movieapp.android.jmmovieapplication.util.Record;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class SearchNameFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String url_image;

    View mView;
    SearchView searchView;
    Typeface type;
    ListView searchResults;
    String founded = "N";
    ArrayList<Movie.MovieItem> temp  = new ArrayList<>();
    Communicator communicator;
    String edit_text;

    private AbsListView mSearchResultListView;
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static SearchNameFragment newInstance(String param1, String param2) {
        SearchNameFragment fragment = new SearchNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchNameFragment() {
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
        mView = inflater.inflate(R.layout.fragment_item, container, false);
        searchView = (SearchView)mView.findViewById(R.id.searchView1);
        searchView.setQueryHint("Start typing to search...");
        mSearchResultListView = (AbsListView) mView.findViewById(R.id.list);

        if(savedInstanceState == null){

        } else{
            edit_text = savedInstanceState.getString("edit_text");
            Log.d("test", " savedInstanceState edit_text:" + edit_text);

//            searchView.setQuery(edit_text, false);
//            searchView.clearFocus();
        }

        // Set OnItemClickListener so we can be notified on item clicks
        mSearchResultListView.setOnItemClickListener(this);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

        }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 3) {
                    mSearchResultListView.setVisibility(mView.VISIBLE);
                    edit_text = newText;
                    Log.d("test", " edit_text:" + edit_text);
                    new SearchMovieTittleTask().execute(newText);

                } else {
                    mSearchResultListView.setVisibility(mView.INVISIBLE);
                }
                return false;
            }
        });
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(edit_text != null) {
            outState.putString("edit_text", edit_text);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), temp.get(position).toString() + " is selected  ",Toast.LENGTH_SHORT).show();
        communicator.response(temp.get(position).getId());
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mSearchResultListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void setCommunicator(Communicator communicator)
    {
        this.communicator = communicator;
    }
    public interface Communicator
    {
        public void response(String index);
    }

    private class SearchMovieTittleTask extends AsyncTask<String,Void,String>
    {
        JSONArray movieTitleArray;
        String urlString = new String();
        String searchText;
        /*ProgressDialog progressDialog;*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*movieTitleList = new JSONArray();*/
     /*       progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Searching...");
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.show();*/
        }
        @Override
        protected String doInBackground(String... params) {
            urlString  =  "http://www.omdbapi.com/?s=" + params[0]+"&y=&plot=short&r=json" +params[0] ;
            temp.clear();

            try{
                JSONObject jsonObj = JSONUtil.jsonHttpRequest(urlString);
                if(jsonObj != null) {
                    movieTitleArray = jsonObj.getJSONArray("Search");
                    for (int i = 0; i < movieTitleArray.length(); i++) {
                        JSONObject obj = movieTitleArray.getJSONObject(i);
                        Log.d("resutlt", " obj == " + obj.getString("Title"));
                        temp.add(new Movie.MovieItem(obj.getString("imdbID"), obj.getString("Title")));
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return urlString;//null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(getActivity() != null) {
                mAdapter = new ArrayAdapter<Movie.MovieItem>(getActivity(),
                        R.layout.framgment_item_list_text, android.R.id.text1, temp);
                ((AdapterView<ListAdapter>) mSearchResultListView).setAdapter(mAdapter);

            }
        }
    }


}

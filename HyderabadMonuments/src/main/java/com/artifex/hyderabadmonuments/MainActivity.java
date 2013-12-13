package com.artifex.hyderabadmonuments;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        GridView mGridView;
        private class Monument{
            String name;
            int drawable;

            Monument(String name, int drawable){
                this.name = name;
                this.drawable = drawable;
            }
        }

        List<Monument> mMonuments;
        public PlaceholderFragment() {
            mMonuments = new ArrayList<Monument>();

            Monument monument = new Monument("Charminar", R.drawable.charminar);
            mMonuments.add(monument);
            monument = new Monument("Golconda",R.drawable.golconda);
            mMonuments.add(monument);
            monument = new Monument("Taramati Bardari", R.drawable.taramati);
            mMonuments.add(monument);
            monument = new Monument("Birla Mandir", R.drawable.birlamandir);
            mMonuments.add(monument);
            monument = new Monument("Tank Bund", R.drawable.tankbund);
            mMonuments.add(monument);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);

            mGridView = (GridView) view.findViewById(R.id.gridView);
            mGridView.setAdapter(new GridItemsAdapter());
        }

        public class GridItemsAdapter extends BaseAdapter{

            @Override
            public int getCount(){
                return mMonuments.size();
            }

            @Override
            public Object getItem(int i){
                return mMonuments.get(i);
            }

            @Override
            public long getItemId(int i){
                return i;
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                view = inflater.inflate(R.layout.grid_item, null);

                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                TextView textView = (TextView) view.findViewById(R.id.textView);

                Monument monument = (Monument) getItem(position);
                imageView.setImageResource(monument.drawable);
                textView.setText(monument.name);

                return view;
            }
        }
    }

}

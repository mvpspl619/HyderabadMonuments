package com.artifex.hyderabadmonuments;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            getFragmentManager().beginTransaction()
                    .add(R.id.container,Fragment.instantiate(this, PlaceholderFragment.class.getName(),bundle) )
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second, menu);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_second, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            super.onViewCreated(view,savedInstanceState);

            Monument monument = getArguments().getParcelable("monument");

            TextView monName = (TextView) view.findViewById(R.id.monumentName);
            ImageView monImage = (ImageView) view.findViewById(R.id.monumentImage);
            TextView monDesc = (TextView) view.findViewById(R.id.monumentDescription);

            monName.setText(monument.getName());
            monImage.setImageResource(monument.getDrawable());
            monDesc.setText(monument.getDescription());

            Button viewOnMap = (Button) view.findViewById(R.id.viewOnMapButton);
            viewOnMap.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("monument", getArguments().getParcelable("monument"));
                    startActivity(intent);
                }
            });
        }
    }

}

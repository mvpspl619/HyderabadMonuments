package com.artifex.hyderabadmonuments;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by Home on 14/12/13.
 */
public class MonumentList
{
    public ArrayList<Monument> items;
    private Context mContext;

    MonumentList(Context context)
    {
        this.mContext = context;
        this.items = items;
    }

    public ArrayList<Monument> LoadData()
    {
        ArrayList<Monument> mMonuments = new ArrayList<Monument>();
        mMonuments.add(new Monument("Charminar", R.drawable.charminar,mContext.getString(R.string.charminarDescription), 17.3616723, 78.4745746));
        mMonuments.add(new Monument("Golconda",R.drawable.golconda, mContext.getString(R.string.golcondaDescription), 17.3835808, 78.40146979));
        mMonuments.add(new Monument("Taramati Bardari", R.drawable.taramati, mContext.getString(R.string.taramatiDescription), 17.3762018, 78.3781787));
        mMonuments.add(new Monument("Birla Mandir", R.drawable.birlamandir, mContext.getString(R.string.birlamandirDescription), 17.4062674, 78.46908689));
        mMonuments.add(new Monument("Tank Bund", R.drawable.tankbund, mContext.getString(R.string.tankbundDescription), 17.4239299, 78.4738385));

        return mMonuments;
    }
}


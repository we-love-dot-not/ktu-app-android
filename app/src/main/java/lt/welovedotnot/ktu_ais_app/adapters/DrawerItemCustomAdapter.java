package lt.welovedotnot.ktu_ais_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import lt.welovedotnot.ktu_ais_app.R;

/**
 * Created by Mindaugas on 5/1/2017.
 */

public class DrawerItemCustomAdapter extends BaseAdapter {
    Context mContext;
    int layoutResourceId;
    String titles[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, String[] titles) {
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView textViewName = (TextView) listItem.findViewById(R.id.drawerItemText);
        textViewName.setText(titles[position]);

        return listItem;
    }
}

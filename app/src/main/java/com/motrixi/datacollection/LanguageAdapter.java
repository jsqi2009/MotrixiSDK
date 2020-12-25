package com.motrixi.datacollection;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motrixi.datacollection.network.models.LanguageInfo;
import com.motrixi.datacollection.utils.DisplayUtil;

import java.util.ArrayList;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * author : Jason
 * date   : 2020/12/24 11:03 AM
 * desc   :
 */
public class LanguageAdapter extends ArrayAdapter<LanguageInfo> {

    private ArrayList<LanguageInfo> list;
    private Context mContext;

    public LanguageAdapter(Context context, int resource, ArrayList<LanguageInfo> list) {
        super(context, resource, list);
        this.mContext = context;
        this.list = list;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LanguageInfo item = list.get(position);
        View view;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            view = new LinearLayout(mContext);
            LinearLayout.LayoutParams viewParams = new  LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            viewParams.setLayoutDirection(VERTICAL);
            view.setLayoutParams(viewParams);


            TextView tvName =  new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    DisplayUtil.dp2px(getContext(), 50)
            );
            params.setLayoutDirection(VERTICAL);
            params.leftMargin = DisplayUtil.dp2px(mContext, 20);
            tvName.setTextSize(22);
            tvName.setGravity(Gravity.CENTER_VERTICAL);
            tvName.setLayoutParams(params);
            tvName.setTextColor(Color.BLACK);

            View line =  new View(mContext);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    DisplayUtil.dp2px(getContext(), 1)
            );
            lineParams.setLayoutDirection(VERTICAL);
            line.setLayoutParams(lineParams);
            line.setBackgroundColor(Color.rgb(0, 150, 182));

            holder.nameTv = tvName;
            //holder.lineView = line;

            ((ViewGroup) view).addView(holder.nameTv);
            //((ViewGroup) view).addView(holder.lineView);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.nameTv.setText(item.language);

        return view;
    }

    class ViewHolder {
        public TextView nameTv;
        public View lineView;
    }
}


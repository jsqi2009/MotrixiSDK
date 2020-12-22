package com.motrixi.datacollection.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.motrixi.datacollection.network.models.LanguageInfo;
import com.motrixi.datacollection.utils.DisplayUtil;
import com.motrixi.datacollection.utils.LanguageUtil;

import java.util.ArrayList;

/**
 * author : Jason
 * date   : 2020/12/18 2:39 PM
 * desc   :
 */
public class LanguageAdapter extends ArrayAdapter<LanguageInfo> {

    private ArrayList<LanguageInfo> list;

    public LanguageAdapter(Context context, int resource, ArrayList<LanguageInfo> list) {
        super(context, resource, list);
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LanguageInfo item = list.get(position);
        View view;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            view = new LinearLayout(getContext());

            TextView tvName =  new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    DisplayUtil.INSTANCE.dp2px(getContext(), 50)
            );
            params.leftMargin = DisplayUtil.INSTANCE.dp2px(getContext(), 20);
            tvName.setTextSize(22);
            tvName.setGravity(Gravity.CENTER_VERTICAL);
            tvName.setLayoutParams(params);

            holder.nameTv = tvName;
            ((ViewGroup) view).addView(holder.nameTv);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.nameTv.setText(item.getLanguage());

        return view;
    }

    class ViewHolder {
        public TextView nameTv;
    }
}

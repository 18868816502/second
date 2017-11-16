package com.beihui.market.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;

public class MoneyFilterPopup extends PopupWindow {

    private View shadowView;
    private TextView tv;
    private ImageView iv;

    private MoneySelectionListener listener;

    public MoneyFilterPopup(final Activity context, View shadowView, TextView tv, ImageView iv, final String[] tags, int selectedIndex) {
        super(context);
        this.shadowView = shadowView;
        this.tv = tv;
        this.iv = iv;

        shadowView.setVisibility(View.VISIBLE);
        shadowView.invalidate();

        View view = LayoutInflater.from(context)
                .inflate(R.layout.popup_window_money_selection, null);
        final ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return tags.length;
            }

            @Override
            public Object getItem(int position) {
                return tags[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loan_common_selection, parent, false);
                }
                ((TextView) convertView).setText(tags[position]);
                return convertView;
            }
        });
        listView.setItemChecked(selectedIndex, true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
                if (listener != null) {
                    listener.onMoneySelected(position);
                }
                dismiss();
            }
        });

        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv.setTextColor(Color.parseColor("#5591FF"));
        iv.setImageResource(R.mipmap.daosanjiao_blue);
        rotateArrow(0, 180, iv);

    }


    @Override
    public void dismiss() {
        super.dismiss();
        shadowView.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#424251"));
        iv.setImageResource(R.mipmap.daosanjiao);
        rotateArrow(180, 0, iv);
    }

    public void setShareItemListener(MoneySelectionListener listener) {
        this.listener = listener;
    }

    private void rotateArrow(float fromDegrees, float toDegrees, ImageView imageView) {
        RotateAnimation mRotateAnimation =
                new RotateAnimation(fromDegrees, toDegrees,
                        (int) (imageView.getMeasuredWidth() / 2.0),
                        (int) (imageView.getMeasuredHeight() / 2.0));
        mRotateAnimation.setDuration(150);
        mRotateAnimation.setFillAfter(true);
        imageView.startAnimation(mRotateAnimation);
    }

    public interface MoneySelectionListener {
        void onMoneySelected(int selectIndex);
    }

}

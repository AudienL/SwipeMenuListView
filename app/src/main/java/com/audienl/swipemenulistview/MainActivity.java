package com.audienl.swipemenulistview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.audienl.swipemenulistviewcore.SwipeMenu;
import com.audienl.swipemenulistviewcore.SwipeMenuCreator;
import com.audienl.swipemenulistviewcore.SwipeMenuItem;
import com.audienl.swipemenulistviewcore.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private SwipeMenuListView mSwipeMenuListView;
    private MyAdapter mAdapter;
    private List<String> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipe_menu_list_view);

        mSwipeMenuListView.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case MyAdapter.TYPE_NON_SWIPE:
                        // 不能滑动
                        break;
                    case MyAdapter.TYPE_SWIPE:
                        SwipeMenuItem item = new SwipeMenuItem(context);
                        item.setBackground(android.R.color.holo_red_dark);
                        item.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, getResources().getDisplayMetrics()));
                        item.setTitle("删除");
                        item.setTitleSize(14);
                        item.setTitleColor(Color.WHITE);
                        menu.addMenuItem(item);
                        break;
                }
            }
        });

        for (int i = 0; i < 30; i++) {
            mItems.add("AudienL: " + i);
        }

        mAdapter = new MyAdapter();
        mSwipeMenuListView.setAdapter(mAdapter);

        // 点击选项中的删除按钮
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                // false: close | true: not close
                return false;
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        public static final int TYPE_SWIPE = 0;
        public static final int TYPE_NON_SWIPE = 1;

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // 最后一项不滑动
            return position == mItems.size() - 1 ? TYPE_NON_SWIPE : TYPE_SWIPE;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();

            final String item = mItems.get(position);

            holder.tv.setText(item);

            /** 注意：这里只能对某个子元素设置click事件，不能对整个item设置onItemClickListener */
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public String getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    static class ViewHolder {
        public TextView tv;

        public ViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }
}

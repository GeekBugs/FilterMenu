package com.f1reking.filtermenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.f1reking.filtermenu.adapter.CommonAdapter;
import com.f1reking.filtermenu.adapter.ViewHolder;
import com.f1reking.filtermenu.model.Menu;
import com.f1reking.library.FilterMenu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.filterMenu) FilterMenu filterMenu;

    private ListView listCategoryLeft;
    private ListView listCategoryRight;
    private ListView listOrderBy;
    private ListView listFilter;
    private List<View> popupViews;
    private ListView listContent;
    private String headers[] = { "分类", "排序", "筛选" };

    private List<Menu> menuList;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_two_listview, null);
        listCategoryLeft = (ListView) view.findViewById(R.id.lv_left_list);
        listCategoryRight = (ListView) view.findViewById(R.id.lv_right_list);

        listOrderBy = new ListView(this);
        listFilter = new ListView(this);

        popupViews = new ArrayList<>();
        popupViews.add(view);
        popupViews.add(listOrderBy);
        popupViews.add(listFilter);

        listContent = new ListView(this);
        listContent.setDividerHeight(1);

        filterMenu.setFilterMenu(Arrays.asList(headers), popupViews, listContent);
    }

    private void initData() {
        menuList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            menuList.add(new Menu("烧鸭" + i));
        }

        listContent.setAdapter(new CommonAdapter<Menu>(this, menuList, R.layout.item_content_list) {
            @Override public void convert(ViewHolder holder, Menu menu) {
                holder.setText(R.id.tv_name, menu.getName());
            }
        });
    }
}

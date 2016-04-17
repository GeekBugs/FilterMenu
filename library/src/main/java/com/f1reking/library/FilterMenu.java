package com.f1reking.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Created by F1ReKing on 2016/3/31.
 */
public class FilterMenu extends LinearLayout {

    private LinearLayout tabMenuView;//顶部菜单布局
    private FrameLayout containerView;//底部容器，包含popupMenuViews，maskView
    private FrameLayout popupMenuViews;//弹出菜单父布局
    private View maskView;//遮罩半透明View，点击可关闭FilterMenu
    private int current_tab_position = -1;//tabMenuView里面选中的tab位置，-1表示未选中
    private int dividerColor = 0xffcccccc;//分割线颜色
    private int textSelectedColor = 0xff890c85;//tab选中颜
    private int textUnselectedColor = 0xff111111;//tab未选中颜色
    private int maskColor = 0x88888888;//遮罩颜色
    private int menuTextSize = 14;//tab字体大小
    private int menuSelectedIcon;//tab选中图标
    private int menuUnselectedIcon;//tab未选中图标
    private int menuBackgroundColor = 0xffffffff;
    private int underlineColor = 0xffcccccc;
    private Context context;

    public FilterMenu(Context context) {
        super(context, null);
        this.context = context;
    }

    public FilterMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public FilterMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(VERTICAL);

        //add Custom Attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FilterMenu);
        underlineColor = a.getColor(R.styleable.FilterMenu_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.FilterMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.FilterMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.FilterMenu_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.FilterMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.FilterMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.FilterMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.FilterMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.FilterMenu_menuUnselectedIcon, menuUnselectedIcon);
        a.recycle();

        //init tabMenuView
        tabMenuView = new LinearLayout(context);
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //add underLine
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(0.5f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        // init containerView
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(
            new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }

    /**
     * init FilterMenu
     */
    public void setFilterMenu(List<String> tabTexts, List<View> popupViews, View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(
            new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i)
                .setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }
    }

    private void addTab(List<String> tabTexts, int i) {
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tab.setTextColor(textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(menuUnselectedIcon),
            null);
        tab.setText(tabTexts.get(i));
        tab.setPadding(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
        tab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);
        //add divider
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 10, 0, 10);
            view.setLayoutParams(lp);
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * change the text of tab
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * Close FilterMenu
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null,
                null, getResources().getDrawable(menuUnselectedIcon), null);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.filter_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.filter_mask_out));
            current_tab_position = -1;
        }
    }

    /**
     * FilterMenu is the visible
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * switch FilterMenu
     */
    private void switchMenu(View target) {
        System.out.println(current_tab_position);
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.filter_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.filter_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuSelectedIcon), null);
                }
            } else {
                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    /**
     * Get the width of the screen
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}

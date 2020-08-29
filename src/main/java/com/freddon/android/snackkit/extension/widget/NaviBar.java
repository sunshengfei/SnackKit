package com.freddon.android.snackkit.extension.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.ResultReceiver;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freddon.android.snackkit.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by fred 2016-05-27
 * <p/>
 * 标题工具条-带搜索扩展
 * 下滑线开头的属性或方法代表NaviBar功能的强依赖 修改应谨慎
 *
 * @author fred
 */
public class NaviBar extends LinearLayout {

    /**
     * 正常模式  【 返回 =》大标题 《= 图标 】
     */
    public final static byte MODE_TITLE_STYLE_NORMAL = 1 << 0;

    /**
     * 复合模式  【 返回 =》大标题/小标题 《= 图标 】
     */
    public final static byte MODE_TITLE_STYLE_COMPLEX = 1 << 1;

    /**
     * 搜索框模式 【 返回 =》[@optional复合筛选框 +] 搜索 《= 图标 】
     */
    public final static byte MODE_TITLE_STYLE_SEARCH = 1 << 1 | 1;
    public final static byte MODE_TITLE_STYLE_SEARCH_WITHOUT_FILTER = 1 << 2;
    public static final int MENU_BUTTON_TEXT = 0;
    public static final int MENU_BUTTON_ICON1ST = 1;
    public static final int MENU_BUTTON_ICON2ND = 2;
    public static final int MENU_BUTTON_ICON3RD = 3;
    private ViewGroup _rootView;
    private TextView _backViewMenuText;
    private TextView _menuIcon1stBadge;
    private TextView _menuIcon2ndBadge;
    private TextView _menuIcon3rdBadge;
    private View _menuIcon1stParent;
    private View _menuIcon2ndParent;
    private View _menuIcon3rdParent;
    private ViewGroup _backView;
    private ImageView _backViewIcon;
    private TextView _title;
    private View _complexView;
    private TextView _superTitle;
    private TextView _subTitle;
    private View _searchView;
    private TextView _searchFilter;
    private ClearEditText _searchQueryText;
    private View _menuContainer;
    private TextView _menuText;
    private ImageView _menuIcon1st;
    private ImageView _menuIcon2nd;
    private ImageView _menuIcon3rd;

    OnNaviBarEventListener onNaviBarEventListener;
    OnQueryTextListener mOnQueryChangeListener;
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
    /**
     * 当前选择的模式
     */
    private byte mode = MODE_TITLE_STYLE_SEARCH_WITHOUT_FILTER;

    public CharSequence getmUserQuery() {
        return mUserQuery == null ? "" : mUserQuery;
    }

    private CharSequence mUserQuery;//键入的查询
    private String mOldQueryText;

    public NaviBar(Context context) {
        this(context, null);
    }

    public NaviBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.navi_bar, this);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NaviBar);
        _rootView = (ViewGroup) findViewById(R.id.layout_navibar);
        //获取返回
        _backView = (ViewGroup) findViewById(R.id.layout_search_titleBar_back);
        _backViewIcon = findViewById(R.id.iv_search_titleBar_back);
        _backViewMenuText = (TextView) findViewById(R.id.tv_search_titleBar_LeftMenu);

        //获取主View
        //模式 MODE_TITLE_STYLE_NORMAL 【wait】
        //只有大标题
        _title = (TextView) findViewById(R.id.tv_search_titleBar_title);

        //模式 MODE_TITLE_STYLE_COMPLEX 【の】
        //大标题 小标题
        _complexView = findViewById(R.id.layout_search_titleBar_complex);
        _superTitle = (TextView) findViewById(R.id.tv_search_titleBar_complex_title);
        _subTitle = (TextView) findViewById(R.id.tv_search_titleBar_complex_subtitle);


        //模式 MODE_TITLE_STYLE_SEARCH / MODE_TITLE_STYLE_SEARCH_WITHOUT_FILTER 【の】
        //[@optional复合筛选框 +] 搜索
        _searchView = findViewById(R.id.layout_search_titleBar_textBox);
        _searchFilter = (TextView) findViewById(R.id.tv_search_titleBar_textBox_filter);
        _searchQueryText = (ClearEditText) findViewById(R.id.et_search_titleBar_textBox);

        //菜单

        _menuContainer = findViewById(R.id.layout_search_titleBar_menu_viewGroup);
        _menuText = (TextView) findViewById(R.id.tv_search_titleBar_menu);
        _menuIcon1stBadge = (TextView) findViewById(R.id.tv_search_titleBar_menu_1st_badge);
        _menuIcon2ndBadge = (TextView) findViewById(R.id.tv_search_titleBar_menu_2nd_badge);
        _menuIcon3rdBadge = (TextView) findViewById(R.id.tv_search_titleBar_menu_3rd_badge);
        _menuIcon1stParent = findViewById(R.id.viewparent_search_titleBar_menu_1st);
        _menuIcon2ndParent = findViewById(R.id.viewparent_search_titleBar_menu_2nd);
        _menuIcon3rdParent = findViewById(R.id.viewparent_search_titleBar_menu_3rd);
        _menuIcon1st = (ImageView) findViewById(R.id.iv_search_titleBar_menu_1st);
        _menuIcon2nd = (ImageView) findViewById(R.id.iv_search_titleBar_menu_2nd);
        _menuIcon3rd = (ImageView) findViewById(R.id.iv_search_titleBar_menu_3rd);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.NaviBar_isShowBack) {
                showBack(a.getBoolean(attr, true), false);

            } else if (attr == R.styleable.NaviBar_isGoneBack) {
                showBack(false, a.getBoolean(attr, true));

            } else if (attr == R.styleable.NaviBar_backIcon) {
                setBackIcon(a.getResourceId(attr, R.drawable.transparent));

            } else if (attr == R.styleable.NaviBar_mode) {
                setMode(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_titleStr) {
                setTitle(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_filterStr) {
                setFilterText(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_searchHintStr) {
                setTextBoxPlaceHolder(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_searchStr) {
                setSearchText(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_subtitleStr) {
                setSubTitle(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_isShowFilter) {
                showFilter(a.getBoolean(attr, true));

            } else if (attr == R.styleable.NaviBar_isShowMenu) {
                showMenu(a.getBoolean(attr, true));

            } else if (attr == R.styleable.NaviBar_menu_text) {
                setMenuText(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_left_menu_text) {
                setLeftMenuText(a.getString(attr));

            } else if (attr == R.styleable.NaviBar_titleAlign) {
                int align = a.getInt(attr, 0);
                setTextAlign(_title, align);

            } else if (attr == R.styleable.NaviBar_titleFontSize) {//TODO

            } else if (attr == R.styleable.NaviBar_menu_icon_1st) {
                setMenuIcon(MENU_BUTTON_ICON1ST, a.getResourceId(attr, R.drawable.transparent));

            } else if (attr == R.styleable.NaviBar_menu_icon_2nd) {
                setMenuIcon(MENU_BUTTON_ICON2ND, a.getResourceId(attr, R.drawable.transparent));

            } else if (attr == R.styleable.NaviBar_menu_icon_3rd) {
                setMenuIcon(MENU_BUTTON_ICON3RD, a.getResourceId(attr, R.drawable.transparent));

            } else if (attr == R.styleable.NaviBar_menu_icon_1st_badge) {
                setBadge(MENU_BUTTON_ICON1ST, a.getString(attr));

            } else if (attr == R.styleable.NaviBar_menu_icon_2nd_badge) {
                setBadge(MENU_BUTTON_ICON2ND, a.getString(attr));

            } else if (attr == R.styleable.NaviBar_menu_icon_3rd_badge) {
                setBadge(MENU_BUTTON_ICON3RD, a.getString(attr));

            } else if (attr == R.styleable.NaviBar_titleColor) {
                setTextColor(_title, a.getColor(attr, getResources().getColor(R.color.defaultTextColor)));
                setTextColor(_superTitle, a.getColor(attr, getResources().getColor(R.color.defaultTextColor)));

            } else if (attr == R.styleable.NaviBar_subtitleColor) {
                setTextColor(_subTitle, a.getColor(attr, getResources().getColor(R.color.defaultTextColor)));

            } else if (attr == R.styleable.NaviBar_filterColor) {
                setTextColor(_searchFilter, a.getColor(attr, getResources().getColor(R.color.defaultTextColor)));

            } else if (attr == R.styleable.NaviBar_bgBarColor) {
                setBgBarColor(_rootView, a.getColor(attr, getResources().getColor(R.color.colorAccent)));

            } else if (attr == R.styleable.NaviBar_searchColor) {
                setTextColor(_searchQueryText, a.getColor(attr, getResources().getColor(R.color.defaultTextColor)));

            } else if (attr == R.styleable.NaviBar_menuTextColor) {
                setTextColor(_menuText, a.getColor(attr, getResources().getColor(R.color.defaultTextColor)));

            } else if (attr == R.styleable.NaviBar_searchHintColor) {
                setHintColor(_searchQueryText, a.getColor(attr, getResources().getColor(R.color.defaultTextColor)));

            } else {
            }
        }
        a.recycle();
//        setMode(mode);
    }

    // region : @fred  [2016/11/15]


    //**************************** 涉及  更新/渲染View

    /**
     * 设置TitleBar
     *
     * @param mode
     */
    public void setMode(byte mode) {
        this.mode = mode;
        switch (mode) {
            case MODE_TITLE_STYLE_NORMAL:
                _title.setVisibility(VISIBLE);
                _complexView.setVisibility(GONE);
                _searchView.setVisibility(GONE);
                break;
            case MODE_TITLE_STYLE_COMPLEX:
                _title.setVisibility(GONE);
                _searchView.setVisibility(GONE);
                _complexView.setVisibility(VISIBLE);
                break;
            case MODE_TITLE_STYLE_SEARCH:
                _searchView.setVisibility(VISIBLE);
                _title.setVisibility(GONE);
                _complexView.setVisibility(GONE);
                break;
            case MODE_TITLE_STYLE_SEARCH_WITHOUT_FILTER:
                _title.setVisibility(GONE);
                _searchView.setVisibility(VISIBLE);
                _complexView.setVisibility(GONE);
                break;
            default:
                break;
        }
        initEventAndPresentByMode(mode);
    }

    public void setMode(String mode) {
        if (mode == null || "default".equalsIgnoreCase(mode)) {
            setMode(MODE_TITLE_STYLE_SEARCH);
        } else if ("normal".equalsIgnoreCase(mode)) {
            setMode(MODE_TITLE_STYLE_NORMAL);
        } else if ("complex".equalsIgnoreCase(mode)) {
            setMode(MODE_TITLE_STYLE_COMPLEX);
        } else if ("searchStandard".equalsIgnoreCase(mode)) {
            setMode(MODE_TITLE_STYLE_SEARCH);
        } else if ("search".equalsIgnoreCase(mode)) {
            setMode(MODE_TITLE_STYLE_SEARCH_WITHOUT_FILTER);
        }
    }

    /**
     * 根据mode设置控件默认状态
     *
     * @param mode
     */
    private void initEventAndPresentByMode(byte mode) {
        switch (mode) {
            case MODE_TITLE_STYLE_NORMAL:
            case MODE_TITLE_STYLE_COMPLEX:
                break;
            case MODE_TITLE_STYLE_SEARCH:
                //设置Filter点击事件
                subscribe(RxView.clicks(_searchFilter)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                if (onNaviBarEventListener != null) {
                                    onNaviBarEventListener.onSearchFilterClick(_searchFilter);
                                }
                            }
                        }));
            case MODE_TITLE_STYLE_SEARCH_WITHOUT_FILTER:
                _searchQueryText.addTextChangedListener(mTextWatcher);
                _searchQueryText.setOnEditorActionListener(mOnEditorActionListener);
                _searchQueryText.setOnKeyListener(mTextKeyListener);
                break;
            default:
                break;
        }
        //设置图标及菜单事件
        subscribe(RxView.clicks(_backView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onNaviBarEventListener != null) {
                            if (onNaviBarEventListener.onBackClick(_backView)) return;
                        }
                        if (getContext() instanceof Activity) {
                            ((Activity) getContext()).onBackPressed();
                        }
                    }
                }));
        subscribe(RxView.clicks(_backViewMenuText)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onNaviBarEventListener != null) {
                            if (onNaviBarEventListener.onLeftMenuTextClick(_backViewMenuText))
                                return;
                        }
                        if (getContext() instanceof Activity) {
                            ((Activity) getContext()).onBackPressed();
                        }
                    }
                }));
        subscribe(RxView.clicks(_menuText)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onNaviBarEventListener != null) {
                            onNaviBarEventListener.onMenuClick(_menuText, MENU_BUTTON_TEXT);
                        }
                    }
                }));

        subscribe(RxView.clicks(_menuIcon1st)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onNaviBarEventListener != null) {
                            onNaviBarEventListener.onMenuClick(_menuIcon1st, MENU_BUTTON_ICON1ST);
                        }
                    }
                }));
        subscribe(RxView.clicks(_menuIcon2nd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onNaviBarEventListener != null) {
                            onNaviBarEventListener.onMenuClick(_menuIcon2nd, MENU_BUTTON_ICON2ND);
                        }
                    }
                }));

        subscribe(RxView.clicks(_menuIcon3rd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onNaviBarEventListener != null) {
                            onNaviBarEventListener.onMenuClick(_menuIcon2nd, MENU_BUTTON_ICON2ND);
                        }
                    }
                }));
    }

    protected CompositeDisposable mCompositeSubscription;

    public void subscribe(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.dispose();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unSubscribe();
    }


    //**************************** 资源或属性直接改变

    /**
     * 设置返回图标
     *
     * @param res
     */
    public void setBackIcon(int res) {
        _backViewIcon.setImageResource(res);
    }

    /**
     * 设置返回是否否显示
     *
     * @param isShow 区域保留|是否隐藏
     * @param b      隐藏左侧图标
     */
    public void showBack(boolean isShow, boolean b) {
        if (b) {
            _backView.setVisibility(GONE);
        } else {
            if (!isShow) {
                _backView.setClickable(false);
                _backView.setFocusableInTouchMode(false);
            } else {
                _backView.setClickable(true);
                _backView.setFocusableInTouchMode(true);
            }
            _backView.setVisibility(isShow ? VISIBLE : INVISIBLE);
        }
    }

    public void showBack() {
        _backView.setVisibility(VISIBLE);
    }

    public void hideBack() {
        _backView.setVisibility(INVISIBLE);
    }

    public void zeroBack() {
        _backView.setVisibility(GONE);
    }

    /**
     * 设置菜单图标
     *
     * @param index
     * @param resourceId
     */
    public void setMenuIcon(int index, int resourceId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId);
        if (index == MENU_BUTTON_ICON1ST) {
            if (resourceId == R.drawable.transparent) {
                _menuIcon1stParent.setVisibility(GONE);
                _menuIcon1st.setVisibility(GONE);
            } else {
                _menuContainer.setVisibility(VISIBLE);
                _menuIcon1stParent.setVisibility(VISIBLE);
                _menuIcon1st.setVisibility(VISIBLE);
                _menuIcon1st.setImageDrawable(drawable);
            }
        } else if (index == MENU_BUTTON_ICON2ND) {
            if (resourceId == R.drawable.transparent) {
                _menuIcon2ndParent.setVisibility(GONE);
                _menuIcon2nd.setVisibility(GONE);
            } else {
                _menuContainer.setVisibility(VISIBLE);
                _menuIcon2ndParent.setVisibility(VISIBLE);
                _menuIcon2nd.setVisibility(VISIBLE);
                _menuIcon2nd.setImageDrawable(drawable);
            }
        } else if (index == MENU_BUTTON_ICON3RD) {
            if (resourceId == R.drawable.transparent) {
                _menuIcon3rdParent.setVisibility(GONE);
                _menuIcon3rd.setVisibility(GONE);
            } else {
                _menuContainer.setVisibility(VISIBLE);
                _menuIcon3rdParent.setVisibility(VISIBLE);
                _menuIcon3rd.setVisibility(VISIBLE);
                _menuIcon3rd.setImageDrawable(drawable);
            }
        }
        _menuText.setVisibility(GONE);
    }

    /**
     * 设置菜单文本
     *
     * @param string
     */
    public void setMenuText(CharSequence string) {
        if (string == null || "".equals(string)) {
            return;
        }
        _menuText.setText(string);
        _menuText.setVisibility(VISIBLE);
//        _menuIcon1st.setVisibility(GONE);
//        _menuIcon2nd.setVisibility(GONE);
//        _menuIcon3rd.setVisibility(GONE);
    }

    /**
     * @param string
     */
    public void setLeftMenuText(CharSequence string) {
        if (string == null || string.length() == 0) {
            _backViewMenuText.setVisibility(GONE);
            return;
        }
        _backViewMenuText.setText(string);
        _backViewMenuText.setVisibility(VISIBLE);
    }


    /**
     * 设置输入框的PlaceHolder / hint
     *
     * @param string
     */
    public void setTextBoxPlaceHolder(CharSequence string) {
        if (string == null) string = "";
        _searchQueryText.setHint(string);
    }

    /**
     * 设置是否显示搜索过滤
     *
     * @param isShow
     */
    public void showFilter(boolean isShow) {
        _searchFilter.setVisibility(isShow ? VISIBLE : GONE);
    }

    /**
     * 设置是否显示菜单
     *
     * @param isShow
     */
    public void showMenu(boolean isShow) {
        _menuContainer.setVisibility(isShow ? VISIBLE : GONE);
    }

    /**
     * 设置标题 改标题也是复合标题中的大标题
     *
     * @param mTitle
     */
    public void setTitle(CharSequence mTitle) {
        if (mTitle == null) mTitle = "";
        _title.setText(mTitle);
        _superTitle.setText(mTitle);
    }

    /**
     * 设置小标题
     *
     * @param mTitle
     */
    public void setSubTitle(CharSequence mTitle) {
        if (mTitle == null) mTitle = "";
        _subTitle.setText(mTitle);
    }

    /**
     * 设置搜索文字
     *
     * @param searchText
     */
    public void setSearchText(CharSequence searchText) {
        if (searchText == null) searchText = "";
        _searchQueryText.setText(searchText);
    }

    /**
     * 设置分类文字
     *
     * @param filterText
     */
    public void setFilterText(CharSequence filterText) {
        if (filterText == null) filterText = "";
        _searchFilter.setText(filterText);
    }

    /**
     * 设置提示颜色
     *
     * @param searchQueryText
     * @param color
     */
    public void setHintColor(EditText searchQueryText, int color) {
        if (searchQueryText != null) {
            searchQueryText.setHintTextColor(color);
        }
    }

    /**
     * 设置文本颜色
     *
     * @param title
     * @param color
     */
    public void setTextColor(TextView title, int color) {
        if (title != null) {
            title.setTextColor(color);
        }
    }


    /**
     * 设置背景色
     *
     * @param rootView
     * @param color
     */
    public void setBgBarColor(ViewGroup rootView, int color) {
        rootView.setBackgroundColor(color);
    }

    public void setBgBarColor(int color) {
        _rootView.setBackgroundColor(color);
    }

    /**
     * 设置图标的badge
     *
     * @param i
     * @param num
     */
    public void setBadge(int i, String num) {
        if (i == MENU_BUTTON_ICON1ST) {
            if (TextUtils.isEmpty(num) || "0".equals(num)) {
                _menuIcon1stBadge.setVisibility(GONE);
            } else {
                _menuIcon1stBadge.setVisibility(VISIBLE);
                _menuIcon1stBadge.setText(num);
            }

        } else if (i == MENU_BUTTON_ICON2ND) {
            if (TextUtils.isEmpty(num) || "0".equals(num)) {
                _menuIcon2ndBadge.setVisibility(GONE);
            } else {
                _menuIcon2ndBadge.setVisibility(VISIBLE);
                _menuIcon2ndBadge.setText(num);
            }
        } else if (i == MENU_BUTTON_ICON3RD) {
            if (TextUtils.isEmpty(num) || "0".equals(num)) {
                _menuIcon3rdBadge.setVisibility(GONE);
            } else {
                _menuIcon3rdBadge.setVisibility(VISIBLE);
                _menuIcon3rdBadge.setText(num);
            }
        }

    }


    /**
     * 获取图标的badge
     *
     * @param i
     */
    public String getBadge(int i) {
        if (i == MENU_BUTTON_ICON1ST) {
            return _menuIcon1stBadge.getText().toString();

        } else if (i == MENU_BUTTON_ICON2ND) {

            return _menuIcon2ndBadge.getText().toString();
        } else if (i == MENU_BUTTON_ICON3RD) {

            return _menuIcon3rdBadge.getText().toString();
        }
        return null;
    }


    /**
     * 设置文本浮动方式
     *
     * @param title
     * @param align
     */
    public void setTextAlign(TextView title, int align) {
        if (title != null) {
            int gravity;
            if (align == 1) {
                gravity = Gravity.CENTER;
            } else if (align == 2) {
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            } else {
                gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            }
            title.setGravity(gravity);
        }
    }


    private void onTextChanged(CharSequence newText) {
        CharSequence text = _searchQueryText.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }


    public void setOnNaviBarEventListener(OnNaviBarEventListener onNaviBarEventListener) {
        this.onNaviBarEventListener = onNaviBarEventListener;
    }

    public void setmOnQueryChangeListener(OnQueryTextListener mOnQueryChangeListener) {
        this.mOnQueryChangeListener = mOnQueryChangeListener;
    }

    /************************************************ 事件接口 ****************************************************************************
     *
     */


    /**
     * Callback to watch the text field for empty/non-empty
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        }

        public void onTextChanged(CharSequence s, int start,
                                  int before, int after) {
            NaviBar.this.onTextChanged(s);
        }

        public void afterTextChanged(Editable s) {
        }
    };


    /**
     * React to the user typing "enter" or other hardwired keys while typing in
     * the search box. This handles these special keys while the edit box has
     * focus.
     */
    OnKeyListener mTextKeyListener = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If there is text in the query box, handle enter, and action keys
            // The search key is handled by the dialog's onKeyDown().
            if (!TextUtils.isEmpty(_searchQueryText.getText()) && event.hasModifiers(KeyEvent.META_SHIFT_ON)) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();
                        // Launch as a regular search.
                        if (onNaviBarEventListener != null)
                            onNaviBarEventListener.launchQuerySearch(_searchQueryText.getText()
                                    .toString());
                        return true;
                    }
                }
            }
            return false;
        }
    };


    /**
     * Callbacks for changes to the query text.
     */
    public interface OnQueryTextListener {

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
         * suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(String newText);
    }


    public interface OnNaviBarEventListener {

        void onSearchFilterClick(View v);

        boolean onBackClick(View v);

        void launchQuerySearch(String query);

        void onMenuClick(View v, int i);

        boolean onLeftMenuTextClick(View v);
    }

    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {

        /**
         * Called when the input method default action key is pressed.
         */
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (onNaviBarEventListener != null)
                onNaviBarEventListener.launchQuerySearch(_searchQueryText.getText()
                        .toString());
            return true;
        }
    };


    /**
     * 反射
     */
    private static class AutoCompleteTextViewReflector {
        private Method doBeforeTextChanged, doAfterTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() {
            try {
                doBeforeTextChanged = AutoCompleteTextView.class
                        .getDeclaredMethod("doBeforeTextChanged");
                doBeforeTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                doAfterTextChanged = AutoCompleteTextView.class
                        .getDeclaredMethod("doAfterTextChanged");
                doAfterTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                ensureImeVisible = AutoCompleteTextView.class
                        .getMethod("ensureImeVisible", boolean.class);
                ensureImeVisible.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                showSoftInputUnchecked = InputMethodManager.class.getMethod(
                        "showSoftInputUnchecked", int.class, ResultReceiver.class);
                showSoftInputUnchecked.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
        }

        void doBeforeTextChanged(AutoCompleteTextView view) {
            if (doBeforeTextChanged != null) {
                try {
                    doBeforeTextChanged.invoke(view);
                } catch (Exception e) {
                }
            }
        }

        void doAfterTextChanged(AutoCompleteTextView view) {
            if (doAfterTextChanged != null) {
                try {
                    doAfterTextChanged.invoke(view);
                } catch (Exception e) {
                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView view, boolean visible) {
            if (ensureImeVisible != null) {
                try {
                    ensureImeVisible.invoke(view, visible);
                } catch (Exception e) {
                }
            }
        }

        void showSoftInputUnchecked(InputMethodManager imm, View view, int flags) {
            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, flags, null);
                    return;
                } catch (Exception e) {
                }
            }

            // Hidden method failed, call public version instead
            imm.showSoftInput(view, flags);
        }
    }


    //************************  SETTER & GETTER


    public View get_backView() {
        return _backView;
    }

    public ImageView get_backViewIcon() {
        return _backViewIcon;
    }

    public TextView get_title() {
        return _title;
    }

    public View get_complexView() {
        return _complexView;
    }

    public TextView get_superTitle() {
        return _superTitle;
    }

    public TextView get_subTitle() {
        return _subTitle;
    }

    public View get_searchView() {
        return _searchView;
    }

    public TextView get_searchFilter() {
        return _searchFilter;
    }

    public ClearEditText getTextBox() {
        return _searchQueryText;
    }

    public View get_menuContainer() {
        return _menuContainer;
    }

    public TextView get_menuText() {
        return _menuText;
    }

    public ImageView get_menuIcon1st() {
        return _menuIcon1st;
    }

    public ImageView get_menuIcon2nd() {
        return _menuIcon2nd;
    }

    public ImageView get_menuIcon3rd() {
        return _menuIcon3rd;
    }

    // endregion
}

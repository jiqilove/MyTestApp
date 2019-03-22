package com.example.myweekview.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


import com.example.myweekview.DateUtil;
import com.example.myweekview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 1、item点击
 * 2、setShowSelectRect      点击是否显示 选项条 true 显示
 * 3、setShowTodayPosition   定位到当天日期。并且选中
 * 4、setCanClick            是否能点击
 * 5、setTodayBackground。设置当天的背景
 * /////////这个可以忽略------>>>----->>>>setDatas     适用于 需要选中当前时间并且需要知道日期的情况，当weekview 不需要数据交互时，可以不设置
 * 6、onItemClick.myItemClick("时间未到",-1);
 * -------->>>>>与 afterToadyOnClick 对应， index =-1代表 不能点击
 * 7、setFollowBorderColor  设置跟随边框颜色  需要重新加多一种 drawable 样式
 * 8、添加预定选中背景与其文字颜色 CalendarItem.class   CalendarKV.class 进行修改  可以参考   MedicineActivity.class 的设置
 * 9、app:wv_showWeekMode="2" 用来设置显示模式 ----->> 在xml 文件中使用
 * <p>
 * <p>
 * 注意：
 * 在使用时，绑定数据使用setDatas（）； 数据绑定
 *               其中 setmWeeks(）； 样式显示。 为了自定义出一周中每天的显示字样；
 *      weekView.setmWeeks(newWeeks,newWeeks); 两者都需要更新时，调用这个方法
 */

public class WeekView<T extends CalendarItem> extends View {
    //===设置显示一周的样式
    public static final int MODE_STARTWITHSUN = 1;
    public static final int MODE_STARTWITHSUN2 = 2;
    public static final int MODE_STARTWITHMON = 3;
    public static final int MODE_STARTWITHMON2 = 4;
    public static final int MODE_SLEF = 5;//tian

    private String[] startWithSun = {"日", "一", "二", "三", "四", "五", "六"};
    private String[] startWithSun2 = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private String[] startWithMon = {"一", "二", "三", "四", "五", "六", "日"};
    private String[] startWithMon2 = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private int weekMode;//显示一周的文字样式
    private String[] mWeeks = {"日", "一", "二", "三", "四", "五", "六"};


    private String[] mWeeksOfDay = new String[7]; // 2018-12-09  显示为 09

    private Drawable mDayBackground;//默认天的背景
    private Drawable todayBackground;//今天的背景
    private Drawable mSelectDayBackground;//选中后天的背景
    private final Paint mPaint;//画笔
    private int mSelectTextColor; //选中后文本颜色
    private int mSelectTextSize;//选中后文本大小
    private int mTextSize; //默认文本大小
    private int mTextColor;//默认文本颜色
    private List<T> mSelectDate;//已选择日期数据
    private List<T> mSelectDate_backups;//已选择日期数据_备份数据
    private Map<Integer, Drawable> mSelectDateType;
    private int defaultType;

    private int mSlop;//系统默认最小移动距离

    private Typeface mTypeface;

    private float mMeasureTextWidth;
    private int mColumnWidth;
    private int mRowHeight;
    private OnItemClick onItemClick;
    private String[] datas;

    private boolean highlightToday = false;
    private boolean showSelectRect = false;
    private int curDayIndex = 0;
    private int todayIndex = -1;
    private boolean afterToadyOnClick = true; // 设置今天之后的天数能否点击  true 可以点击， false 不允许点击

    /**
     * //时候跟随边框颜色变化
     */
    private boolean followBorderColor = false;

    public boolean getFollowBorderColor() {
        return followBorderColor;
    }

    public void setFollowBorderColor(boolean followBorderColor) {
        this.followBorderColor = followBorderColor;
    }


    // 是否高亮当前日期

    private boolean showTodayPosition; //设置是否定位到当天的位置


    public boolean getIsHighlightToday() {
        return highlightToday;
    }

    /**
     * 用于日期高亮  格式 2018-08-08
     *
     * @param highlightToday
     */
    public void setHighlightToday(boolean highlightToday) {
        this.highlightToday = highlightToday;
    }

    public String[] getmWeeks() {
        return mWeeks;
    }

    public void setmWeeks(String[] Weeks) {
        this.mWeeks = Weeks;
        //==格式转化。 如果不是日期 则直接替换
        for (int i = 0; i < Weeks.length; i++) {
            if (!Weeks[i].contains("-")) {
                setmWeeksOfDay(mWeeks);
                break;
            } else {
                mWeeksOfDay[i] = Weeks[i].substring(Weeks[i].lastIndexOf("-") + 1, Weeks[i].length());
                setmWeeksOfDay(mWeeksOfDay);
            }
        }
    }

    public void setmWeeks(String[] Weeks, String[] datas) {
        this.mWeeks = Weeks;
        this.datas = datas;
        //==格式转化。 如果不是日期 则直接替换
        for (int i = 0; i < Weeks.length; i++) {
            if (!Weeks[i].contains("-")) {
                setmWeeksOfDay(mWeeks);
                break;
            } else {
                mWeeksOfDay[i] = Weeks[i].substring(Weeks[i].lastIndexOf("-") + 1, Weeks[i].length());
                setmWeeksOfDay(mWeeksOfDay);
            }
        }
    }

    public void setDatas(String[] datas) {
        this.datas = datas;
    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public WeekView(Context context) {
        this(context, null);

    }

    public WeekView(String[] mWeeks, Context context) {
        this(context, null);
        this.mWeeks = mWeeks;
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mSelectDate = new ArrayList<>();
        mSelectDate_backups = new ArrayList<>();
        mSelectDateType = new HashMap<>();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeekView);
        int textColor = a.getColor(R.styleable.WeekView_wv_textColor, Color.BLACK);
        setTextColor(textColor);
        int textSize = a.getDimensionPixelSize(R.styleable.WeekView_wv_textSize, -1);
        setTextSize(textSize);

        int weekMode = a.getInteger(R.styleable.WeekView_wv_showWeekMode, 1);
        setWeekMode(weekMode);
        Drawable dayBackground = a.getDrawable(R.styleable.WeekView_wv_dayBackground);
        setDayBackground(dayBackground);

        Drawable selectDayBackground = a.getDrawable(R.styleable.WeekView_wv_selectDayBackground);
        HashMap<Integer, Drawable> map = new HashMap<>();
        mSelectDateType.put(0, selectDayBackground);
        setSelectDayBackground(map, 0);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) (mMeasureTextWidth * getmWeeksOfDay().length) + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) mMeasureTextWidth + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(widthSize, heightSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String todayStr = "";
        if (getIsHighlightToday()) {
            todayStr = DateUtil.getToday();
        }
        mRowHeight = getHeight();
        if (mTextSize != -1) {
            mPaint.setTextSize(mTextSize);
        }
        if (mTypeface != null) {
            mPaint.setTypeface(mTypeface);
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        //每一个小节的宽度
        mColumnWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
        for (int i = 0; i < getmWeeksOfDay().length; i++) {
            String text = getmWeeksOfDay()[i];
            if (!getmWeeks()[i].contains("-")) {
                text = getmWeeks()[i];
            }


            if (getmWeeks()[i].equals(todayStr)) {
                todayIndex = i;
                curDayIndex = i;
                text = "今";
            }

            int fontWidth = (int) mPaint.measureText(text);
            int startX = mColumnWidth * i + (mColumnWidth - fontWidth) / 2 + getPaddingLeft();

            int startY = (int) ((getHeight()) / 2 - (mPaint.ascent() + mPaint.descent()) / 2) + getPaddingTop();
            CalendarItem item;

            String data = "";
            if (datas != null && datas.length > 0) {
                data = (datas[i]);
            } else {
                data = (getmWeeks()[i]);

            }

            if (mSelectDate != null && mSelectDate.size() != 0 && ((item = isContains(data)) != null)) {
                // 否则绘制选择后的背景和文字颜色,------->显示不同的背景或者布局
                int type = item.getSelectDataType();

                if (item.getTextColor() == 0) {

                    mPaint.setColor(mTextColor);
                } else {
                    mPaint.setColor(item.getTextColor());
                }

                Log.e("cnb", "onDraw:1= " + mSelectDate.size() + "type " + type);
                if (mSelectDateType.containsKey(type)) {
                    Drawable drawable = mSelectDateType.get(type);
                    if (drawable != null) {
                        setCompoundDrawablesWithIntrinsicBounds(drawable);
                        drawBackground(canvas, drawable, i);
                    }


                    if (mSelectTextColor == 0) {
                        mSelectTextColor = mTextColor;
                    }


                    if (mSelectTextSize == 0) {
                        mSelectTextSize = mTextSize;
                    }

                    mPaint.setTextSize(mSelectTextSize);
                    if (showSelectRect) {

                        canvas.drawText(text, startX, startY, mPaint);
                        canvas.drawRect(mColumnWidth * i + 5, startY + 20, mColumnWidth * i + mColumnWidth - 5, startY + 30, mPaint);

                    } else {
                        canvas.drawText(text, startX, startY, mPaint);
                    }


                } else {
                    Log.e("===", "需要调用setSelectDayBackground()进行绑定dewable，如果设置setFollowBorderColor=true，通过 HashMap<Integer, Drawable>添加对应的填充颜色");
                }

            } else {
//                Log.e("cnb", "onDraw:2 ="+mSelectDate.size());
                mPaint.setColor(mTextColor);
                mPaint.setTextSize(mTextSize);

                if (text.equals("今")) {
                    drawBackground(canvas, todayBackground, i);
                    if (todayBackground != null) {
                        drawBackground(canvas, todayBackground, i);
                    } else {
                        drawBackground(canvas, mDayBackground, i);
                    }
//                    mPaint.setColor(Color.WHITE);
                    canvas.drawText(text, startX, startY, mPaint);
                } else {
                    drawBackground(canvas, mDayBackground, i);
                    canvas.drawText(text, startX, startY, mPaint);
                }


            }

        }
    }

    private void drawBackground(Canvas canvas, Drawable background, int column) {
        if (background != null) {
            canvas.save();
            int dx = (mColumnWidth * column) + (mColumnWidth / 2) - (background.getIntrinsicWidth() / 2);
            canvas.translate(dx, 0);
            background.draw(canvas);
            canvas.restore();
        }
    }


    private int mDownX = 0, mDownY = 0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX(); //
                int upY = (int) event.getY();
                int diffX = Math.abs(upX - mDownX);
                int diffY = Math.abs(upY - mDownY);
                if (diffX < mSlop && diffY < mSlop) {
                    int column = upX / mColumnWidth;
//                    int row = upY / mRowHeight;
                    onClick(column);
                }

                break;

            default:

        }


        return super.onTouchEvent(event);
    }


    private boolean singleClick = false;
    private boolean selectBgShow = true;

    public boolean getSingleClick() {
        return singleClick;
    }

    public void setSingleClick(boolean singleClick) {
        this.singleClick = singleClick;
    }

    /**
     * @param singleClick
     * @param selectBgShow
     */
    public void setSingleClick(boolean singleClick, boolean selectBgShow) {
        this.singleClick = singleClick;
        this.selectBgShow = selectBgShow;
    }

    private int oldSelectIndex;
    String date = "";

    private void onClick(int index) {
        if (!singleClick) {
            return;
        }
        if (onItemClick != null) {
            if (todayIndex < index && !afterToadyOnClick) {
                onItemClick.myItemClick("时间未到", -1);
                return;
            } else {
                onItemClick.myItemClick(mWeeks[index], index);
            }
        }

        if (datas != null) {
            date = datas[index];
        } else {
            date = mWeeks[index];
        }

        final CalendarItem item = isContains(date);

        /**
         * 创建一个新的CalendarItem，为下面重新设置颜 挂钩
         * 其中
         * followBorderColor ：通过 让文本跟随 边框颜色，前提填充颜色不能和边框同样颜色
         * beforeSelectItem  ：一直显示的标志。点击时通过beforeSelectItem 来判断
         *                    所选的的item是不是之前就通过 selectDates（）固定选好不能改变的
         * getSelectDataType ：类型不等于默认。如果不等于默认，那将会选择自动填充满
         */
        T t = (T) new CalendarItem() {
            @Override
            public int getTextColor() {
                if (followBorderColor && item != null) { // 设置跟随边框颜色
                    return ContextCompat.getColor(getContext(), R.color.white);
                } else {
                    if (mSelectTextColor == 0) {
                        return mTextColor;
                    }
                    return mSelectTextColor;
                }
            }

            @Override
            public boolean beforeSelectItem() {
                return false;
            }

            @Override
            public String getSelectData() {
                return date;
            }

            @Override
            public int getSelectDataType() {
                if (followBorderColor && item != null) {
                    if (item.getSelectDataType() == 1) {
                        return item.getSelectDataType();
                    }
                    return (item.getSelectDataType() * 2);
                } else {
                    return defaultType;
                }

            }
        };



        /**
         * 逻辑：
         *---> 条目不为空并且已经选择的日期不为空 ---> 判断所选条目与上次所选条目是否相同。--->去除所有默认颜色，重新附加
         * --->直接附加样式在所选条目
         */
        if (mSelectDate != null && item != null ) {
            if (oldSelectIndex != index) {
                mSelectDate.clear();
                mSelectDate.addAll(mSelectDate_backups);//恢复默认在做修改
                Iterator<T> iterator = mSelectDate.iterator();
                while (iterator.hasNext()) {
                    T x = iterator.next();
                    if (!x.beforeSelectItem()) {
                        iterator.remove();
                        continue;
                    }
                }
                mSelectDate_backups.clear(); //清除所有
                mSelectDate_backups.addAll(mSelectDate);//备份预选信
                //====已经存在的选择日期 改变颜色
                for (int i = 0; i < mSelectDate.size(); i++) {
                    if (DateUtil.formatDate(mSelectDate.get(i).getSelectData(), "yyyy-mm-dd").equals(date)) {
                        mSelectDate.set(i, t);
                        break;
                    }
                }
                //====当没有预先选择的日期=====
                if (mSelectDate.size() == 0) {
                    mSelectDate.add(t);
                    mSelectDateType.put(defaultType, mSelectDayBackground);
                    return;
                }
                mSelectDate.add(t);

            }

        } else {
            if (oldSelectIndex == index) {
                return;
            }
            Log.e(TAG, "onClick:  no select");
            if (!selectBgShow) {
                mSelectDate.clear();//清除所有是为了实现单击效果，
            }
            Iterator<T> iterator = mSelectDate.iterator();
            while (iterator.hasNext()) {
                T x = iterator.next();
                if (!x.beforeSelectItem()) {
                    iterator.remove();
                    continue;
                }
            }
            mSelectDate.clear();
            mSelectDate.addAll(mSelectDate_backups);

            mSelectDate.add(t);
            mSelectDateType.put(defaultType, mSelectDayBackground);
        }
        oldSelectIndex = index;
        invalidate();
    }

    private static final String TAG = "WeekView";

    public void setSelectDate(List<T> days) {
        this.mSelectDate.addAll(days);
        mSelectDate_backups.addAll(days);

        invalidate();
    }


    /**
     * 设置字体大小.
     *
     * @param size text size.
     */
    public void setTextSize(int size) {
        this.mTextSize = size;
        mPaint.setTextSize(mTextSize);
        mMeasureTextWidth = mPaint.measureText(getmWeeks()[0]);
    }

    /**
     * 设置文字颜色.
     *
     * @param color 颜色.
     */
    public void setTextColor(@ColorInt int color) {
        this.mTextColor = color;
        mPaint.setColor(mTextColor);
    }

    /**
     * 设置字体.
     *
     * @param typeface {@link Typeface}.
     */
    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        invalidate();
    }

    /**
     * 设置天的背景.
     *
     * @param background 背景 drawable.
     */
    public void setDayBackground(Drawable background) {
        if (background != null && mDayBackground != background) {
            this.mDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mDayBackground);
        }

    }

    /**
     * 设置今日 日期背景
     *
     * @param background
     */
    public void setTodayBackground(Drawable background) {

        if (background != null && todayBackground != background) {
            this.todayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(todayBackground);
        }
    }


    public void setShowSelectRect(boolean setTextCenter) {
        this.showSelectRect = setTextCenter;
    }

    public void invalidateView() {
        invalidate();
    }


    /**
     * 设置选择后,天的背景.
     *
     * @param mSelectDateType
     */
    public void setSelectDayBackground(HashMap<Integer, Drawable> mSelectDateType, int defaultType) {
        this.mSelectDateType = mSelectDateType;
        this.defaultType = defaultType;
        mSelectDayBackground = mSelectDateType.get(defaultType);
    }

    /**
     * 这里要求drawable必须有宽和高,不然是不会显示的
     *
     * @param drawable
     */
    private void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str.substring(0, 1)).matches();
    }

    private CalendarItem isContains(String date) {
        if (!isNumeric(date)) {
            Log.e(TAG, "CalendarItem: 传入值不能为中文，请使用 setDatas（String [] weeks）绑定一周数据");
            return null;
        }

        date = DateUtil.formatDate(date, "yyyy-mm-dd");

        if (mSelectDate == null) {
            return null;
        } else {

            for (int i = 0; i < mSelectDate.size(); i++) {
                CalendarItem item = mSelectDate.get(i);
                if (DateUtil.formatDate(item.getSelectData(), "yyyy-mm-dd").equals(date)) {

                    Log.e(TAG, "isContains: " + date + "== type" + item.getSelectDataType());
                    return item;
                }
            }
            return null;
        }
    }

    //===选中文字颜色
    public int getmSelectTextColor() {
        return mSelectTextColor;
    }

    public void setmSelectTextColor(int mSelectTextColor) {
        this.mSelectTextColor = mSelectTextColor;
    }

    //===选中文字大小
    public int getmSelectTextSize() {
        return mSelectTextSize;
    }

    public void setmSelectTextSize(int mSelectTextSize) {
        this.mSelectTextSize = mSelectTextSize;
    }

    /**
     * 获取 {@link Paint} 对象.
     *
     * @return {@link Paint}.
     */
    public Paint getPaint() {
        return mPaint;
    }

    //===设置一周
    public String[] getmWeeksOfDay() {
        return mWeeksOfDay;
    }

    public void setmWeeksOfDay(String[] mWeeksOfDay) {
        this.mWeeksOfDay = mWeeksOfDay;
    }

    //设置是否定位到当天的位置
    //===使用这个方法之前先调用 setDatas（String【】 datas）
    public void setShowTodayPosition(boolean showTodayPosition) {
        this.showTodayPosition = showTodayPosition;
        if (showTodayPosition) {
            Log.e(TAG, "setShowTodayPosition: ");
            if (mWeeks != null) {
                for (int i = 0; i < mWeeks.length; i++) {
                    if (mWeeks[i].equals(DateUtil.getToday())) {
                        curDayIndex = i;
                        break;
                    }
                }
                onClick(curDayIndex);
            }
        }

    }


    //===设置一周显示样式
    public void setWeekMode(int weekMode) {
        this.weekMode = weekMode;
        switch (weekMode) {
            case MODE_STARTWITHSUN:
                mWeeks = startWithSun;
                break;
            case MODE_STARTWITHSUN2:
                mWeeks = startWithSun2;
                break;
            case MODE_STARTWITHMON:
                mWeeks = startWithMon;
                break;
            case MODE_STARTWITHMON2:
                mWeeks = startWithMon2;
                break;
            case MODE_SLEF:
                mWeeks = mWeeks;
                break;
        }

    }

    public boolean getAfterToadyOnClick() {
        return afterToadyOnClick;
    }

    public void setAfterToadyOnClick(boolean afterToadyOnClick) {
        this.afterToadyOnClick = afterToadyOnClick;
    }

    public interface OnItemClick<T> {
        void myItemClick(String date, int index);
    }
}
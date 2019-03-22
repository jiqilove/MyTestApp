package com.example.myweekview.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


import com.nenglong.jxhd.client.yeb.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Edited by cnb
 *
 * 简单的日历控件,增加不同选中背景的显示
 * https://github.com/Airsaid/CalendarView
 * <p>
 * 1、calendarView.setSelectDate(selectDay);
 * 2、calendarView.setSelectDayBackground(drawableHashMap, 1);
 * 3、calendarView.setFollowBorderColor(true);   ----> 跟随边框颜色变化，但还是需要做一个绑定。
 * ---------> CalendarKV getSelectDataType 中 设置允许返回的type 类型
 * 4、calendarView.setHighlightToday(true,"当"); ----> 定位到当天，高亮颜色，以及自定修改当天文字（一个字）
 * 5、 setHighLightToday 、 setSelectDay 、setChangStatus, 可以结合使用。 固定m
 */
public class CalendarView<T extends CalendarItem> extends View {

    final String TAG = "CalendarView";

    /**
     * 默认的日期格式化格式
     */
    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

    /**
     * 默认文字颜色
     */
    private int mTextColor;
    /**
     * 选中后文字颜色
     */
    private int mSelectTextColor;
    /**
     * 默认文字大小
     */
    private float mTextSize;
    /**
     * 选中后文字大小
     */
    private float mSelectTextSize;
    /**
     * 默认天的背景
     */
    private Drawable mDayBackground;
    /**
     * 选中后天的背景
     */
    private Drawable mSelectDayBackground;
    /**
     * 日期格式化格式
     */
    private String mDateFormatPattern;
    /**
     * 字体
     */
    private Typeface mTypeface;
    /**
     * 日期状态是否能够改变
     */
    private boolean mIsChangeDateStatus;
    /**
     * 是否高亮当天日期
     */
    private boolean highlightToday = false;

    /**
     * 今天的背景
     */
    private Drawable todayBackground;

    /**
     * 时候跟随边框颜色变化
     */
    private boolean followBorderColor = false;

    /**
     * 当天日期文字修改 ，只取第一个文字 或 数字
     */
    private String todayStr = "";

    /**
     * 每列宽度
     */
    private int mColumnWidth;
    /**
     * 每行高度
     */
    private int mRowHeight;

    /**
     * 设置能否点击
     */
    private boolean SingleClick = false;

    /**
     * 已选择日期数据
     */
    private List<T> mSelectDate;
    /**
     * 已选择日期数据——备份
     */
    private List<T> mSelectDate_backups;
    /**
     * 背景颜色类型设置保存
     */
    private Map<Integer, Drawable> mSelectDateType;
    private int defaultType;

    //===当前时间===
    private int curDay = 0;
    private int curYear = 0;
    private int curMoth = 0;

    /**
     * 存储对应列行处的天
     */
    private int[][] mDays = new int[6][7];

    private OnDataClickListener mOnDataClickListener;
    private OnDateChangeListener mChangeListener;
    private SimpleDateFormat mDateFormat;
    private Calendar mSelectCalendar;
    private Calendar mCalendar;
    private Paint mPaint;
    private int mSlop;

    public interface OnDataClickListener<T> {

        /**
         * 日期点击监听.
         *
         * @param view  与次监听器相关联的 View.
         * @param year  对应的年.
         * @param month 对应的月.
         * @param day   对应的日.
         */
        void onDataClick(@NonNull CalendarView view, int year, int month, int day, T item);
    }

    public interface OnDateChangeListener {

        /**
         * 选中的天发生了改变监听回调, 改变有 2 种, 分别是选中和取消选中.
         *
         * @param view   与次监听器相关联的 View.
         * @param select true 表示是选中改变, false 是取消改变.
         * @param year   对应的年.
         * @param month  对应的月.
         * @param day    对应的日.
         */
        void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day);
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mSelectCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar = Calendar.getInstance(Locale.CHINA);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectDate = new ArrayList<>();
        mSelectDate_backups = new ArrayList<>();
        mSelectDateType = new HashMap<>();
        curYear = mCalendar.get(Calendar.YEAR);
        // 获取的月份要少一月, 所以这里 + 1
        curMoth = mCalendar.get(Calendar.MONTH) + 1;
        curDay = mCalendar.get(Calendar.DAY_OF_MONTH);


        setClickable(true);

//        curDay = DateUtil.getToday();
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        int textColor = a.getColor(R.styleable.CalendarView_cv_textColor, Color.BLACK);
        setTextColor(textColor);

        int selectTextColor = a.getColor(R.styleable.CalendarView_cv_selectTextColor, Color.BLACK);
        setSelectTextColor(selectTextColor);

        float textSize = a.getDimension(R.styleable.CalendarView_cv_textSize, sp2px(14));
        setTextSize(textSize);

        float selectTextSize = a.getDimension(R.styleable.CalendarView_cv_selectTextSize, sp2px(14));
        setSelectTextSize(selectTextSize);

        Drawable dayBackground = a.getDrawable(R.styleable.CalendarView_cv_dayBackground);
        setDayBackground(dayBackground);

        Drawable selectDayBackground = a.getDrawable(R.styleable.CalendarView_cv_selectDayBackground);
        HashMap<Integer, Drawable> map = new HashMap<>();
        mSelectDateType.put(0, selectDayBackground);
        setSelectDayBackground(map, 0);

        String pattern = a.getString(R.styleable.CalendarView_cv_dateFormatPattern);
        setDateFormatPattern(pattern);

        boolean isChange = a.getBoolean(R.styleable.CalendarView_cv_isChangeDateStatus, false);
        setChangeDateStatus(isChange);

        a.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mColumnWidth = getWidth() / 7;
        mRowHeight = getHeight() / 6;
        mPaint.setTextSize(mTextSize);


        int year = mCalendar.get(Calendar.YEAR);
        // 获取的月份要少一月, 所以这里 + 1
        int month = mCalendar.get(Calendar.MONTH) + 1;


        // 获取当月的天数
        int days = getMonthDays(year, month);
        // 获取当月第一天位于周几
        int week = getFirstDayWeek(year, month);
        // 绘制每天
        for (int day = 1; day <= days; day++) {
            // 获取天在行、列的位置
            int column = (day + week - 1) % 7;
            int row = (day + week - 1) / 7;
            // 存储对应天
            mDays[row][column] = day;


//            String dayStr = String.valueOf(day);
            String dayStr = day + "";
            if (day == curDay && highlightToday) {
                if (curYear == year && curMoth == month) {
                    if (!todayStr.equals("")) {
                        dayStr = todayStr;
                    } else {
                        dayStr = "今";
                    }
                }

            }
            float textWidth = mPaint.measureText(dayStr);
            int x = (int) (mColumnWidth * column + (mColumnWidth - textWidth) / 2);
            int y = (int) (mRowHeight * row + mRowHeight / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            CalendarItem item;
            String date = getFormatDate(year, month - 1, day);
            if (mSelectDate != null && mSelectDate.size() != 0 && ((item = isContains(date)) != null)) {
                int textColor = 0;
                // 否则绘制选择后的背景和文字颜色,------->显示不同的背景或者布局
                int type = item.getSelectDataType();
                if (item.getTextColor() == 0) {
                    textColor = mTextColor;

                } else {
                    textColor = item.getTextColor();
                }
                if (mSelectDateType.containsKey(type)) {
                    Drawable drawable = mSelectDateType.get(type);
                    if (drawable != null) {
                        setCompoundDrawablesWithIntrinsicBounds(drawable);
                        drawBackground(canvas, drawable, column, row);
                        drawText(canvas, dayStr, textColor, mSelectTextSize, x, y);
                    }
                } else {
                    Log.e(TAG, "需要调用setSelectDayBackground()进行绑定dewable,如果设置setFollowBorderColor=true，通过 HashMap<Integer, Drawable>添加对应的填充颜色");
                }

            } else {

                // 没有则绘制默认背景和文字颜色
//                drawBackground(canvas, mDayBackground, column, row);
                if (year > curYear) {
                    // -------------年份大于当前年份--------------
                    //当前时间之后
                    drawBackground(canvas, null, column, row);
                    drawText(canvas, dayStr, mTextColor, mTextSize, x, y);

                } else if (year == curYear) {
                    //   // -------------年份==当前年份--------------
                    //当前年份--->1、是否大于当前月份
                    //                               2、等于当前月份  ---->同样判断，天
                    if (month > curMoth) {
                        //当前时间之后
                        drawBackground(canvas, null, column, row);
                        drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
                    } else if (month == curMoth) {

                        if (day < curDay) {
                            drawBackground(canvas, mDayBackground, column, row);
                            drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
                        } else if (day == curDay) {
                            //==这里可以自定义当前日期
                            if (todayBackground == null) {
                                todayBackground = mDayBackground;


                            }
                            drawBackground(canvas, todayBackground, column, row);
                            drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
                        } else {
                            //当前时间之后
                            drawBackground(canvas, null, column, row);
                            drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
                        }


                    } else {
                        drawBackground(canvas, mDayBackground, column, row);
                        drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
                    }
                } else {
                    // -------------年份小于当前年份--------------
                    drawBackground(canvas, mDayBackground, column, row);
                    drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
                }

            }

        }
    }

    private void drawBackground(Canvas canvas, Drawable background, int column, int row) {
        if (background != null) {
            canvas.save();
            int dx = (mColumnWidth * column) + (mColumnWidth / 2) - (background.getIntrinsicWidth() / 2);
            int dy = (mRowHeight * row) + (mRowHeight / 2) - (background.getIntrinsicHeight() / 2);
            canvas.translate(dx, dy);
            background.draw(canvas);
            canvas.restore();
        }
    }

    private void drawText(Canvas canvas, String text, @ColorInt int color, float size, int x, int y) {
        mPaint.setColor(color);
        mPaint.setTextSize(size);
        if (mTypeface != null) {
            mPaint.setTypeface(mTypeface);
        }
        canvas.drawText(text, x, y, mPaint);
    }

    private int mDownX = 0, mDownY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClickable()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                int diffX = Math.abs(upX - mDownX);
                int diffY = Math.abs(upY - mDownY);
                if (diffX < mSlop && diffY < mSlop) {
                    int column = upX / mColumnWidth;
                    int row = upY / mRowHeight;
                    onClick(mDays[row][column]);

                }
                break;
            default:
        }
        return super.onTouchEvent(event);
    }

    private int oldSelectDay;

    private void onClick(int day) {

        if (!getSingleClick()) {
            if (day < 1) {
                return;
            }
        }

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        if (mOnDataClickListener != null) {
            final String date = getFormatDate(year, month, day);
            CalendarItem item = isContains(date);
            Log.e(TAG, "onClick: " + date);
            mOnDataClickListener.onDataClick(this, year, month, day, item);
        }
//if (getSingleClick()){
//
//
//}
        if (mIsChangeDateStatus) {

            // 如果选中的天已经选择则取消选中
            final String date = getFormatDate(year, month, day);

            final CalendarItem item = isContains(date);

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
                        return (item.getSelectDataType() * 3);
                    } else {
                        return defaultType;
                    }

                }
            };
            if (mSelectDate != null && (item != null)) {
                if (oldSelectDay != day) {
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

                    //-----
                    mSelectDate_backups.clear(); //清除所有
                    mSelectDate_backups.addAll(mSelectDate);//备份预选信息
                    for (int i = 0; i < mSelectDate.size(); i++) {
                        if (mSelectDate.get(i).getSelectData().equals(date)) {
                            mSelectDate.set(i, t);
                            break;
                        }
                    }

                    if (mChangeListener != null) {
                        mChangeListener.onSelectedDayChange(this, false, year, month, day);
                    }

                }
            } else {
                if (getSingleClick()) {
                    mSelectDate.clear();
                }
                mSelectDate.clear();
                mSelectDate.addAll(mSelectDate_backups);

                mSelectDate.add(t);
                mSelectDateType.put(defaultType, mSelectDayBackground);

                if (mChangeListener != null) {
                    mChangeListener.onSelectedDayChange(this, true, year, month, day);
                }

            }
            oldSelectDay = day;
            invalidate();
        }

    }

    //====================================set 方法设置========================================


    //----------------------------------日历基本设置----------------------------------------

    /**
     * 切换到下一个月.
     */
    public void nextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        invalidate();
    }

    /**
     * 切换到上一个月.
     */
    public void lastMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        invalidate();
    }

    public void setMonth(int month) {
        mCalendar.set(Calendar.MONTH, month - 1);
        invalidate();
    }

    public void setYearAndMonth(int year, int month) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1);
        invalidate();
    }

    public  void setJumpToAppointDay(int day){
        onClick(day);

    }

    //----------------------------------样式设置----------------------------------------


    public void setFollowBorderColor(boolean followBorderColor) {
        this.followBorderColor = followBorderColor;
    }

    public void setHighlightToday(boolean highlightToday) {
        setHighlightToday(highlightToday, "");
    }

    public void setHighlightToday(boolean highlightToday, String todayStr) {
        this.highlightToday = highlightToday;

        if (highlightToday) {

            if (mCalendar != null) {
                onClick(mCalendar.get(Calendar.DAY_OF_MONTH));
            }
            if (todayStr.length()>0 ){
                this.todayStr = todayStr.substring(0, 1);
            }
        }
    }

    /**
     * 当天日期 背景设置
     *
     * @param background
     */
    public void setTodayBackground(Drawable background) {

        if (background != null && todayBackground != background) {
            this.todayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(todayBackground);//--->为什么要甲这句话，这个才是重要的
        }
    }

    /**
     * 设置文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void setTextColor(@ColorInt int textColor) {
        this.mTextColor = textColor;
    }

    /**
     * 设置选中后的的文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void  setSelectTextColor(@ColorInt int textColor) {
        this.mSelectTextColor = textColor;
    }

    /**
     * 设置文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
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
     * 设置选中后的的文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setSelectTextSize(float textSize) {
        this.mSelectTextSize = textSize;
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
     * 设置选择后,天的背景.
     *
     * @param mSelectDateType
     */
    public void setSelectDayBackground(HashMap<Integer, Drawable> mSelectDateType, int defaultType) {
        this.mSelectDateType = mSelectDateType;
        this.defaultType = defaultType;
        mSelectDayBackground = mSelectDateType.get(defaultType);
    }

    //------------------------------------数据设置--------------------------------------


    /**
     * 设置选中的日期数据.
     *
     * @param days 日期数据, 日期格式为 {@link #setDateFormatPattern(String)} 方法所指定,
     *             如果没有设置则以默认的格式 {@link #DATE_FORMAT_PATTERN} 进行格式化.
     */
    public void setSelectDate(List<T> days) {
        this.mSelectDate.addAll(days);
        mSelectDate_backups.addAll(days);
        invalidate();
    }


    /**
     * 设置日期格式化格式.
     *
     * @param pattern 格式化格式, 如: yyyy-MM-dd.
     */
    public void setDateFormatPattern(String pattern) {
        if (!TextUtils.isEmpty(pattern)) {
            this.mDateFormatPattern = pattern;
        } else {
            this.mDateFormatPattern = DATE_FORMAT_PATTERN;
        }
        this.mDateFormat = new SimpleDateFormat(mDateFormatPattern, Locale.CHINA);
    }

    /**
     * 设置点击是否能够改变日期状态 (默认或选中状态).
     * <p>
     * 默认是 false, 即点击只会响应点击事件 {@link OnDataClickListener}, 日期状态而不会做出任何改变.
     *
     * @param isChanged 是否能改变日期状态.
     */
    public void setChangeDateStatus(boolean isChanged) {
        this.mIsChangeDateStatus = isChanged;
    }

    /**
     * 设置日期点击监听.
     *
     * @param listener 被通知的监听器.
     */
    public void setOnDataClickListener(OnDataClickListener listener) {
        this.mOnDataClickListener = listener;
    }

    /**
     * 能否通过点击
     *
     * @param singleClick
     */
    public void setSingleClick(boolean singleClick) {
        SingleClick = singleClick;
    }

    /**
     * 设置选中日期改变监听器.
     *
     * @param listener 被通知的监听器.
     */
    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mChangeListener = listener;
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

    //====================================get ========================================

    //====================================get ========================================

    /**
     * 获取选中的日期数据.
     *
     * @return 日期数据.
     */
    public List<String> getSelectDate() {
        List<String> list = new ArrayList<>();
        int size = mSelectDate.size();
        for (int i = 0; i < size; i++) {
            list.add(mSelectDate.get(i).getSelectData());
        }
        return list;
    }

    /**
     * 获取当前年份.
     *
     * @return year.
     */
    public int getYear() {
        return mCalendar.get(Calendar.YEAR);
    }


    /**
     * 获取当前月份.
     *
     * @return month. (思考后, 决定这里直接按 Calendar 的 API 进行返回, 不进行 +1 处理)
     */
    public int getMonth() {
        return mCalendar.get(Calendar.MONTH);
    }

    /**
     * 设置当前显示的 Calendar 对象.
     *
     * @param calendar 对象.
     */
    public void setCalendar(Calendar calendar) {
        this.mCalendar = calendar;
        invalidate();
    }

    /**
     * 获取当前显示的 Calendar 对象.
     *
     * @return Calendar 对象.
     */
    public Calendar getCalendar() {
        return mCalendar;
    }

    /**
     * 获取日期格式化格式.
     *
     * @return 格式化格式.
     */
    public String getDateFormatPattern() {
        return mDateFormatPattern;
    }

    /**
     * 获取 {@link Paint} 对象.
     *
     * @return {@link Paint}.
     */
    public Paint getPaint() {
        return mPaint;
    }

    /**
     * 获取是否能改变日期状态.
     *
     * @return {@link #mIsChangeDateStatus}.
     */
    public boolean getChangeDateStatus() {
        return mIsChangeDateStatus;
    }

    /**
     * 根据指定的年月日按当前日历的格式格式化后返回.
     *
     * @param year  年.
     * @param month 月.
     * @param day   日.
     * @return 格式化后的日期.
     */
    public String getFormatDate(int year, int month, int day) {
        mSelectCalendar.set(year, month, day);
        return mDateFormat.format(mSelectCalendar.getTime());
    }

    /**
     * 通过指定的年份和月份获取当月有多少天.
     *
     * @param year  年.
     * @param month 月.
     * @return 天数.
     */
    private int getMonthDays(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 获取指定年月的 1 号位于周几.
     *
     * @param year  年.
     * @param month 月.
     * @return 周.
     */
    private int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 0);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public boolean getSingleClick() {
        return SingleClick;
    }

    public boolean getFollowBorderColor() {
        return followBorderColor;
    }

    public boolean getHighlightToday() {
        return highlightToday;
    }

    public Drawable getTodayBackground() {
        return todayBackground;
    }

    //======================通用方法===================

    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getContext().getResources().getDisplayMetrics());
    }

    private CalendarItem isContains(String date) {
        if (mSelectDate == null) {
            return null;
        } else {
            for (int i = 0; i < mSelectDate.size(); i++) {
                CalendarItem item = mSelectDate.get(i);
                if (item.getSelectData().contains(date)) {
                    return item;
                }
            }
            return null;
        }
    }

}

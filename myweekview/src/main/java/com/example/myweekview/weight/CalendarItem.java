package com.example.myweekview.weight;

/**
 * Created by Administrator on 2018/4/1.
 */

public interface CalendarItem {

    /**显示的日期
     * @return 默认格式:20180401,或者在调用mCalendarView.setDateFormatPattern("yyyy-MM-dd")进行格式化
     */
    public String getSelectData();

    /**显示的背景类型
     * @return ,注意不是背景,默认为0
     */
    public int getSelectDataType();

    public  int getTextColor();

    public boolean beforeSelectItem();




}

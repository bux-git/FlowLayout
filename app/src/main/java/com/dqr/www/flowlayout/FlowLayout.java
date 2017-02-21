package com.dqr.www.flowlayout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description：流式布局
 * Author：LiuYM
 * Date： 2017-02-21 14:43
 */

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width=0;//wrap_content时 计算出的宽
        int height=getPaddingTop()+getPaddingBottom();//计算出的高

        int lineHeight=0;//记录单行最大高
        int lineWidth=getPaddingLeft()+getPaddingRight();//记录单行宽

        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            if(child.getVisibility()==GONE)continue;

            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams params= (MarginLayoutParams) child.getLayoutParams();
            int childWidth=child.getMeasuredWidth()+params.leftMargin+params.rightMargin;
            int childHeight=child.getMeasuredHeight()+params.topMargin+params.bottomMargin;

            if(childWidth+lineWidth<=widthSize){
                lineWidth +=childWidth;//累积行宽
                lineHeight=Math.max(lineHeight,childHeight);//最大高

            }else{//换行处理 处理上一行记录数据
                width=Math.max(width,lineWidth);//最大宽
                height+=lineHeight;//累积高

                //重置数据
                lineHeight=childHeight;
                lineWidth=childWidth+getPaddingLeft()+getPaddingRight();
            }

            if(i==childCount-1){//处理最后一个
                width=Math.max(width,lineWidth);//最大宽
                height+=lineHeight;//累积高
            }
        }
        setMeasuredDimension(widthMode==MeasureSpec.EXACTLY?widthSize:width,
                heightMode==MeasureSpec.EXACTLY?heightSize:height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int mayUse=getMeasuredWidth()-getPaddingRight()-getPaddingLeft();

        int height=getPaddingTop();//记录从上往下已经使用的高

        int width=getPaddingLeft();//记录从左往右单行已经使用的宽
        int lineHeight=0;//单行最高

        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            if(child.getVisibility()==GONE)continue;

            MarginLayoutParams params= (MarginLayoutParams) child.getLayoutParams();
            int childWidth=child.getMeasuredWidth();
            int childHeight=child.getMeasuredHeight();

            int marginLR=params.leftMargin+params.rightMargin;
            int marginTB=params.topMargin+params.bottomMargin;

            int left;
            int top;
            int right;
            int bottom;

            if(childWidth+marginLR+width<=mayUse){
                //计算左上角坐标
                left=width+params.leftMargin;
                top=height+params.topMargin;

                width+=childWidth+marginLR;//累加
                lineHeight=Math.max(lineHeight,childHeight+marginTB);//最高


            }else{//换行 新行
                height+=lineHeight;

                //计算左上角坐标
                left=getPaddingLeft()+params.leftMargin;
                top=height+params.topMargin;

                //重置
                width=getPaddingLeft()+childWidth+marginLR;
                lineHeight=childHeight+marginTB;
            }

            //计算右下角坐标
            right=left+childWidth;
            bottom=top+childHeight;

            child.layout(left,top,right,bottom);
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    }
}

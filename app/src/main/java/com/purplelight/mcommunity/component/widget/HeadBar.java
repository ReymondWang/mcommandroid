package com.purplelight.mcommunity.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.purplelight.mcommunity.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 顶部工具栏
 * Created by wangyn on 16/4/9.
 */
public class HeadBar extends RelativeLayout {

    @InjectView(R.id.btnLeftHeadButton) ImageButton btnLeftHeadButton;
    @InjectView(R.id.btnRightHeadButton) ImageButton btnRightHeadButton;
    @InjectView(R.id.imgHeadCenter) ImageView imgHeadCenter;
    @InjectView(R.id.txtHeadCenter) TextView txtHeadCenter;

    private HeadButtonClickedListener mLeftHeadButtonClickedListener, mRightHeadButtonClickedListener;

    public HeadBar(Context context) {
        this(context, null);
    }

    public HeadBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
        initFromAttributeSet(context, attrs);
    }

    public interface HeadButtonClickedListener{
        void onHeadButtonClicked();
    }

    public void setLeftHeadButtonEvent(HeadButtonClickedListener onLeftHeadButtonClickedListener){
        if (onLeftHeadButtonClickedListener != null){
            mLeftHeadButtonClickedListener = onLeftHeadButtonClickedListener;
            btnLeftHeadButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLeftHeadButtonClickedListener.onHeadButtonClicked();
                }
            });
        }
    }

    public void setRightHeadButtonEvent(HeadButtonClickedListener onRightHeadButtonClickedListener){
        if (onRightHeadButtonClickedListener != null){
            mRightHeadButtonClickedListener = onRightHeadButtonClickedListener;
            btnRightHeadButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRightHeadButtonClickedListener.onHeadButtonClicked();
                }
            });
        }
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.item_head_bar, this);
        ButterKnife.inject(this);
    }

    private void initFromAttributeSet(Context context, AttributeSet attrs){
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PurpleLight);
        Drawable leftButtonIcon = a.getDrawable(R.styleable.PurpleLight_leftButtonIcon);
        Drawable rightButtonIcon = a.getDrawable(R.styleable.PurpleLight_rightButtonIcon);
        Drawable centerImage = a.getDrawable(R.styleable.PurpleLight_centerImage);
        String centerTitle = a.getString(R.styleable.PurpleLight_centerTitle);

        if (leftButtonIcon != null){
            btnLeftHeadButton.setVisibility(View.VISIBLE);
            btnLeftHeadButton.setImageDrawable(leftButtonIcon);
        } else {
            btnLeftHeadButton.setVisibility(View.GONE);
        }

        if (rightButtonIcon != null){
            btnRightHeadButton.setVisibility(View.VISIBLE);
            btnRightHeadButton.setImageDrawable(rightButtonIcon);
        } else {
            btnRightHeadButton.setVisibility(View.GONE);
        }

        if (centerImage != null){
            imgHeadCenter.setVisibility(View.VISIBLE);
            imgHeadCenter.setImageDrawable(centerImage);
        } else {
            imgHeadCenter.setVisibility(View.GONE);
        }

        txtHeadCenter.setText(centerTitle);

        a.recycle();
    }

}

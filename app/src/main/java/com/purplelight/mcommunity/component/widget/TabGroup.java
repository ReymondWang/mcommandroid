package com.purplelight.mcommunity.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.util.ConvertUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 首页底部导航栏
 * Created by wangyn on 16/4/7.
 */
public class TabGroup extends LinearLayout {

    @InjectView(R.id.community) LinearLayout mCommunity;
    @InjectView(R.id.work) ViewGroup mWork;
    @InjectView(R.id.contact) LinearLayout mContact;
    @InjectView(R.id.profile) LinearLayout mProfile;
    @InjectView(R.id.count) TextView mCount;

    private int[] imageRes;
    private int[] imageResActive;
    {
        imageRes = new int[] {R.drawable.ic_community_normal
                , R.drawable.ic_work_normal
                , R.drawable.ic_contact_normal
                , R.drawable.ic_profile_normal};

        imageResActive = new int[] {R.drawable.ic_community_select
                , R.drawable.ic_work_select
                , R.drawable.ic_contact_select
                , R.drawable.ic_profile_select};
    }

    private int mIndex;
    private ViewGroup[] mItems;

    private final int mLabelColor;
    private final int mLabelColorSelected;

    private TabSelectListener mTabSelectListener;

    public TabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.item_tab_group, this);
        ButterKnife.inject(this);

        mLabelColor = getResources().getColor(R.color.dark_gray);
        mLabelColorSelected = getResources().getColor(R.color.color_primary);
        mItems = new ViewGroup[] {mCommunity, mWork, mContact, mProfile};

        setBackgroundResource(R.drawable.bottom_bar);
        updateSelected(0);

        for(int i = 0; i < mItems.length; i++){
            ViewGroup item = mItems[i];
            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (index != getIndex()) {
                        if (mTabSelectListener != null) {
                            if (mTabSelectListener.onTabSelected(index)) {
                                updateSelected(index);
                            }
                        }
                    }
                }

            });
        }
    }

    public void updateSelected(int index){
        mIndex = index;
        ViewGroup item = mItems[mIndex];
        new TabGroup.ViewHolder(item).setSelected(index, true);
        for(int i = 0; i < mItems.length; i++) {
            if(i == mIndex) {
                continue;
            }
            ViewGroup viewGroup = mItems[i];
            new TabGroup.ViewHolder(viewGroup).setSelected(i, false);
        }
    }

    public void setOnTabChangeListener(TabGroup.TabSelectListener tabSelectListener){
        mTabSelectListener = tabSelectListener;
    }

    public int getIndex(){
        return mIndex;
    }

    public void setIndex(int index){
        mIndex = index;
    }

    public ViewGroup getWork(){
        return mWork;
    }

    public void setWorkCount(int workCount){
        mCount.setVisibility(workCount == 0 ? View.GONE : View.VISIBLE);
        mCount.setText(String.valueOf(workCount));
    }

    public int getWorkCount(){
        return ConvertUtil.ToInt(mCount.getText().toString());
    }

    public interface TabSelectListener{
        boolean onTabSelected(int index);
    }

    class ViewHolder{
        @InjectView(R.id.icon)
        ImageView mIcon;
        @InjectView(R.id.label)
        TextView mLabel;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }

        public void setSelected(int index, boolean selected){
            if (selected){
                mIcon.setImageResource(imageResActive[index]);
                mLabel.setTextColor(mLabelColorSelected);
            }else{
                mIcon.setImageResource(imageRes[index]);
                mLabel.setTextColor(mLabelColor);
            }
        }
    }
}

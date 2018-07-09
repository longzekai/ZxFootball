package com.parsonswang.zxfootball.common.view;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parsonswang.common.image.GlideImageLoader;
import com.parsonswang.common.image.Imageloaders;
import com.parsonswang.common.utils.UIUtils;
import com.parsonswang.common.view.MarqueTextView;
import com.parsonswang.zxfootball.R;
import com.parsonswang.zxfootball.bean.MatchTimelines;
import com.parsonswang.zxfootball.bean.PlayerInfo;
import com.parsonswang.zxfootball.common.utils.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 比赛球员信息view
 */
public class MatchPlayerView extends FrameLayout {

    private ImageView mIvPlayer;
    private MarqueTextView mTvPlayernName;
    private LinearLayout mEventContainer;

    private Context mContext;

    public MatchPlayerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MatchPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_match_player, null, false);
        mIvPlayer = view.findViewById(R.id.iv_player);
        mTvPlayernName = view.findViewById(R.id.tv_playername);
        mEventContainer = view.findViewById(R.id.event_container);

        addView(view);
    }

    public void setData(PlayerInfo playerInfo, HashMap<String, ArrayList<MatchTimelines>> matchTimelines,
                        SparseIntArray timeLineEventResMap) {
        ImageUtils.loadMatchPlayerAvatar(mContext, mIvPlayer, playerInfo.avatarUrl);

        String name = playerInfo.playerName;
        if (name.contains("·") && name.split("·").length > 1) {
            name = name.split("·")[1];
        } else if (name.contains("-") && name.split("-").length > 1) {
            name = name.split("-")[1];
        }
        mTvPlayernName.setText(name);

        final ArrayList<MatchTimelines> matchTimelineList= matchTimelines.get(playerInfo.playerId);
        if (matchTimelineList != null && !matchTimelineList.isEmpty()) {
            for (MatchTimelines matchTimeline : matchTimelineList) {
                int res = timeLineEventResMap.get(matchTimeline.eventType);
                ImageView imageView = new ImageView(getContext());
                imageView.setBackgroundResource(res);
                mEventContainer.addView(imageView);

                final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mEventContainer.getLayoutParams();
                layoutParams.width = UIUtils.dip2px(getContext(), 10);
                layoutParams.height = UIUtils.dip2px(getContext(), 10);
            }
        }

    }
}

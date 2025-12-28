package com.example.tepinhui.ui.community;

import com.example.tepinhui.R;

/**
 * 头像工具：实现“随机但固定”的头像分配（同一个 userName 永远映射同一张本地头像）
 */
public class AvatarUtil {
    private static final int[] AVATARS = new int[] {
            R.drawable.avatar_1,
            R.drawable.avatar_2,
            R.drawable.avatar_3,
            R.drawable.avatar_4,
            R.drawable.avatar_5,
            R.drawable.avatar_6
    };

    public static int forUser(String userName) {
        if (userName == null) return AVATARS[0];
        int idx = Math.abs(userName.hashCode()) % AVATARS.length;
        return AVATARS[idx];
    }
}



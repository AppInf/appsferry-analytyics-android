package com.appsferry.analytics.a.j;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Do not modify its pkg path.
 */
public class a {

    static {
        System.loadLibrary("a");
    }

    public static native void b(@NonNull Context a, @NonNull String b);

    @SuppressWarnings("all")
    @Nullable
    public static native String a(long a, @NonNull String b, @NonNull String c);

}

package cn.mealkey.mkservice.common.functions;

import android.view.View;

public interface Func1<T1, R> {
    R call(T1 value);

    View call();
}

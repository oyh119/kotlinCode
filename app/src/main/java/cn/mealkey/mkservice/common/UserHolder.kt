package cn.mealkey.mkservice.common

import android.text.TextUtils
import javax.inject.Inject

class UserHolder @Inject constructor() {
    var regions: String  = ""
        get() {
            if (TextUtils.isEmpty(field)) {
                field = sharedPreferences().getString("app_RegionIds", "")
            }
            return field
        }
        set(value) {
            field = value
            sharedPreferences().edit{
                putString("app_RegionIds", value)
            }
        }

}
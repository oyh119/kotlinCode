package cn.mealkey.mkservice.common

import android.text.TextUtils
import javax.inject.Inject

/**
 * Created by wuhaowen on 2017/7/12.
 */
class StoreHolder @Inject constructor() {
    var tenantName: String  = ""
        get() {
            if (TextUtils.isEmpty(field)) {
                field = sharedPreferences().getString("app_TenantName", "")
            }
            return field
        }
        set(value) {
            field = value
            sharedPreferences().edit{
                putString("app_TenantName", value)
            }
        }

    var storeName: String = ""
        get() {
            if (TextUtils.isEmpty(field)) {
                field = sharedPreferences().getString("app_StoreName", "")
            }
            return field
        }
        set(value) {
            field = value
            sharedPreferences().edit{
                putString("app_StoreName", value)
            }
        }

    var storeId: Long = -1L
        get() {
            if (field == -1L) {
                field = sharedPreferences().getLong("app_StoreId", -1L)
            }
            return field
        }
        set(value) {
            field = value
            sharedPreferences().edit {
                putLong("app_StoreId", field)
            }
        }

    var tenantId: Long = -1L
        get() {
            if (field == -1L) {
                field = sharedPreferences().getLong("app_TenantId", -1L)
            }
            return field
        }
        set(value) {
            field = value
            sharedPreferences().edit {
                putLong("app_TenantId", field)
            }
        }

    var typeId: Int = -1
        get() {
            if (field == -1) {
                field = sharedPreferences().getInt("app_TypeId", -1)
            }
            return field
        }
        set(value) {
            field = value
            sharedPreferences().edit {
                putInt("app_TypeId", field)
            }
        }


    var sessionId: String = ""
        get() {
            if (TextUtils.isEmpty(field)) {
                field = sharedPreferences().getString("app_sessionId", "")
            }
            return field
        }
        set(value) {
            field = value
            sharedPreferences().edit {
                putString("app_sessionId", field)
            }
        }

}

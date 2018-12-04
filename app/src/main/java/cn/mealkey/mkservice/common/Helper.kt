package cn.mealkey.mkservice.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import cn.mealkey.mkservice.MKAplication
import com.ta.utdid2.device.UTDevice

/**
 * Created by wuhaowen on 2018/3/22.
 */
fun deviceUid() = UTDevice.getUtdid(MKAplication.instance)!!

fun sharedPreferences() = MKAplication.instance.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)!!

inline fun SharedPreferences.edit(block:SharedPreferences.Editor.()->Unit) = edit().also{block(it)}.apply()

@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.editSync(block:SharedPreferences.Editor.()->Unit) = edit().also{block(it)}.commit()

//inline fun <reified T> SharedPreferences.get(key :String,default:T):T? =
//    when(T::class){
//        Long::class -> getLong(key, default as Long) as T
//        Int::class -> getInt(key,default as Int) as T
//
//        else->null
//    }

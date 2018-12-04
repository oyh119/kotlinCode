package cn.mealkey.mkservice.apis

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class ComboDish(
        var dishId: Long = 0,    //	菜品ID
        var dishName: String,  //	菜品名称
        var dishAliasName: String,   //	菜品别名
        var picPath: String,   //	菜品图片
        var largePicpath: String,   //	菜品大图
        var dishTypeId: Long,   //	菜品类型ID
        var pinyin: String,   //	拼音
        var dishTypeName: String,   //	菜品类型简称
        var price: Double,    //	价格
        var unitName: String,    //	单位名称
        var memberPrice: Double,    //	会员价格
        var minQuantity: Int = 0,  //	最小点菜数量
        var count: Int = 0,  //	剩余数量：0已售罄,-1表示无限制
        var typeId: Long = 0,  //	菜品分类ID
        var typeName: String,  //	菜品分类名称
        var isSelfhelp: Boolean = false,  //	是否自助点菜
        var blurredPicture: String,  //	模糊图片地址
        var pungencyDegree: Int = 0, //	辣度
        var description: String,   //	菜品描述
        var processingCharge: Double,  //	加工费
        var tag: String,  //	菜品标签-保留字段
        var isHasDiet: Boolean = false,   //	是否有忌口
        var isNeedRecommend: Boolean = false,   //	是否参与推荐菜获取
        var recommendType: String,  //	获取推荐菜，取为2
        var eatCount: Int = 0,  //	点餐数量
        var isRecommend: Boolean = false,   //	是否推荐菜品
        var optionNum: Int = 0,   //	可选数量
        var dishTasteId: Long = 0,//味型ID
        var showPrice: String,
        var showMemberPrice: String,
        var showComboPrice: String,
        var showComboMemberPrice: String,
        var tasteNames: String,
        var tasteId: String,
        var methoadName: String,
        var customTaste: String
):Parcelable

@Parcelize
data class ComboClass(
        var itemGroupId: Long = 0,   //	套餐类别ID
        var itemGroupName: String? = null,  //	套餐类别名称
        var isRequired: Boolean = false,   //	是否必选
        var isRepeatableDish: Boolean = false,   //	是否可以选择多份
        var maxChooseNum: Int = 0,   //	最大选择份数
        var minChooseNum: Int = 0,   //	最小选择份数
        var selectNum: Int = 0,
        var comboDishList: List<ComboDish>? = null    //[ComboDishResponse]	套餐类别菜品集合
):Parcelable

@Parcelize
data class DishMthod (
    var id: Long = 0 ,   //	主键ID(口味ID)
    var mainMethodId: Long ,  //	味型主菜品ID
    var methoadName: String? = null    ,//	味型名称
    var price: Double,   //	价格
    var memberPrice: Double,    //	会员价格
    var processingCharge: Double   //	加工费
):Parcelable

@Parcelize
data class Dish(
    var typeId: Long = 0,
    var typeName: String,
    var dishId: Long = 0,
    var dishName: String,
    var picPath: String,
    var dishTypeId: Long = 0,
    var dishTypeName: String,
    var pinyin: String,
    var praises: Int = 0,
    var minQuantity: Int = 0,// integer  最小点菜数量
    var price: Double,
    var memberPrice: Double ,
    var tableId: Long = 0,
    var dishTasteId: Long = 0,//味型ID
    var showPrice: String,
    var showMemberPrice: String,
    var showCount: String,
    var showComboPrice: String,
    var showComboMemberPrice: String,
    var tasteId: String,
    var tasteNames: String,
    var isDefaultDish: Boolean = false,
    var count: Int = 0,
    var unitName: String,
    var eatCount:Int = 0,
    var mealId: Long = 0,//点餐平板需要
    var orderTime: Long = 0,//点菜时间,用于排序 最后点的在最前面
    var isCombo: Boolean = false,//是否套餐
    var isAutoPrice: Boolean = false,//是否固定价格
    var isSellout: Boolean = false,//是否售罄
    var comboType: Long = 0,//		套餐类型（1非套餐，2固定套餐，3自选套餐）
    var showTypeNumName: String,    //	展示荤素值
    var dishAliasName: String ,   //	菜品别名
    var processingCharge: Double,//	加工费
    var dishMethodTypeList: List<DishMthod>? = null,    //[菜品味型]	味型集合
    var comboDishTypeList: List<ComboClass>? = null   ,   //[ComboDishTypeResponse]	套餐类别集合
    var isOpen: Boolean = false,
    var flavorTypeName: String,
    var customTaste: String,
    var isWeigh: Boolean = false,
    var minWeighQuantity: Double,
    var maxWeighQuantity: Double ,
    var weighQuantity:Double = 0.0
):Parcelable

@Parcelize
data class MealClass (
    var typeId: Long = 0,
    var typeName: String,
    var dishList: ArrayList<Dish>? = null
):Parcelable

data class DishMenu (
    var typeList: List<MealClass>? = null,
    var dishNum: Int = 0,
    var orderStatus: Int = 0,
    var isSuspend: Int = 0,
    var isFreeSingle: Boolean = false//是否是有免单
)


data class TasteInfo (
    var tasteId: Long = 0,// integer  口味ID {"tasteId":0,"tasteName":"string"}
    var tasteName: String,// string  口味名称
    var isChecked:Boolean = false

)

data class AllTasteInfo (
    var tasteList: List<TasteInfo>,
    var customTaste: String
)

data class OrderSyn (
    var isSynergyOrder: Boolean = false
)

data class DefaultDish(
    var dishId: Long = 0,
    var dishName: String,
    var dishAliasName: String,
    var price: BigDecimal,
    var memberPrice: BigDecimal,
    var minQuantity: Int = 0,
    var unitName: String,
    var dishTypeName: String,
    var processingCharge: BigDecimal,
    var eatCount: Int = 0
)


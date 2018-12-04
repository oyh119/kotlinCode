package cn.mealkey.mkservice.apis

/**
 * Created by wuhaowen on 2018/3/22.
 */


data class Activation(var tenantId: Long = 0,
                      var storeId: Long = 0,
                      var storeName: String,
                      var activateDate: String? = null,
                      var sessionId: String,
                      var tenantName: String = "",
                      var isUsedCookingStation: Boolean = false,
                      var typeId: Int = 0)

data class Waiter(var roaderId: Long = 0,
                  var roaderName: String)

data class WaiterWraper(var roaderInfoList: List<Waiter>, var noRoaderInfoList: List<Waiter>)

//登陆请求参数
data class InitLoginResq constructor(var storeId: Long,
                         var tenantId: Long,
                         var employeeCode: String,
                         var password: String,
                         var noEmployeeCard: Boolean,
                         var padIdCode: String,
                         var employeeIdCode: String

)

data class LockList(
        var clientTypeId: Int = 0,
        var lockScreenTime: Long = 0
)







//登陆返回结果
data class InitLoginResp(
        var userId: Long = 0,
        var userName: String,
        var employeeCode: String,
        var loginDate: String,
        var userMenuList: List<String>,
        var lockList: List<LockList>,
        var sessionId: String,
        var pattern: Int = 0)

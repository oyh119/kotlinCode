package cn.mealkey.mkservice.business.order

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v13.app.FragmentPagerAdapter
import android.view.Menu
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Table
import cn.mealkey.mkservice.base.view.BaseMvpActivity

import kotlinx.android.synthetic.main.activity_order_dishes.*
import android.view.MenuItem
import android.widget.SearchView
import cn.mealkey.mkservice.apis.MealClass
import cn.mealkey.mkservice.node.model.NodeDish
import kotlinx.android.synthetic.main.content_order_dishes.*
import javax.inject.Inject
import cn.mealkey.mkservice.apis.Dish


class OrderDishesActivity : BaseMvpActivity(), OrderDishesContract.View ,DishFragment.OnDishFragmentListener{
    lateinit var table:Table
    var mealId = 0L

    @Inject lateinit var presenter: OrderDishesContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_dishes)
        setSupportActionBar(toolbar)
        with(intent){
            table = getParcelableExtra("table")
            mealId = table.mealId
        }
        title = String.format(resources.getString(R.string.order_dishes), table.tableName)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        presenter.init(mealId = mealId, tableId = table.tableId, error = {}){
            dishMenu, allTasteInfo ->
            run {
                initViewPager(dishMenu?.typeList)

            }
        }
    }


    fun initViewPager(mealClasses: List<MealClass>?){
        mealClasses?.run {
            val fragments = mutableListOf<DishFragment>()
            for (i in 0 until size){
                val fragment = DishFragment.newInstance(dishes = this[i].dishList)
                fragments.add(fragment)
            }
            view_pager.adapter = object :FragmentPagerAdapter(fragmentManager){

                override fun getItem(position: Int): Fragment = fragments[position]


                override fun getCount() = fragments.size

                override fun getPageTitle(position: Int): CharSequence = this@run[position].typeName
            }

            tab_class.setupWithViewPager(view_pager)



        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_order, menu)
        val menuItem = menu.findItem(R.id.action_search)//在菜单中找到对应控件的item
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "请输入菜品首字母搜索"
        menuItem.setOnActionExpandListener(object :MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                return true
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDishClick(item: Dish) {
        if ((item.dishMethodTypeList != null && !item.dishMethodTypeList!!.isEmpty()) || item.isWeigh) {
            OrderMethodWeighDialog(this,item).show()
        }else if (item.comboType > 2){
            item.comboDishTypeList?.let {
                if (it.size == 1){
                    OrderComboFixedFragment.newInstance(dish = item).show(fragmentManager,"combo_fixed_dialog")

                }else{
                    OrderComboFragment.newInstance(dish = item).show(fragmentManager,"combo_dialog")


                }
            }
        }
    }

    override fun onDishesCountChanged(count: Int) {
    }

    override fun onDishesPriceSum(price: String) {
    }

    override fun onAllDishesChanged(dishes: List<NodeDish>) {
    }

    override fun onGlobalTaste(tastes: String) {
    }
}

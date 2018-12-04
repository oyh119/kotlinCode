package cn.mealkey.mkservice.business.table

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Region
import cn.mealkey.mkservice.apis.Table
import cn.mealkey.mkservice.base.view.BaseMvpActivity
import cn.mealkey.mkservice.business.order.OrderDishesActivity
import cn.mealkey.mkservice.common.UserHolder
import cn.mealkey.mkservice.common.logd
import cn.mealkey.mkservice.common.msg
import kotlinx.android.synthetic.main.activity_region.*
import org.jetbrains.anko.toast
import javax.inject.Inject
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuParams
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener
import org.jetbrains.anko.startActivity


class RegionActivity : BaseMvpActivity(), RegionContract.View, RegionsDialogFragment.Listener,
        TableStateDialogFragment.Listener, OnMenuItemClickListener,
        PersonNumDialogFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var presenter: RegionContract.Presenter
    @Inject
    lateinit var userHolder: UserHolder


    lateinit var mMenuDialogFragment: ContextMenuDialogFragment
    lateinit var adapter: RegionAdapter

    var regions: ArrayList<Region>? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region)
        setSupportActionBar(toolbar)
        rcyRegion.layoutManager = GridLayoutManager(this, 2)
        adapter = RegionAdapter(object : RegionAdapterListener {
            override fun handleTable(table: Table, type: Int) {
                when (type) {
                    TYPE_CLICK_OPEN -> {
                        PersonNumDialogFragment.newInstance(table, TYPE_CLICK_OPEN).show(supportFragmentManager, "personNumDialog")
                    }
                    TYPE_CLICK_OPEN_ORDER -> {

                        PersonNumDialogFragment.newInstance(table, TYPE_CLICK_OPEN_ORDER).show(supportFragmentManager, "personNumDialog")
                    }

                    TYPE_CLICK_ORDER -> startActivity<OrderDishesActivity>("table" to table)

                }

            }
        })
        rcyRegion.adapter = adapter

        btnRegionSelect.setOnClickListener {
            findRegions()
        }

        btnRefreshTables.setOnClickListener {
            findRegionTables(userHolder.regions)
        }

        btnFilterTables.setOnClickListener {
            TableStateDialogFragment.newInstance().show(supportFragmentManager, "tableState")
        }

        val styleAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val height = styleAttributes.getDimensionPixelSize(0, 0)
        styleAttributes.recycle()

        val menuParams = MenuParams()
        menuParams.actionBarSize = height
        menuParams.menuObjects = getMenuObjects()
        menuParams.animationDuration = 80
        menuParams.isClosableOutside = true
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams)

    }

    override fun onResume() {
        super.onResume()
        if (userHolder.regions.isEmpty()) findRegions() else findRegionTables(userHolder.regions)
    }


    fun findRegions() {
        regions?.let {
            RegionsDialogFragment.newInstance(it).show(supportFragmentManager, "regionsSheet")
        } ?: presenter.findRegions({
            toast(it.msg())
        }) {
            regions = ArrayList(it)
            regions?.let { RegionsDialogFragment.newInstance(it).show(supportFragmentManager, "regionsSheet") }

        }
    }

    fun findRegionTables(regionIds: String, state: Int = 0) {
        presenter.findTables(regionIds, state, {
            toast(it.msg())
        }) {
            adapter.tables = it
            adapter.notifyDataSetChanged()
        }

    }


    override fun onRegionClicked(position: Int) {
        logd(position.toString())
        regions?.let {
            val region = it[position]
            userHolder.regions = region.regionId.toString()
            findRegionTables(userHolder.regions)

        }
    }

    override fun onTableStateClicked(position: Int) {
        findRegionTables(userHolder.regions, position)
    }


    override fun onNumInput(num: Int, openFor: Int, table: Table) {
        presenter.openTable(table.tableId, num, {
            toast(it.msg())
        }) {
            if (openFor == TYPE_CLICK_OPEN) findRegionTables(userHolder.regions)
            else {
                table.mealId = it.mealId
                startActivity<OrderDishesActivity>("table" to table)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.table_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_menu) {
            mMenuDialogFragment.show(supportFragmentManager, "ContextMenuDialogFragment")

        }
        return super.onOptionsItemSelected(item)

    }

    override fun onMenuItemClick(p0: View?, p1: Int) {

    }

    private fun getMenuObjects(): List<MenuObject> {
        val vip = MenuObject("会员中心")
        vip.resource = R.drawable.ic_vip

        val settlement = MenuObject("反结算")
        settlement.resource = R.drawable.ic_settlement

        val close = MenuObject("注销登陆")
        close.resource = R.drawable.ic_logout
        return listOf(vip, settlement, close)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}

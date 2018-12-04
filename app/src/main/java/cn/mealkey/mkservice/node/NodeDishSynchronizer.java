package cn.mealkey.mkservice.node;

import android.annotation.SuppressLint;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wuhaowen.rxsocketio.RxSocketIO;

import org.json.JSONArray;
import org.json.JSONObject;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.mealkey.mkservice.node.model.NodeAction;
import cn.mealkey.mkservice.node.model.NodeDish;
import cn.mealkey.mkservice.node.model.NodeSetDish;
import cn.mealkey.mkservice.node.model.NodeTaste;
import cn.mealkey.mkservice.node.model.RealmDish;
import cn.mealkey.mkservice.node.model.RealmGlobalTaste;
import cn.mealkey.mkservice.node.model.RealmSetDish;
import cn.mealkey.mkservice.node.model.RealmTaste;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.socket.client.IO;
import io.socket.client.Socket;

import static cn.mealkey.mkservice.node.model.NodeAction.TYPE_ADD;
import static cn.mealkey.mkservice.node.model.NodeAction.TYPE_DECREASING;
import static cn.mealkey.mkservice.node.model.NodeAction.TYPE_INCREASING;
import static cn.mealkey.mkservice.node.model.NodeAction.TYPE_MODIFY;


public class NodeDishSynchronizer {

    public final static int ACTION_PLUS = 1;
    public final static int ACTION_MINUS = 2;
    public final static int ACTION_SPECIFIC = 3;

    Gson gson = new Gson();


    public static class Option {
        public String tableName;
        public long tableId;
        public boolean synergyOrder;
        public long mealId;
        public long storeId;
        public String userId;
        public String userName;
        public boolean sortByFrom;
        public boolean observeMyDishesChanged;
        public boolean observeDishesChangedExceptMy;
        public int form;
    }

    final String NODE_TAG = "[SYNC NODE SERVER] ";
    final String REALM_TAG = "[SYNC REALM DB] ";
    private static final String RECOVER_DISHES_ACTION = "RECOVER_DISHES_ACTION";
    private static final String CHANGE_DISHES_ACTION = "CHANGE_DISHES_ACTION";
    private static final String CHANGE_DISHES_FOR_SELF_ACTION = "CHANGE_DISHES_FOR_SELF_ACTION";
    private static final String CHANGE_GLOBAL_TASTES_ACTION = "CHANGE_GLOBAL_TASTES_ACTION";
    private static final String PAY_COMPLETE_ACTION = "PAY_COMPLETE_ACTION";
    //Gson gson = new GsonBuilder().serializeNulls().create();

    Option option;

    Realm mainRealm;
    Socket mSocket;

    NodeAllDishesChangeListener allDishesChangeListener;
    NodeMyDishesChangeListener myDishesChangeListener;
    NodeOthersDishesChangedListener nodeOthersDishesChangedListener;
    NodeGlobalTasteChangedListener globalTasteChangedListener;

    @Inject
    public NodeDishSynchronizer() {
        mainRealm = Realm.getDefaultInstance();
    }

    public final void start(Option option) {
        this.option = option;
        observeRealmDishes(option.sortByFrom);

        if (option.synergyOrder) {
            StringBuilder sb = new StringBuilder("https://pre.mealkey.net" + "?");
            sb = sb.append("mealId=").append(option.mealId)
                    .append("&storeId=").append(option.storeId)
                    .append("&tableName=").append(option.tableName)
                    .append("&tableId=").append(option.tableId)
                    .append("&userName=").append(option.userName)
                    .append("&userId=").append(option.userId)
                    .append("&client=").append(option.form);

            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            try {
                mSocket = IO.socket(sb.toString(), opts);
            } catch (Exception e) {
                //logger.error("{}连接失败", NODE_TAG);
            }
            if (mSocket == null)
                return;
            mSocket.on(Socket.EVENT_CONNECT, args -> {}
                    //logger.debug("{} socket id:{}", NODE_TAG, mSocket.id())
            );
            mSocket.on(Socket.EVENT_DISCONNECT, args -> {}
                    //logger.debug("{} socket disconnect id:{}", NODE_TAG, mSocket.id())
            );
            observeNodeDishes();
            mSocket.connect();
        }

    }

    @SuppressLint("CheckResult")
    private void observeRealmDishes(boolean sortByFrom) {
        mainRealm.setAutoRefresh(true);

        RealmQuery<RealmGlobalTaste> globalTastesQuery = mainRealm.where(RealmGlobalTaste.class)
                .equalTo("mealId", option.mealId);
        globalTastesQuery.findAll().asFlowable().filter(RealmResults::isLoaded)
                .subscribe(realmTastes -> {
                    List<NodeTaste> tastes = new LinkedList<>();
                    for (int i = 0; i < realmTastes.size(); i++) {
                        tastes.add(realmTastes.get(i).nodeTaste());
                    }
                    //logger.debug("{} 全局忌口发生变化 {}", REALM_TAG, tastes);
                    if (this.globalTasteChangedListener != null)
                        globalTasteChangedListener.onGlobalTasteChanged(tastes);

                }, error -> {}
                        //logger.debug("{} 全局忌口发生变化 {}", REALM_TAG, error)

                );

        RealmQuery<RealmDish> query = mainRealm.where(RealmDish.class)
                .equalTo("mealId", option.mealId)
                .greaterThan("eatCount", 0);
        Flowable<RealmResults<RealmDish>> observable;
        if (sortByFrom) {
            observable = query.findAllSorted(new String[]{"from", "modifyTime"}, new Sort[]{Sort.ASCENDING, Sort.DESCENDING})
                    .asFlowable().filter(RealmResults::isLoaded);
        } else {
            observable = query.findAllSorted("modifyTime", Sort.DESCENDING).asFlowable().filter(RealmResults::isLoaded);
        }
        observable
                .subscribe(realmDishes -> {
                    List<NodeDish> dishes = new LinkedList<>();
                    for (int i = 0; i < realmDishes.size(); i++) {
                        dishes.add(realmDishes.get(i).nodeDish());
                    }
                    //logger.debug("{} 所有菜品发生变化 {}", REALM_TAG, dishes);

                    onDishesChanged(dishes);
                }, error ->{}
                    //logger.error("{} 所有菜品发生变化 {}", REALM_TAG, error)
                        );

        if (option.observeMyDishesChanged) {
            mainRealm.where(RealmDish.class)
                    .equalTo("mealId", option.mealId)
                    .equalTo("from", option.form)
                    .greaterThan("eatCount", 0).findAll().asFlowable().filter(RealmResults::isLoaded)
                    .subscribe(realmDishes -> {
                        List<NodeDish> dishes = new LinkedList<>();
                        for (int i = 0; i < realmDishes.size(); i++) {
                            dishes.add(realmDishes.get(i).nodeDish());
                        }
                        //logger.debug("{} 我的菜品发生变化 {}", REALM_TAG, dishes);

                        onMyDishesChanged(dishes);
                    }, error ->
                            {}
                            //logger.debug("{} 我的菜品发生变化 {}", REALM_TAG, error)
                    );
        }

        if (option.observeDishesChangedExceptMy) {
            mainRealm.where(RealmDish.class)
                    .equalTo("mealId", option.mealId)
                    .notEqualTo("from", option.form)
                    .greaterThan("eatCount", 0).findAll()
                    .asFlowable().filter(RealmResults::isLoaded)
                    .subscribe(realmDishes -> {
                        //logger.debug("{} 其他人菜品发生变化", REALM_TAG);
                        onOthersDishesChanged();
                    }, error ->
                            {}
                            //logger.error("{} 其他人菜品发生变化 {}", REALM_TAG, error)
                    );
        }
    }

    @SuppressLint("DefaultLocale")
    public final void setDefaultDishes(List<NodeDish> defaultDishes) {
        mainRealm.executeTransactionAsync(realm -> {
            Long[] dishIds = new Long[defaultDishes.size()];
            for (int i = 0; i < defaultDishes.size(); i++) {
                dishIds[i] = defaultDishes.get(i).dishId;
            }
            long count = dishIds.length == 0 ? 0 : realm.where(RealmDish.class).equalTo("mealId", option.mealId).in("dishId", dishIds).count();
            if (count == 0) {
                List<RealmDish> realmDishList = new LinkedList<>();
                for (NodeDish dish : defaultDishes) {
                    if (dish.eatCount < 1) continue;
                    dish.pid = String.format("%d_%d_%s_%d", option.mealId, dish.dishId, dish.userId, dish.orderTime);
                    RealmDish realmDish = new RealmDish(dish, option.mealId, option.tableId);
                    realmDishList.add(realmDish);
                }
                realm.insertOrUpdate(realmDishList);
            }
        });

    }


    public final void saveDishes(List<NodeDish> dishes) {
        mainRealm.executeTransactionAsync(realm -> {
            for (NodeDish dish : dishes) {
                RealmDish realmDish = new RealmDish(dish, option.mealId, option.tableId);
                realm.copyToRealmOrUpdate(realmDish);
            }
        });
    }

    public final Single<List<NodeDish>> findSelectedDishes(long mealId) {
        return mainRealm.where(RealmDish.class)
                .equalTo("mealId", mealId)
                .greaterThan("eatCount", 0).findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded)
                .concatMap(Flowable::fromIterable)
                .map(RealmDish::nodeDish).toList()
                .subscribeOn(AndroidSchedulers.mainThread());

    }

    @SuppressLint({"DefaultLocale", "CheckResult"})
    private void observeNodeDishes() {

        RxSocketIO.on(mSocket, RECOVER_DISHES_ACTION)
                .filter(socketIOMsg -> socketIOMsg != null && socketIOMsg.getArgs() != null && !TextUtils.isEmpty(socketIOMsg.getArgs()[0].toString()))
                .map(socketIOMsg -> {
                    String s = socketIOMsg.getArgs()[0].toString();
                    Type type = new TypeToken<List<NodeDish>>() {}.getType();
                    return (List<NodeDish>)gson.fromJson(s,type);
                }).concatMap(dishes ->
                        Observable.fromIterable(dishes).map(dish -> {
                            dish.pid = String.format("%d_%d_%s_%d", option.mealId, dish.dishId, dish.userId, dish.orderTime);
                            if (dish.comboType > 1 && dish.comboDishList != null) {
                                for (NodeSetDish setDish : dish.comboDishList) {
                                    setDish.parentKey = dish.pid;
                                    setDish.pid = setDish.dishId + dish.pid;
                                }
                            }
                            return dish;
                        }).toList().toFlowable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dishes -> mainRealm.executeTransactionAsync(realm -> {
                            final RealmResults<RealmDish> realmDishes = realm.where(RealmDish.class)
                                    .equalTo("mealId", option.mealId).findAll();
                            realmDishes.deleteAllFromRealm();
                            List<RealmDish> realmDishList = new LinkedList<>();
                            for (int i = 0; i<dishes.size(); i++) {
                                NodeDish dish = dishes.get(i);
                                if (dish.eatCount < 1) continue;
                                RealmDish realmDish = new RealmDish(dish, option.mealId, option.tableId);
                                realmDish.modifyTime = System.currentTimeMillis();
                                realmDishList.add(realmDish);
                            }
                            realm.insertOrUpdate(realmDishList);
                            }, () -> {}
                                //logger.debug("{} 恢复菜存储成功", NODE_TAG)
                        , error -> {}
                                //logger.error("{} 恢复菜存储失败", NODE_TAG, error))
                ));


        RxSocketIO.on(mSocket, CHANGE_DISHES_ACTION, CHANGE_DISHES_FOR_SELF_ACTION)
                .filter(socketIOMsg -> socketIOMsg != null && socketIOMsg.getArgs() != null && !TextUtils.isEmpty(socketIOMsg.getArgs()[0].toString()))
                .map(socketIOMsg -> {
                    String s = socketIOMsg.getArgs()[0].toString();
                    //logger.debug("{} {}单个菜品：{}", NODE_TAG, socketIOMsg.getEventName(), s);
                    return gson.fromJson(s, NodeDish.class);
                }).map(dish -> {
            dish.pid = String.format("%d_%d_%s_%d", option.mealId, dish.dishId, dish.userId, dish.orderTime);
            if (dish.comboType > 1 && dish.comboDishList != null) {
                for (NodeSetDish setDish : dish.comboDishList) {
                    setDish.parentKey = dish.pid;
                    setDish.pid = setDish.dishId + dish.pid;
                }
            }
            return dish;
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dish -> mainRealm.executeTransactionAsync(realm -> {
                    RealmDish oldRealmDish = realm.where(RealmDish.class).equalTo("pid", dish.pid).findFirst();
                    if (oldRealmDish == null) {
                        RealmDish realmDish = new RealmDish(dish, option.mealId, option.tableId);
                        realmDish.modifyTime = System.currentTimeMillis();
                        realm.copyToRealmOrUpdate(realmDish);
                    }else {
                        oldRealmDish.eatCount = dish.eatCount;
                        oldRealmDish.tastes.deleteAllFromRealm();
                        if (dish.tastes!=null) {
                            for (NodeTaste taste : dish.tastes) {
                                oldRealmDish.tastes.add(new RealmTaste(taste));
                            }
                        }
                    }
                }, () -> {}, error -> {}));

        RxSocketIO.on(mSocket, CHANGE_GLOBAL_TASTES_ACTION)
                .filter(socketIOMsg -> socketIOMsg != null && socketIOMsg.getArgs() != null && !TextUtils.isEmpty(socketIOMsg.getArgs()[0].toString()))
                .map(socketIOMsg -> {
                    String s = socketIOMsg.getArgs()[0].toString();
                    Type type = new TypeToken<List<NodeDish>>() {}.getType();
                    return (List<NodeTaste>)gson.fromJson(s,type);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tastes -> saveGlobalTastes(option.mealId, option.tableId,tastes));
        RxSocketIO.on(mSocket, PAY_COMPLETE_ACTION)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(foo-> {
                    //logger.debug("{}支付完成清除菜品", NODE_TAG);
                    deleteDishesCache(option.mealId);});


    }

    private void onDishesChanged(List<NodeDish> dishes) {
        if (allDishesChangeListener != null) allDishesChangeListener.allDishesChanged(dishes);
    }

    private void onMyDishesChanged(List<NodeDish> dishes) {
        if (myDishesChangeListener != null) myDishesChangeListener.myDishesChanged(dishes);
    }

    private void onOthersDishesChanged() {
        if (nodeOthersDishesChangedListener != null)
            nodeOthersDishesChangedListener.othersDishesChanged();
    }


    public void saveGlobalTastes(long mealId,long tableId, List<NodeTaste> tastes) {
        mainRealm.executeTransactionAsync(realm -> {
            final RealmResults<RealmGlobalTaste> realmGlobalTastes = realm.where(RealmGlobalTaste.class)
                    .equalTo("mealId", mealId).findAll();
            realmGlobalTastes.deleteAllFromRealm();
            if (tastes != null && !tastes.isEmpty()) {
                List<RealmGlobalTaste> realmGlobalTasteList = new LinkedList<>();
                for (NodeTaste taste : tastes) {
                    RealmGlobalTaste realmDish = new RealmGlobalTaste(taste, mealId, tableId);
                    realmGlobalTasteList.add(realmDish);
                }
                realm.insertOrUpdate(realmGlobalTasteList);
            }
        }, () ->{} ,
                //logger.debug("{} 存储全单忌口成功", REALM_TAG
                 error ->{}
                //logger.error("{} 存储全单忌口失败", REALM_TAG, error)
        );
    }

    public Single<List<NodeTaste>> findGlobalTastes(long mealId){
        return mainRealm.where(RealmGlobalTaste.class)
                .equalTo("mealId", mealId)
                .findAllAsync().asFlowable()
                .filter(RealmResults::isLoaded)
                .flatMap(Flowable::fromIterable)
                .map(realmTaste -> new NodeTaste(realmTaste.id, realmTaste.name))
                .toList().subscribeOn(AndroidSchedulers.mainThread());
    }

    public void sendGlobalTaste(List<NodeTaste> tastes) {
        saveGlobalTastes(option.mealId, option.tableId, tastes);
        if (option.synergyOrder) {
            Observable.just(tastes).observeOn(Schedulers.io())
                    .map(o -> {
                        JSONArray jsonObject = null;
                        try {
                            jsonObject = new JSONArray(gson.toJson(o));
                        } catch (Exception e) {
                          //  logger.error("{} 发送Action错误", NODE_TAG, e);
                        }
                        return jsonObject;
                    }).subscribe(jsonObject -> mSocket.emit(CHANGE_GLOBAL_TASTES_ACTION, jsonObject),throwable -> {}
                    //logger.error("{} 发送全局Action错误", NODE_TAG, throwable)
                    ,()->{}
                           // logger.debug("{} 发送全局Action成功", NODE_TAG)
            );
        }
    }


    @SuppressLint("DefaultLocale")
    public final void copyToOrUpdateNodeDish(NodeDish dish, int type) {
        mainRealm.executeTransactionAsync(realm -> {
            @SuppressLint("DefaultLocale") String pid = dish.pid;
            NodeAction action = new NodeAction();
            action.key = String.format("%d_%s_%d", dish.dishId, dish.userId, dish.orderTime);
            RealmDish realmDish = realm.where(RealmDish.class).equalTo("pid", pid).findFirst();
            if (realmDish == null) {
                if (type == ACTION_MINUS) return;
                action.type = TYPE_ADD;
                if (type == ACTION_PLUS) dish.eatCount = dish.minQuantity;
                action.value = dish;
               // logger.debug("{} 新增db数据: {}", REALM_TAG, dish);
                RealmDish saveDish = new RealmDish(dish, option.mealId, option.tableId);
                saveDish.modifyTime = System.currentTimeMillis();
                realm.copyToRealmOrUpdate(saveDish);
                sendAction(action);
            } else {
                int oldEatCount = realmDish.eatCount;
                int diff;
                if (type == ACTION_PLUS) {
                    if (oldEatCount == 0) {
                        diff = dish.minQuantity;
                    } else {
                        diff = 1;
                    }
                    realmDish.eatCount = oldEatCount + diff;
                } else if (type == ACTION_MINUS) {
                    int eatCount = oldEatCount - 1;
                    if (eatCount < dish.minQuantity) {
                        realmDish.eatCount = 0;
                        diff = -dish.minQuantity;
                    } else {
                        realmDish.eatCount = eatCount;
                        diff = -1;
                    }
                } else {
                    realmDish.eatCount = dish.eatCount;
                    action.field = "eatCount";
                    action.value = dish.eatCount;
                    action.type = TYPE_MODIFY;
                    //logger.debug("{} 覆盖db eatCount: {}", REALM_TAG, dish);
                    sendAction(action);
                    return;
                }
                Map<String, Integer> value = new HashMap<>();
                action.value = value;
                if (diff > 0) {
                    action.type = TYPE_INCREASING;
                    //logger.debug("{} 增加db eatCount: {}", REALM_TAG, dish);
                    value.put("eatCount", diff);
                    sendAction(action);

                } else if (diff < 0) {
                    action.type = TYPE_DECREASING;
                    //logger.debug("{} 减少db eatCount: {}", REALM_TAG, dish);
                    value.put("eatCount", -diff);
                    sendAction(action);

                }
            }
        }, () -> {}
                   //     logger.debug("{} db菜品存储成功", REALM_TAG)
                , error -> {}
                        //logger.error("{} db菜品存储失败", REALM_TAG, error)
        );


    }

    @SuppressLint("DefaultLocale")
    public final void changeDishTaste(NodeSetDish dish) {
        NodeAction action = new NodeAction();
        action.type = TYPE_MODIFY;
        if (TextUtils.isEmpty(dish.parentKey)) {//非套餐内菜品
            @SuppressLint("DefaultLocale") String pid = ((NodeDish) dish).pid;
            action.key = String.format("%d_%s_%d", dish.dishId, dish.userId, dish.orderTime);
            mainRealm.executeTransactionAsync(realm -> {
                RealmDish realmDish = realm.where(RealmDish.class).equalTo("pid", pid).findFirst();
                if (realmDish != null) {
                    if (realmDish.tastes == null)
                        realmDish.tastes = new RealmList<>();
                    else
                        realmDish.tastes.clear();

                    for (NodeTaste taste : dish.tastes) {
                        realmDish.tastes.add(new RealmTaste(taste));
                    }
                }
            }, () -> {
                action.field = "tastes";
                action.value = dish.tastes;
                sendAction(action);
            }, error -> {}
                //logger.error("{} 改变忌口存储DB失败", REALM_TAG, error)
            );
        } else {
            @SuppressLint("DefaultLocale") String pid = dish.pid;
            mainRealm.executeTransactionAsync(realm -> {
                RealmSetDish realmDish = realm.where(RealmSetDish.class).equalTo("pid", pid).findFirst();
                if (realmDish != null) {
                    if (realmDish.tastes == null)
                        realmDish.tastes = new RealmList<>();
                    else
                        realmDish.tastes.clear();

                    for (NodeTaste taste : dish.tastes) {
                        realmDish.tastes.add(new RealmTaste(taste));
                    }
                }
            }, () -> {
                action.field = "comboDishList";
                RealmDish realmDish = mainRealm.where(RealmDish.class).equalTo("pid", dish.parentKey).findFirst();
                action.key = String.format("%d_%s_%d", realmDish.getDishId(), realmDish.getUserId(), realmDish.getOrderTime());
                if (realmDish != null)
                    Observable.fromIterable(realmDish.comboDishList)
                            .map(RealmSetDish::nodeSetDish)
                            .toList()
                            //.defaultIfEmpty(new ArrayList<>())
                            .subscribe(comboDishes -> {
                                action.value = comboDishes;
                                sendAction(action);
                            });

            }, error -> {}
                //logger.error("{}改变忌口存储DB失败:", REALM_TAG, error)
            );
        }

    }

    public void deleteDishesCache() {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            realm.where(RealmDish.class).findAll().deleteAllFromRealm();
            realm.where(RealmSetDish.class).findAll().deleteAllFromRealm();
            //realm.where(RealmGlobalTaste.class).findAll().deleteAllFromRealm();
        }, () -> {}
            //logger.debug("{} 删除指定餐次缓存菜品成功", REALM_TAG)
                , error -> {}
                        //logger.error("{} 删除指定餐次缓存菜品失败 {}", REALM_TAG, error)
        );

    }

    //清除指定餐次的菜品缓存
    public void deleteDishesCache(long mealId) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            realm.where(RealmDish.class).equalTo("mealId", mealId).findAll().deleteAllFromRealm();
            realm.where(RealmSetDish.class).equalTo("mealId", mealId).findAll().deleteAllFromRealm();
            //realm.where(RealmGlobalTaste.class).equalTo("mealId", mealId).findAll().deleteAllFromRealm();
        }, () -> {}
        //logger.debug("{} 删除指定餐次缓存菜品成功", REALM_TAG)
                , error ->{}
                //logger.error("{} 删除指定餐次缓存菜品失败 {}", REALM_TAG, error)

        );

    }

    //清除指定桌台非该餐次的菜品缓存
    public void deleteDishesCache(long tableId, long mealId) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            realm.where(RealmDish.class).equalTo("tableId", tableId).notEqualTo("mealId", mealId).findAll().deleteAllFromRealm();
            realm.where(RealmSetDish.class).equalTo("tableId", tableId).notEqualTo("mealId", mealId).findAll().deleteAllFromRealm();
            realm.where(RealmGlobalTaste.class).equalTo("tableId", tableId).notEqualTo("mealId", mealId).findAll().deleteAllFromRealm();
        }, () -> {}
            //logger.debug("{} 删除指定桌台非本餐次缓存菜品成功", REALM_TAG)
                , error -> {}
                //logger.error("{} 删除指定桌台非本餐次缓存菜品失败 {}", REALM_TAG, error)
        );
    }

    private void sendAction(NodeAction action) {
        if (option.synergyOrder)
            Observable.just(action).observeOn(Schedulers.io())
                    .map(o -> {
                        JSONObject jsonObject = null;
                        try {
                            String s = gson.toJson(o);
                            //logger.debug("{} 发送: {}", NODE_TAG, s);
                            jsonObject = new JSONObject(s);
                        } catch (Exception e) {
                            //logger.error("{} 发送Action错误", NODE_TAG, e);
                        }
                        return jsonObject;
                    }).subscribe(jsonObject -> mSocket.emit(CHANGE_DISHES_ACTION, jsonObject));
    }

    public final void stop() {
        mainRealm.close();
        if (mSocket != null)
            mSocket.close();
    }

    public final void setAllDishesChangeListener(NodeAllDishesChangeListener allDishesChangeListener) {
        this.allDishesChangeListener = allDishesChangeListener;
    }

    public final void setMyDishesChangeListener(NodeMyDishesChangeListener myDishesChangeListener) {
        this.myDishesChangeListener = myDishesChangeListener;
    }

    public void setOthersDishesChangedListener(NodeOthersDishesChangedListener nodeOthersDishesChangedListener) {
        this.nodeOthersDishesChangedListener = nodeOthersDishesChangedListener;
    }

    public void setGlobalTasteChangedListener(NodeGlobalTasteChangedListener globalTasteChangedListener) {
        this.globalTasteChangedListener = globalTasteChangedListener;
    }
}

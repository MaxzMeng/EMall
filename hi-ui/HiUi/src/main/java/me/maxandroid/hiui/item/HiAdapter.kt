package me.maxandroid.hiui.item

import android.content.Context
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 通用数据适配器
 *
 *
 * bugfix:HiDataItem<*, out RecyclerView.ViewHolder>  都被改成了这样。否则会有类型转换问题
 */
class HiAdapter(private var mContext: Context) : Adapter<ViewHolder>() {
    private var recyclerViewRef: WeakReference<RecyclerView>? = null
    private val mInflater by lazy { LayoutInflater.from(mContext) }
    private var dataSets = java.util.ArrayList<HiDataItem<*, ViewHolder>>()

    private var typeArrays = SparseArray<HiDataItem<*, ViewHolder>>()
//    private val typePositions = SparseIntArray();

    private var headers = SparseArray<View>()
    private var footers = SparseArray<View>()
    private var BASE_ITEM_TYPE_HEADER = 1000000
    private var BASE_ITEM_TYPE_FOOTER = 2000000

    /**
     *在指定为上添加HiDataItem
     */
    fun addItemAt(
        index: Int,
        dataItem: HiDataItem<*, ViewHolder>,
        notify: Boolean
    ) {
        if (index >= 0) {
            dataSets.add(index, dataItem)
        } else {
            dataSets.add(dataItem)
        }

        val notifyPos = if (index >= 0) index else dataSets.size - 1
        if (notify) {
            notifyItemInserted(notifyPos)
        }

        dataItem.setAdapter(this)
    }

    /**
     * 往现有集合的尾部逐年items集合
     */
    fun addItems(items: List<HiDataItem<*, ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        items.forEach { dataItem ->
            dataSets.add(dataItem)
            dataItem.setAdapter(this)
        }
        if (notify) {
            notifyItemRangeInserted(start, items.size)
        }
    }

    /**
     * 从指定位置上移除item
     */
    private fun removeItemAt(index: Int): HiDataItem<*, out ViewHolder>? {
        return if (index >= 0 && index < dataSets.size) {
            val remove: HiDataItem<*, out ViewHolder> = dataSets.removeAt(index)
            notifyItemRemoved(index)
            remove
        } else {
            null
        }
    }

    /**
     * 移除指定item
     */
    fun removeItem(dataItem: HiDataItem<*, out ViewHolder>) {
        val index: Int = dataSets.indexOf(dataItem)
        removeItemAt(index)
    }

    /**
     * 指定刷新 某个item的数据
     */
    fun refreshItem(dataItem: HiDataItem<*, out ViewHolder>) {
        val indexOf = dataSets.indexOf(dataItem)
        notifyItemChanged(indexOf)
    }

    override fun getItemViewType(position: Int): Int {
        val dataItem = dataSets[position]
        val type = dataItem.javaClass.hashCode()
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, dataItem)
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val dataItem = typeArrays.get(viewType)
        val vh = dataItem.onCreateViewHolder(parent)
        if (vh != null) return vh

        var view: View? = dataItem.getItemView(parent)
        if (view == null) {
            val layoutRes = dataItem.getItemLayoutRes()
            if (layoutRes < 0) {
                throw RuntimeException("dataItem:" + dataItem.javaClass.name + " must override getItemView or getItemLayoutRes")
            }
            view = mInflater.inflate(layoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, view!!)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, out ViewHolder>>,
        view: View
    ): ViewHolder {
        //得到该Item的父类类型,即为HiDataItem.class。  class 也是type的一个子类。
        //type的子类常见的有 class，类泛型,ParameterizedType参数泛型 ，TypeVariable字段泛型
        //所以进一步判断它是不是参数泛型
        val superclass = javaClass.genericSuperclass
        if (superclass is ParameterizedType) {
            //得到它携带的泛型参数的数组
            val arguments = superclass.actualTypeArguments
            //挨个遍历判断 是不是咱们想要的 RecyclerView.ViewHolder 子类 类型的。
            for (argument in arguments) {

                if (argument is Class<*>
                    && ViewHolder::class.java.isAssignableFrom(argument)
                ) {
                    try {
                        //如果是，则使用反射 实例化类上标记的实际的泛型对象
                        //这里需要  try-catch 一把，如果咱们直接在HiDataItem子类上标记 RecyclerView.ViewHolder，抽象类是不允许反射的
                        return argument.getConstructor(View::class.java)
                            .newInstance(view) as ViewHolder
                    } catch (e: Throwable) {
                        e.printStackTrace()

                    }
                }
            }
        }
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataSets[position]
        dataItem.onBindData(holder, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
        /**
         * 为列表上的item 适配网格布局
         */
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val itemPosition = position
                    if (itemPosition < dataSets.size) {
                        val dataItem = dataSets[itemPosition]
                        if (dataItem != null) {
                            val spanSize = dataItem.getSpanSize()
                            return if (spanSize <= 0) spanCount else spanSize
                        }
                    }
                    return spanCount
                }
            }
        }
    }

    override fun getItemCount() = dataSets.size


}
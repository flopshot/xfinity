package com.xfinity.ui.util

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import java.util.*
import java.util.concurrent.ThreadPoolExecutor

abstract class AsyncDiffUtilAdapter<VH : RecyclerView.ViewHolder, ItemType> : RecyclerView.Adapter<VH>() {

    private val pendingItems: Queue<MutableList<ItemType>> = ArrayDeque()
    private var executor: ThreadPoolExecutor? = null
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    /**
     * Sets the thread pool of background threads to be used for the DiffUtil processing
     *
     * @param executor Thread pool executor supplied for executing and updating DiffUtil.Results
     */
    fun setThreadPoolExecutor(executor: ThreadPoolExecutor?) {
        this.executor = executor
    }

    /**
     * Method to be used to update the RecyclerView.Adapter backing data
     *
     * @param oldItems Previous backing list of the RecyclerView.Adapter
     * @param newItems List of data to be used as new backing data of the list
     */
    fun updateItems(newItems: MutableList<ItemType>,
                    oldItems: MutableList<ItemType>) {
        pendingItems.add(newItems)
        if (pendingItems.size > 1) {
            return
        }

        updateItemsInternal(newItems, oldItems)
    }

    private fun updateItemsInternal(newItems: MutableList<ItemType>, oldItems: MutableList<ItemType>) {

        val calculateDiffUtilRunnable = Runnable {
            val diffResult = DiffUtil.calculateDiff(itemsDiffUtilCallback(oldItems, newItems))

            mainThreadHandler.post({ applyDiffResult(newItems, oldItems, diffResult) })
        }

        if (executor != null) {
            executor!!.execute(calculateDiffUtilRunnable)
        } else {
            AsyncTask.execute(calculateDiffUtilRunnable)
        }

    }

    private fun applyDiffResult(newItems: MutableList<ItemType>, oldItems: MutableList<ItemType>,
                                diffResult: DiffUtil.DiffResult) {
        pendingItems.remove()
        dispatchUpdates(newItems, oldItems, diffResult)
        if (pendingItems.size > 0) {
            updateItemsInternal(pendingItems.peek(), newItems)
        }
    }

    private fun dispatchUpdates(newItems: List<ItemType>, oldItems: MutableList<ItemType>,
                                diffResult: DiffUtil.DiffResult) {
        diffResult.dispatchUpdatesTo(this)
        oldItems.clear()
        oldItems.addAll(newItems)
    }

    /**
     * This method implements the DiffUtil.Callback between the new and old data items for the
     * RecyclerView.Adapter
     *
     * @param oldItems Previous backing list of the RecyclerView.Adapter
     * @param newItems List of data to be used as new backing data of the list
     * @return the DiffUtil.Callback used to calculate the DiffUtil.Result in the background thread
     */
    protected abstract fun itemsDiffUtilCallback(oldItems: List<ItemType>,
                                                 newItems: List<ItemType>): DiffUtil.Callback

}
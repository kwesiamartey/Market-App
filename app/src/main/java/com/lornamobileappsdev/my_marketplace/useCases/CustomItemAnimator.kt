package com.lornamobileappsdev.my_marketplace.useCases

import android.util.SparseArray
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CustomItemAnimator : DefaultItemAnimator() {

    private val cachedViews = SparseArray<RecyclerView.ViewHolder>()

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        val result = super.animateRemove(holder)
        holder?.let {
            cachedViews.put(holder.adapterPosition, it)
        }
        return result
    }

    override fun animateMove(holder: RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        val result = super.animateMove(holder, fromX, fromY, toX, toY)
        holder?.let {
            cachedViews.put(holder.adapterPosition, it)
        }
        return result
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        val result = super.animateAdd(holder)
        holder?.let {
            cachedViews.remove(holder.adapterPosition)
        }
        return result
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>): Boolean {
        val cachedHolder = cachedViews.get(viewHolder.adapterPosition)
        return cachedHolder?.let {
            super.canReuseUpdatedViewHolder(viewHolder, payloads) && viewHolder.itemView == cachedHolder.itemView
        } ?: super.canReuseUpdatedViewHolder(viewHolder, payloads)
    }

    override fun endAnimation(holder: RecyclerView.ViewHolder) {
        super.endAnimation(holder)
        cachedViews.remove(holder.adapterPosition)
    }

    override fun endAnimations() {
        super.endAnimations()
        cachedViews.clear()
    }
}

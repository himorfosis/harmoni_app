package com.harmonievent.homepage.fragment.callback

object HomeCallback {

    lateinit var onClickItem: OnClickItem

    interface OnClickItem {
        fun onItemClicked(data: Int)

    }

    fun setOnclick(onClickItem: OnClickItem) {
        this.onClickItem = onClickItem

    }

}
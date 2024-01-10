package com.mohanjp.runningtracker.common.utils.presentation

import android.view.View

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.setOnDebounceClickListener(onClick: (View) -> Unit) {
    val debounceClickListener = object : DebounceClickListener() {
        override fun onDebounceClick(view: View) {
            onClick(view)
        }
    }
    setOnClickListener(debounceClickListener)
}
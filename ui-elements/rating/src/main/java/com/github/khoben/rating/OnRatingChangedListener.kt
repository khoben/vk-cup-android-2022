package com.github.khoben.rating

interface OnRatingChangedListener {
    fun onRatingChanged(rating: Float)

    object NOOP : OnRatingChangedListener {
        override fun onRatingChanged(rating: Float) = Unit
    }
}
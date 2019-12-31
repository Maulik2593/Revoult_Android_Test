package com.revolut.ui.adapter

/**
 * Listens changes in amount in recyclerview
 */
interface OnAmountChangedListener {

    /**
     * Function called when the user changed the amount for the given currency
     *
     */
    fun onAmountChanged(symbol: String, amount: Float)
}
package revoult.test.maulik.view

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.revolut.ui.adapter.OnAmountChangedListener
import com.revolut.util.formatToString
import com.revolut.util.getCurrencyFlagResId
import com.revolut.util.getCurrencyNameResId
import kotlinx.android.synthetic.main.rate_list_item.view.*
import revoult.test.maulik.model.Rate
import revoult.test.maulik.utils.DecimalDigitsInputFilter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.map
import kotlin.collections.set

class RateListAdapter(var context: Context, private val onAmountChangedListener: OnAmountChangedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val symbols = ArrayList<String>()
    private val rateMap = HashMap<String, Rate>()
    private var amount = 1.0F

    /**
     * Update the rate of each currency except the focused
     */
    fun updateRates(rates: List<Rate>) {
        if (symbols.isEmpty()) {
            symbols.addAll(rates.map { it.symbol })
        }
        for (rate in rates) {
            rateMap[rate.symbol] = rate
        }
        notifyItemRangeChanged(0, symbols.size - 1, amount)
    }

    /**
     * Update the amount and change amount for all currencies
     */
    fun updateAmount(amount: Float) {
        this.amount = amount
        notifyItemRangeChanged(0, symbols.size - 1, amount)
    }

    /**
     * Returns the rate at the given position to bind it to recyclerview item view
     */
    private fun rateAtPosition(pos: Int): Rate {
        return rateMap[symbols[pos]]!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RateViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(revoult.test.maulik.R.layout.rate_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return symbols.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (!payloads.isEmpty()) {
            (holder as RateViewHolder).bind(rateAtPosition(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RateViewHolder).bind(rateAtPosition(position))
    }

    /**
     * Viewholder for the currency
     */
    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCountryFlag: ImageView = itemView.ivCountryFlag
        var txtCurrencySymbol: TextView = itemView.txtCurrencySymbol
        var txtCurrencyName: TextView = itemView.txtCurrencyName
        var edtCurrencyAmount: EditText = itemView.txtCurrencyAmount
        var symbol: String = ""
        fun bind(rate: Rate) {
            if (symbol != rate.symbol) {
                initView(rate)
                this.symbol = rate.symbol
            }
            //  we don't change the value,for focused edittext
            if (!edtCurrencyAmount.isFocused) {
                edtCurrencyAmount.setText((rate.rate * amount).formatToString())
            }
        }

        /**
         * Setup the view
         */
        private fun initView(rate: Rate) {
            val symbol = rate.symbol.toLowerCase()
            val nameId = getCurrencyNameResId(itemView.context, symbol)
            val flagId = getCurrencyFlagResId(itemView.context, symbol)

            txtCurrencySymbol.text = rate.symbol
            txtCurrencyName.text = itemView.context.getString(nameId)
            ivCountryFlag.setImageResource(flagId)

            edtCurrencyAmount.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                //If view lost focus, do nothing
                if (!hasFocus) {
                    return@OnFocusChangeListener
                }

                //If view is already on top, we are not performing any operation
                layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                    //We move focused item from its current position, to the top
                    symbols.removeAt(currentPosition).also {
                        symbols.add(0, it)
                    }
                    notifyItemMoved(currentPosition, 0)
                    try {
                        onAmountChangedListener.onAmountChanged(symbols[0], edtCurrencyAmount.text.toString().toFloat())
                    } catch (ex: Exception) {
                    }
                }
            }

            edtCurrencyAmount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edtCurrencyAmount.isFocused) {
                        try {
                            if (s.toString() != null && s.toString().isNotEmpty() && ((s.toString()).toFloat()) > 0) {
                                onAmountChangedListener.onAmountChanged(symbol, s.toString().toFloat())
                            }
                            else if(s.toString().isEmpty()||s.toString().toFloat()<=0)
                            {
                                onAmountChangedListener.onAmountChanged(symbol, 0f)
                            }
                        } catch (ex: Exception) {
                        }
                    }
                }
            })
            edtCurrencyAmount.filters = (arrayOf<InputFilter>(DecimalDigitsInputFilter(Integer.MAX_VALUE, 2, Double.POSITIVE_INFINITY)))

        }
    }
}
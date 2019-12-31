package revoult.test.maulik.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolut.ui.adapter.OnAmountChangedListener
import kotlinx.android.synthetic.main.rate_list_layout.view.*
import revoult.test.maulik.model.Rate
import revoult.test.maulik.viewmodel.RateViewModel
import revoult.test.maulik.viewmodel.RateViewModelFactory
import com.androidstudy.networkmanager.Tovuti
import com.androidstudy.networkmanager.Monitor
import com.irozon.sneaker.Sneaker
import revoult.test.maulik.databinding.RateListLayoutBinding

class RateFragment : Fragment() {

    lateinit var rateViewModel: RateViewModel
    var fragmentView: View? = null
    private var listAdapter: RateListAdapter? = null
    private var rateListLayoutBinding: RateListLayoutBinding? = null
    var rateHandler: Handler = Handler()
    lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rateListLayoutBinding = DataBindingUtil.inflate(inflater, revoult.test.maulik.R.layout.rate_list_layout, container, false)
        fragmentView = rateListLayoutBinding?.root
        initAdapter()
        setAdapter()
        fetchRetroInfo()
        startRateHandler()
        monitorNetworkState()
        return fragmentView
    }

    /**
     * Monitor network changes
     */
    private fun monitorNetworkState() {
        Tovuti.from(activity).monitor(object : Monitor.ConnectivityListener {
            override fun onConnectivityChanged(connectionType: Int, isConnected: Boolean, isFast: Boolean) {
                if (!isConnected) {
                    showErrorMessage(getString(revoult.test.maulik.R.string.error_network))
                    stopRateHandler()
                } else {
                    hideErrorMessage()
                    startRateHandler()
                }
            }
        })
    }

    /**
     * Hide recyclerview
     */
    private fun hideErrorMessage() {
        fragmentView?.post_list?.visibility = View.VISIBLE
        fragmentView?.txtNoData?.visibility=View.GONE
        if(listAdapter!!.itemCount>0)
        fragmentView?.progress_circular?.visibility=View.GONE
    }

    /**
     * Start handler, to call api every 1 second
     */
    private fun startRateHandler() {
        runnable = Runnable {
            rateViewModel.fetchRateInfoFromRepository()
            rateHandler.postDelayed(runnable, 1000)
        }
        rateHandler.postDelayed(runnable, 1000)
    }

    /**
     * Stop handler callback to stop api calls
     */
    private fun stopRateHandler() {
        rateHandler.removeCallbacks(runnable)
    }

    /**
     * Create viewmodel for rates
     */
    fun initViewModel() {
        var retroViewModelFactory = RateViewModelFactory()
        rateViewModel = ViewModelProviders.of(this, retroViewModelFactory).get(RateViewModel::class.java)
    }

    /**
     * Create observer on rate list data to looking for any change
     */
    fun fetchRetroInfo() {
        rateViewModel.rateListLiveData?.observe(this, object : Observer<List<Rate>> {
            override fun onChanged(t: List<Rate>?) {
                t?.apply {
                    if (t.size > 0) {
                        listAdapter?.updateRates(t)
                        hideErrorMessage()
                    } else {
                        showErrorMessage(getString(revoult.test.maulik.R.string.error_message))
                    }
                }
            }
        })
    }

    /**
     * Set up adapter for recyclerview
     */
    private fun setAdapter() {
        fragmentView?.post_list?.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            adapter = listAdapter
        }
        fragmentView?.post_list?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->{
                        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    /**
     * Create rate list adapter
     */
    private fun initAdapter() {
        listAdapter = RateListAdapter(this@RateFragment.requireActivity(), object : OnAmountChangedListener {
            override fun onAmountChanged(symbol: String, amount: Float) {
                listAdapter!!.updateAmount(amount)
                rateViewModel.baseCurrency = symbol
                rateViewModel.changeBaseCurrency(symbol)

            }
        })
    }

    override fun onDestroy() {
        stopRateHandler()
        super.onDestroy()
    }

    fun showErrorMessage(message: String) {
        fragmentView?.post_list?.visibility = View.GONE
        fragmentView?.txtNoData?.visibility=View.VISIBLE
        fragmentView?.progress_circular?.visibility=View.GONE
        Sneaker.with(activity)
                .setTitle(getString(revoult.test.maulik.R.string.error_title))
                .setMessage(message)
                .sneakError()
    }
}
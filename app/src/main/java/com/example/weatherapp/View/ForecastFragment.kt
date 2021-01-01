package com.example.weatherapp.View

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.weatherapp.CommunicationInterface
import com.example.weatherapp.ViewModel.ForecastViewModel
import com.example.weatherapp.R
import com.example.weatherapp.Util.loadImage
import kotlinx.android.synthetic.main.fragment_forecast.*

class ForecastFragment : Fragment() {

    lateinit var communication: CommunicationInterface
    lateinit var viewModel: ForecastViewModel
    var reply: String? = null
    val API = ""
    var birim: String? = null

    override fun onAttach(context: Context) {
        try {
            communication = context as MainActivity
        } catch (e: Exception) {
            println(e.localizedMessage)
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast, container, false)

        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        reply = sp.getString("reply", "")
        if (reply == "celsius") {
            birim = "°C"
        } else if (reply == "kelvin") {
            birim = "K"
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ForecastViewModel::class.java)
        viewModel.getData("Istanbul", reply, API)
        observeViewModel()

        (activity as AppCompatActivity).setSupportActionBar(toolbar_forecast)
        setHasOptionsMenu(true)
        communication.toolbar(toolbar_forecast)



        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeViewModel() {
        viewModel.weather.observe(viewLifecycleOwner, Observer {
            name.text = it.name
            val tempDouble = it.main.get("temp") as Double
            temp.text = "${tempDouble.toInt()}$birim"
            description.text = it.weather.get(0).get("description").toString().toUpperCase()
            icon.loadImage(it.weather.get(0).get("icon").toString())
            val feelingDouble = it.main.get("feels_like") as Double
            feeling.text = "Hissedilen ${feelingDouble.toInt()}$birim"
            val minDouble = it.main.get("temp_min") as Double
            val maxDouble = it.main.get("temp_max") as Double
            min_max.text = "${minDouble.toInt()}$birim/${maxDouble.toInt()}$birim"
        })

        viewModel.forecast.observe(viewLifecycleOwner, Observer {
            val map1 = it.list.get(0).get("main") as Map<String, Any>
            val map2 = it.list.get(1).get("main") as Map<String, Any>
            val map3 = it.list.get(2).get("main") as Map<String, Any>
            val map4 = it.list.get(3).get("main") as Map<String, Any>
            val temp1Double = map1.get("temp") as Double
            val temp2Double = map2.get("temp") as Double
            val temp3Double = map3.get("temp") as Double
            val temp4Double = map4.get("temp") as Double
            temp1.text = "${temp1Double.toInt()}$birim"
            temp2.text = "${temp2Double.toInt()}$birim"
            temp3.text = "${temp3Double.toInt()}$birim"
            temp4.text = "${temp4Double.toInt()}$birim"
            time1.text = it.list.get(0).get("dt_txt").toString().takeLast(8).take(5)
            time2.text = it.list.get(1).get("dt_txt").toString().takeLast(8).take(5)
            time3.text = it.list.get(2).get("dt_txt").toString().takeLast(8).take(5)
            time4.text = it.list.get(3).get("dt_txt").toString().takeLast(8).take(5)
            val hm1: ArrayList<Map<String, Any>> =
                it.list.get(0).get("weather") as ArrayList<Map<String, Any>>
            icon1.loadImage(hm1.get(0).get("icon").toString())
            val hm2: ArrayList<Map<String, Any>> =
                it.list.get(1).get("weather") as ArrayList<Map<String, Any>>
            icon2.loadImage(hm2.get(0).get("icon").toString())
            val hm3: ArrayList<Map<String, Any>> =
                it.list.get(2).get("weather") as ArrayList<Map<String, Any>>
            icon3.loadImage(hm3.get(0).get("icon").toString())
            val hm4: ArrayList<Map<String, Any>> =
                it.list.get(3).get("weather") as ArrayList<Map<String, Any>>
            icon4.loadImage(hm4.get(0).get("icon").toString())
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val search = menu.findItem(R.id.action_search).actionView as SearchView
        search.queryHint = "Şehir adı giriniz.."
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getData(it, reply, API)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

}
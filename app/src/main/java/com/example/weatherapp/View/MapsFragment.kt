package com.example.weatherapp.View

import android.content.Context
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.weatherapp.CommunicationInterface
import com.example.weatherapp.ViewModel.MapsViewModel
import com.example.weatherapp.R
import com.example.weatherapp.Util.loadImage

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_maps.*
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment() {

    lateinit var communication: CommunicationInterface
    lateinit var viewModel: MapsViewModel
    var marker: Marker? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    var temp: String? = null
    var description: String? = null
    var icon: String? = null
    var reply: String? = null
    var birim: String? = null
    val API = ""

    override fun onAttach(context: Context) {
        try {
            communication = context as MainActivity
            bottomSheetDialog = BottomSheetDialog(context)
        } catch (e: Exception) {
            println(e.localizedMessage)
        }
        super.onAttach(context)
    }

    private val callback = OnMapReadyCallback { googleMap ->


        googleMap.setOnMapLongClickListener(GoogleMap.OnMapLongClickListener {
            if (marker == null) {
                marker = googleMap.addMarker(MarkerOptions().position(it).title("Sıcaklık"))
            } else {
                marker!!.position = it
            }
            temp = null
            description = null
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 5f))
            viewModel.getData(it, reply, API)

        })

        googleMap.setOnMarkerClickListener {
            marker?.let { m ->
                if (it == m) {
                    bottomSheet(m.title)
                }
            }

            true
        }

    }

    private fun bottomSheet(name: String) {
        bottomSheetDialog?.let {
            it.bsd_name.text = name
            temp?.let { temp ->
                it.bsd_temp.text = temp
                description?.let { description ->
                    it.bsd_description.text = description
                    icon?.let { icon ->
                        it.bsd_icon.loadImage(icon)
                    }
                }
            }
            it.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MapsViewModel::class.java)

        observeViewModel()

        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        reply = sp.getString("reply", "")
        if (reply == "celsius") {
            birim = "°C"
        } else if (reply == "kelvin") {
            birim = "K"
        }

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        (activity as AppCompatActivity).setSupportActionBar(toolbar_maps)
        setHasOptionsMenu(true)
        communication.toolbar(toolbar_maps)

        bottomSheetDialog?.setContentView(R.layout.bottom_sheet_maps)
    }

    private fun observeViewModel() {
        viewModel.weather.observe(viewLifecycleOwner, Observer {
            Toast.makeText(
                requireContext(),
                "Hava durumu için işaretlediğiniz noktaya dokunun.",
                Toast.LENGTH_SHORT
            ).show()
            val tempDouble = it.main.get("temp") as Double
            temp = "${tempDouble.toInt()}$birim"

            description = it.weather.get(0).get("description").toString().toUpperCase()

            icon = it.weather.get(0).get("icon").toString()
            marker?.let { marker ->
                marker.title = it.name
            }

        })
    }

}
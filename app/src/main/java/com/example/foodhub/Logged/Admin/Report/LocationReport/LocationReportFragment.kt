package com.example.foodhub.Logged.Admin.Report.LocationReport

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.R
import com.example.foodhub.database.Account
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.LocationReport
import com.example.foodhub.databinding.FragmentLocationReportBinding
import com.example.foodhub.util.Util
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class LocationReportFragment : Fragment(), OnMapReadyCallback  {

    companion object {
        fun newInstance() = LocationReportFragment()
    }

    val util = Util()

    private lateinit var binding: FragmentLocationReportBinding
    private lateinit var viewModel: LocationReportViewModel

    private lateinit var db: FoodHubDatabase
    private val TAG = MapsInitializer::class.java.simpleName
    private lateinit var map: GoogleMap

    private lateinit var lLocationReport: List<LocationReport>

    private lateinit var eastMalaysia: LatLng
    private lateinit var westMalaysia: LatLng

    private var zoomLevelEast = 0f
    private var zoomLevelWest = 0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationReportBinding.inflate(inflater)
        db = FoodHubDatabase.getInstance(requireContext())

        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        lifecycleScope.launch {
            getData("all")
        }

        binding.btnAllUser.setOnClickListener {
            getData("all")
        }

        binding.btnDonor.setOnClickListener {
            getData("donor")
        }

        binding.btnDonee.setOnClickListener {
            getData("donee")
        }

        binding.btnEastMalaysia.setOnClickListener {
            map.moveCamera(CameraUpdateFactory.newLatLng(eastMalaysia))
            map.setMinZoomPreference(zoomLevelEast)
            map.setMaxZoomPreference(zoomLevelEast)
        }

        binding.btnWestMalaysia.setOnClickListener {
            map.moveCamera(CameraUpdateFactory.newLatLng(westMalaysia))
            map.setMinZoomPreference(zoomLevelWest)
            map.setMaxZoomPreference(zoomLevelWest)
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationReportViewModel::class.java)
        // TODO: Use the ViewModel
    }


    //Map
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        eastMalaysia = LatLng(3.7035, 114.2043)
        westMalaysia = LatLng(3.9743, 102.4381)
        zoomLevelEast = 5.35f
        zoomLevelWest = 6f


        map.moveCamera(CameraUpdateFactory.newLatLng(eastMalaysia))
        map.setMinZoomPreference(zoomLevelEast)

        map.uiSettings.setAllGesturesEnabled(false)

        setMapStyle(map)
        addHeatMap(map)

    }


    private fun addHeatMap(map: GoogleMap) {
        var latLngs: List<LatLng?>? = null

        // Get the data: latitude/longitude positions of malaysia state.
        try {
            latLngs = readItems(R.raw.malaysia)
        } catch (e: JSONException) {
            Toast.makeText(context, "Problem reading list of locations.", Toast.LENGTH_LONG)
                .show()
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        val provider = HeatmapTileProvider.Builder()
            .data(latLngs)
            .build()

        // Add a tile overlay to the map, using the heat map tile provider.
        val overlay = map.addTileOverlay(TileOverlayOptions().tileProvider(provider))
    }

    @Throws(JSONException::class)
    private fun readItems(@RawRes resource: Int): List<LatLng?> {
        val result: MutableList<LatLng?> = ArrayList()
        val inputStream = context?.resources?.openRawResource(resource)
        val json = Scanner(inputStream).useDelimiter("\\A").next()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val `object` = array.getJSONObject(i)
            val lat = `object`.getDouble("lat")
            val lng = `object`.getDouble("lng")
            result.add(LatLng(lat, lng))
        }
        return result
    }


    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }


    //get Data
    private fun getData(type: String?) {
        val locationReportLiveData = db.locationReportDao.getAll()

        val url = "http://10.0.2.2/foodhub_server/account.php?request=locationReport"
        val accounts: MutableList<Account> = mutableListOf()

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received location report")
                val jsonArray = response.getJSONArray("data")

                for (i in 0 until jsonArray.length()) {
                    val jsonObj = jsonArray.getJSONObject(i)
                    val jsonState = jsonObj.getString("state")
                    val jsonAccountType = jsonObj.getString("account_type")

                    val account = Account()
                    account.state = jsonState
                    account.accountType = jsonAccountType

                    accounts.add(account)
                }

                locationReportLiveData.observe(viewLifecycleOwner) { locationReports ->

                    for (i in 0 until locationReports!!.size) {
                        locationReports[i].totalDonor = 0
                        locationReports[i].totalDonee = 0
                        locationReports[i].totalUser = 0
                    }

                    for (i in 0 until accounts.size) {
                        val state = accounts[i].state
                        val accountType = accounts[i].accountType

                        for (j in locationReports.indices) {
                            val lState = locationReports[j].state

                            if (state == lState) {
                                when (accountType) {
                                    "donor" -> {
                                        locationReports[j].totalDonor =
                                            locationReports[j].totalDonor!!.plus(1)
                                    }
                                    "donee" -> {
                                        locationReports[j].totalDonee =
                                            locationReports[j].totalDonee!!.plus(1)
                                    }
                                }
                                locationReports[j].totalUser =
                                    locationReports[j].totalUser!!.plus(1)
                            }
                        }
                    }

                    updateData(locationReports)

                    when (type) {
                        "all" -> {
                            lLocationReport = locationReports.toMutableList()
                                .sortedByDescending { it.totalUser }
                        }
                        "donor" -> {
                            lLocationReport = locationReports.toMutableList()
                                .sortedByDescending { it.totalDonor }
                        }
                        "donee" -> {
                            lLocationReport = locationReports.toMutableList()
                                .sortedByDescending { it.totalDonee }
                        }
                    }
                    updateUI(type)
                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)

    }


    //update local DB & server
    private fun updateData(locationReports: List<LocationReport>) {
        lifecycleScope.launch {
            for (i in locationReports.indices) {
                locationReports[i].updatedAt = util.generateDate()
                db.locationReportDao.update(locationReports[i])
            }

            //postData()
        }
    }

    //update UI
    private fun updateUI(type: String?) {
        var total = ""
        for (i in lLocationReport.indices) {
            val state = lLocationReport[i].state.toString()
            when (type) {
                "all" -> total = lLocationReport[i].totalUser.toString()
                "donor" -> total = lLocationReport[i].totalDonor.toString()
                "donee" -> total = lLocationReport[i].totalDonee.toString()
            }

            binding.tableState.getChildAt(i).findViewWithTag<TextView>("label").text = state
            binding.tableState.getChildAt(i).findViewWithTag<TextView>("value").text = total
        }
    }

    //Post to server
    private fun postData() {
        val url = "http://10.0.2.2/foodhub_server/location_report.php"

        val js = JSONObject()
        val request = "update"

        try {
            val jsonobject = JSONObject()
            jsonobject.put("request", request)
            jsonobject.put("data", lLocationReport)
            js.put("data", jsonobject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST, url, js,
            { response ->
                Log.i("test", response.toString())
                val status = response.getInt("status")

                if (status == 0) {
                    Log.i("db_location_report_fragment", "Location Report updated successful")
                } else if (status > 0) {
                    Log.i("db_location_report_fragment", "Location Report failed to update")
                } else {
                    Log.e("db_location_report_fragment", "Location Report failed to update due to error")
                }
            }, { error ->
                VolleyLog.d("db_location_report_fragment", "Error: " + error.toString())
            })
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(jsonObjReq)
    }


}
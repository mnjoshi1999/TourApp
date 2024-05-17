package com.android.ux.tourid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservationFragment : Fragment() {
    // TODO: Rename and change types of parameter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_reservation, container, false)
        val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
        var blog =databaseHandler.viewReservations()
        val blogs = blog.map { "Reservation ID: "+it.reservation_id+"\n\n"+"Attendee Name: "+it.attendee+"\n\n"+"Event Name: "+it.event_title+"\n\n"+"Date Booked: "+it.reservation_date+"\n\n"}
        var i :Int = 0

        val listview = view.findViewById(R.id.reserves_list) as ListView
        var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,blogs)
        listview.adapter = arrayAdapter

        if(blog.size < 1){
            var blogs = ArrayList<String>()
            blogs.add("No reservations made")
            val listview = view.findViewById(R.id.reserves_list) as ListView
            var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,blogs)
            listview.adapter = arrayAdapter

        }

        return view
    }


}
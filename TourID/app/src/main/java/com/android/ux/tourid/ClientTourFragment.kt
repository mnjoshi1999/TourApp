package com.android.ux.tourid

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClientTourFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientTourFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_client_tour, container, false)
        val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
        var eventtitle= ArrayList<String>()
        var eventdesc= ArrayList<String>()
        var eventtime= ArrayList<String>()
        var eventimg= ArrayList<String>()
        val bloglist = databaseHandler.viewTours()
        bloglist.forEach {element->
            eventtitle.add(element.eventtitle)
            eventdesc.add(element.eventdesc)
            eventtime.add(element.eventtime)
            eventimg.add(element.eventimg)
        }
        var i :Int = 0

        val listview = view.findViewById(R.id.client_Tour_list) as ListView
        val myListAdapter = CustomAdapter(requireContext(),eventtitle,eventdesc,eventimg,eventtime,inflater)
        listview.adapter = myListAdapter

        listview.setOnItemClickListener(){adapterView, view, position, id ->

            val dialogBuilder = AlertDialog.Builder(requireContext())

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to make a reservation for this event?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener {

                        dialog, id ->
                    val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
                    var email = databaseHandler.getcurrent()
                    databaseHandler.makeresevation(email,Date().toString(),eventtitle.get(position))
                    Toast.makeText(requireContext(),"Successfully made your reservation",Toast.LENGTH_SHORT).show()})
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Event Reservation")
            // show alert dialog
            alert.show()

            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(requireContext(), "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
        }


        return view
    }


}
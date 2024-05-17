package com.android.ux.tourid

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso

class  CustomAdapter(
    private val context: Context,
    private val title: ArrayList<String>,
    private val description: ArrayList<String>,
    private val imgid: ArrayList<String>,
    private val eventime: ArrayList<String>,
    inflater: LayoutInflater
)
    : ArrayAdapter<String>(context, R.layout.custom_list, title) {

    @SuppressLint("ViewHolder", "MissingInflatedId")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val imageView = rowView.findViewById(R.id.icon) as ImageView
        val subtitleText = rowView.findViewById(R.id.description) as TextView
        val time = rowView.findViewById(R.id.time) as TextView

        titleText.text = "Event Title: "+title[position]
        Picasso.get().load(imgid[position]).into(imageView)
        subtitleText.text = "Event Desc: "+ description[position]
        time.text = "Event Time: "+eventime[position]

        return rowView
    }
}
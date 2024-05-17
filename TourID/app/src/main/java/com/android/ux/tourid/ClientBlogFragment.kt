package com.android.ux.tourid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClientBlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientBlogFragment : Fragment(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_client_blog, container, false)

        val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
        val bloglist = databaseHandler.viewBlogs()
        val blogs = bloglist.map { it.blogTitle}
        var i :Int = 0

        val listview = view.findViewById(R.id.blogs_list) as ListView
        var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,blogs)
        listview.adapter = arrayAdapter

        listview.setOnItemClickListener { parent, view, position, id ->
            // This is the click event handler
            val selectedItem = parent.getItemAtPosition(position)
//            Toast.makeText(requireContext(), "Clicked on: $selectedItem", Toast.LENGTH_SHORT).show()
            val viewModel :SharedViewModel by activityViewModels()
            viewModel.message.value = selectedItem.toString()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.container, ViewBlogFragment())
            }
            if (transaction != null) {
                transaction.disallowAddToBackStack()
            }
            if (transaction != null) {
                transaction.commit()
            }

        }

        if(bloglist.size < 1){
            val blogs = ArrayList<String>()
            blogs.add("No blogs available")
            val listview = view.findViewById(R.id.blogs_list) as ListView
            var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,blogs)
            listview.adapter = arrayAdapter
        }

        return view
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


}
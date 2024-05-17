package com.android.ux.tourid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewCommentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewCommentsFragment : Fragment() {
    private var blogid1:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_view_comments, container, false)
        val viewModel :SharedViewModel by activityViewModels()
        viewModel.blogid.observe(viewLifecycleOwner, Observer { blogid ->
            val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
            val bloglist = databaseHandler.viewComments(blogid)
            blogid1=blogid

            val blogs = bloglist.map {it.commentContent}
            val listview = view.findViewById(R.id.comments_list) as ListView
            var arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,blogs)
            Toast.makeText(requireContext(),"here1 "+bloglist.size.toString(),Toast.LENGTH_LONG).show()
            listview.adapter = arrayAdapter

        })

        val sendbtn = view.findViewById(R.id.idFABcomment) as FloatingActionButton
        val textview = view.findViewById(R.id.commented) as TextInputEditText

        sendbtn.setOnClickListener {
            if(textview.text.toString().length > 0){
                val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
                databaseHandler.addComment(blogid1,textview.text.toString())
                Toast.makeText(requireContext(),"here",Toast.LENGTH_LONG).show()
            }
        }



        return view

    }
}

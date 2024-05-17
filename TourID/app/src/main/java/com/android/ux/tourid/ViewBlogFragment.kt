package com.android.ux.tourid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewBlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewBlogFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_view_blog, container, false)
        // Inflate the layout for this fragment
        if (isAdded) {
            val viewModel :SharedViewModel by activityViewModels()
            viewModel.message.observe(viewLifecycleOwner, Observer { message ->
                // Update UI with the received message
                val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
                var blog =databaseHandler.viewBlog(message)
                var blogtitle = view.findViewById<TextView>(R.id.blogtitleview)
                var blogcontent = view.findViewById<TextView>(R.id.blogcontentview)
                blogtitle.text=blog.blogTitle
                blogcontent.text=blog.blogContent

                var viewcomments = view.findViewById<TextView>(R.id.viewComments)
                viewcomments.setOnClickListener {
                    val viewModel :SharedViewModel by activityViewModels()
                    viewModel.blogid.value = blog.blogid
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.replace(R.id.container, ViewCommentsFragment())
                    }
                    if (transaction != null) {
                        transaction.addToBackStack("blog")
                    }
                    if (transaction != null) {
                        transaction.commit()
                    }
                }

            })


        }

        return view
    }


}
package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_oferty.*
import kotlinx.android.synthetic.main.fragment_oferty.view.*

class OfertyFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var mAdapter: FirestorePagingAdapter<MyOfert, OfertViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(
            com.example.projekt.R.layout.fragment_oferty,
            container,
            false
        )

        // Init RecyclerView
        val recyclerView = viewOfLayout.recyclerViewOferty
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        setupAdapter()

        // Refresh Action on Swipe Refresh Layout
        viewOfLayout.swipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }

        viewOfLayout.test.setOnClickListener {
            (activity as MainActivity).loadFragment(OfertyFragmentTwo())
        }

        viewOfLayout.recyclerViewOferty.adapter = mAdapter

        return viewOfLayout
    }

    private fun setupAdapter(){

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val query = db.collection("oferty").whereEqualTo("uzytkownik_id", auth.currentUser!!.uid)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<MyOfert>()
            .setLifecycleOwner(this)
            .setQuery(query, config, MyOfert::class.java)
            .build()

        mAdapter = object : FirestorePagingAdapter<MyOfert, OfertViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertViewHolder {
                val view = layoutInflater.inflate(R.layout.item_ofert, parent, false)
                Log.d("TEST", view.toString())
                return OfertViewHolder(view)
            }

            override fun onBindViewHolder(viewHolder: OfertViewHolder, position: Int, post: MyOfert) {
                // Bind to ViewHolder
                viewHolder.bind(post)
            }

            override fun onError(e: Exception) {
                super.onError(e)
                Log.e("TEST", e.message.toString())
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                when (state) {
                    LoadingState.LOADING_INITIAL -> {
                        swipeRefreshLayout.isRefreshing = true
                    }

                    LoadingState.LOADING_MORE -> {
                        swipeRefreshLayout.isRefreshing = true
                    }

                    LoadingState.LOADED -> {
                        swipeRefreshLayout.isRefreshing = false
                    }

                    LoadingState.ERROR -> {
                        Toast.makeText(
                            context,
                            "Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                        swipeRefreshLayout.isRefreshing = false
                    }

                    LoadingState.FINISHED -> {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

}
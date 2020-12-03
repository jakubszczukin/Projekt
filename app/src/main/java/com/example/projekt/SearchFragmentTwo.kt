package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_oferty.*
import kotlinx.android.synthetic.main.fragment_oferty.view.*
import kotlinx.android.synthetic.main.fragment_search_two.*
import kotlinx.android.synthetic.main.fragment_search_two.view.*

class SearchFragmentTwo : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var mAdapter: FirestorePagingAdapter<MyOfert, OfertViewHolder>
    private val model: SearchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(
            com.example.projekt.R.layout.fragment_search_two,
            container,
            false
        )


        // Init RecyclerView
        val recyclerView = viewOfLayout.recyclerViewSearch
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        setupAdapter()

        // Refresh Action on Swipe Refresh Layout
        viewOfLayout.swipeRefreshLayoutTwo.setOnRefreshListener {
            mAdapter.refresh()
        }

        viewOfLayout.recyclerViewSearch.adapter = mAdapter

        return viewOfLayout
    }

    private fun setupAdapter(){

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val city = model.fCity.value.toString()

        val query = db.collection("oferty").whereEqualTo("miasto", city).whereNotEqualTo(
            "uzytkownik_id",
            auth.currentUser!!.uid
        )
        //val query = db.collection("oferty").whereEqualTo("miasto", "Lublin")

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
                        swipeRefreshLayoutTwo.isRefreshing = true
                    }

                    LoadingState.LOADING_MORE -> {
                        swipeRefreshLayoutTwo.isRefreshing = true
                    }

                    LoadingState.LOADED -> {
                        swipeRefreshLayoutTwo.isRefreshing = false
                    }

                    LoadingState.ERROR -> {
                        Toast.makeText(
                            context,
                            "Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                        swipeRefreshLayoutTwo.isRefreshing = false
                    }

                    LoadingState.FINISHED -> {
                        swipeRefreshLayoutTwo.isRefreshing = false
                        if (mAdapter.itemCount == 0) Log.d("TEST", "Brak wyników")
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
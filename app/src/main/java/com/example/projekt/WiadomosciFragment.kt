package com.example.projekt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_wiadomosci.view.*

class WiadomosciFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private val model: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater.inflate(
            com.example.projekt.R.layout.fragment_wiadomosci,
            container,
            false
        )

        viewOfLayout.search.setOnClickListener {
            val city = viewOfLayout.szukajText.text.toString()
            model.select(city)
            (activity as MainActivity).loadFragment(SearchFragmentTwo())
        }

        return viewOfLayout
    }

}
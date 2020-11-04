package com.example.projekt

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


class MyStepperAdapter(fm: FragmentManager?, context: Context?) :
    AbstractFragmentStepAdapter(fm!!, context!!) {

    private val CURRENT_STEP_POSITION_KEY = "messageResourceId"

    override fun createStep(position: Int): Step? {
        val b = Bundle()
        b.putInt(CURRENT_STEP_POSITION_KEY, position)
        when (position) {
            0 -> {
                val mRegisterFragmentOne = RegisterFragmentOne()
                mRegisterFragmentOne.arguments = b
                return mRegisterFragmentOne
            }
            1 -> {
                val mRegisterFragmentTwo = RegisterFragmentTwo()
                mRegisterFragmentTwo.arguments = b
                return mRegisterFragmentTwo
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getViewModel(position: Int): StepViewModel {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return StepViewModel.Builder(context)
            .setTitle("Test") //can be a CharSequence instead
            .create()
    }
}
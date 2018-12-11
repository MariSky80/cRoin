package com.croin.croin


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * @author Maricel Bros Maimó
 *
 * CalculatorFragment subclass.
 *
 */
class CalculatorFragment : Fragment() {

    /**
     * Overrides onCreateView default function from fragment behaviour.
     *
     * @param LayoutInflater
     * @param ViewGroup?
     * @param Bundle?
     *
     * @return View?
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_calculator, container, false)
    }

}

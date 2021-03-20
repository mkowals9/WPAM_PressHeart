package com.wpam.pressheart.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wpam.pressheart.R
import kotlinx.android.synthetic.main.main_view_window.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainViewWindow : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_view_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_log_in.setOnClickListener{
            findNavController().navigate(R.id.action_MainViewFragment_to_MainLoggedFragment)
        }

        button_sign_up.setOnClickListener{
            findNavController().navigate(R.id.action_MainViewFragment_to_SignUpFragment)
        }
//        view.findViewById<Button>(R.id.button_log_in).setOnClickListener {
//
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

}
package com.ecommerce.blum.vendedor.Nav_fragments_vendedor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.FragmentInicioVBinding
import com.ecommerce.blum.vendedor.Bottom_nav_fragments_vendedor.FragmentMisProductosV
import com.ecommerce.blum.vendedor.Bottom_nav_fragments_vendedor.FragmentOrdenesV

class FragmentInicioV : Fragment() {

    private lateinit var binding: FragmentInicioVBinding
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInicioVBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.op_mis_productos_v -> {
                    replaceFragment(FragmentMisProductosV())
                }
                R.id.op_mis_ordenes_v ->{
                    replaceFragment(FragmentOrdenesV())
                }

            }
            true
        }

        replaceFragment(FragmentMisProductosV())
        binding.bottomNavigation.selectedItemId = R.id.op_mis_productos_v

        binding.addFab.setOnClickListener {
            Toast.makeText(
                mContext,
                "Haz echo clic en el boton para agregar un nuevo producto",
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }


    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }

}
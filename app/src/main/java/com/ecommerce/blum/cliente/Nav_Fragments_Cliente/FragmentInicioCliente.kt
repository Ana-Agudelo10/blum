package com.ecommerce.blum.cliente.Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecommerce.blum.R
import com.ecommerce.blum.cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.ecommerce.blum.cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaC
import com.ecommerce.blum.databinding.ActivityLoginVendedorBinding
import com.ecommerce.blum.databinding.FragmentInicioClienteBinding


class FragmentInicioCliente : Fragment() {

    private lateinit var binding: FragmentInicioClienteBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentInicioClienteBinding.inflate(inflater, container, false)


        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.op_tienda_c -> {
                    replaceFragmente(FragmentTiendaC())
                }
                R.id.op_mis_ordenes_c -> {
                    replaceFragmente(FragmentMisOrdenesC())
                }

            }
            true
        }

        replaceFragmente(FragmentTiendaC())
        binding.bottomNavigation.selectedItemId = R.id.op_tienda_c

        return binding.root
    }

    private fun replaceFragmente(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()

    }


}
package com.ecommerce.blum.cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecommerce.blum.Adaptadores.AdaptadorCarritoC
import com.ecommerce.blum.Modelos.ModeloProductoCarrito
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.FragmentCarritoCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentCarritoC : Fragment() {

    private lateinit var binding : FragmentCarritoCBinding

    private lateinit var mContext : Context
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var productosArrayList : ArrayList<ModeloProductoCarrito>
    private lateinit var productoAdaptadorCarritoC: AdaptadorCarritoC


    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCarritoCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        cargarProdCarrito()
    }

    private fun cargarProdCarrito() {

        productosArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    productosArrayList.clear()

                    for(ds in snapshot.children){

                        val modeloProductoCarrito = ds.getValue(ModeloProductoCarrito::class.java)
                        productosArrayList.add(modeloProductoCarrito!!)
                    }
                    productoAdaptadorCarritoC = AdaptadorCarritoC (mContext, productosArrayList)
                    binding.carritoRv.adapter = productoAdaptadorCarritoC
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }


}
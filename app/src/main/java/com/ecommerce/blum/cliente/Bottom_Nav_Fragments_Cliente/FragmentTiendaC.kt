package com.ecommerce.blum.cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecommerce.blum.Adaptadores.AdaptadorCategoriaC
import com.ecommerce.blum.Modelos.ModeloCategoria
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.FragmentTiendaCBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentTiendaC : Fragment() {


    private lateinit var  binding: FragmentTiendaCBinding
    private lateinit var mContext: Context

    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoria: AdaptadorCategoriaC

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTiendaCBinding.inflate(LayoutInflater.from(mContext), container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listarCategorias()
    }

    private fun listarCategorias() {

        categoriaArrayList = ArrayList()
        var ref = FirebaseDatabase.getInstance().getReference("Categorias")
            .orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for(ds in snapshot.children){
                    val modeloCat =ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modeloCat!!)
                }

                adaptadorCategoria = AdaptadorCategoriaC(mContext, categoriaArrayList)
                binding.CategoriasRV.adapter = adaptadorCategoria

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })








    }


}
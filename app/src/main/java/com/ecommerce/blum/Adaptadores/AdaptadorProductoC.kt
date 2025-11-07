package com.ecommerce.blum.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.blum.Modelos.ModeloProducto
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ItemProductoCBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProductoC : RecyclerView.Adapter<AdaptadorProductoC.HolderProducto> {
    private lateinit var binding: ItemProductoCBinding

    private var mContex: Context
    private var productosArrayList: ArrayList<ModeloProducto>

    constructor(mContex: Context, productosArrayList: ArrayList<ModeloProducto>) {
        this.mContex = mContex
        this.productosArrayList = productosArrayList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderProducto {
        binding = ItemProductoCBinding.inflate(LayoutInflater.from(mContex), parent, false)
        return HolderProducto(binding.root)
    }

    override fun getItemCount(): Int {
        return productosArrayList.size
    }

    override fun onBindViewHolder(
        holder: HolderProducto,
        position: Int
    ) {
        val modeloProducto = productosArrayList[position]

        val nombre = modeloProducto.nombre
        val precio = modeloProducto.precio
        val precioDesc = modeloProducto.precioDesc
        val notaDesc = modeloProducto.notaDesc

        cargarPrimeraImagen(modeloProducto, holder)

        holder.item_nombre_p.text = "${nombre}"
        holder.item_precio_p.text = "${precio}${" COP"}"
        holder.item_precio_p_desc.text = "${precioDesc}"
        holder.item_nota_p.text = "${notaDesc}"


        /* Si el precio con desc y la nota no son campos vacios*/
        if (precioDesc.isNotEmpty() && notaDesc.isNotEmpty()) {
            visualizarDescuento(holder)
        }


    }

    private fun visualizarDescuento(holder: AdaptadorProductoC.HolderProducto) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val nota_Desc = "${ds.child("notaDesc").value}"
                    val precio_Desc = "${ds.child("precioDesc").value}"

                    if (nota_Desc.isNotEmpty() && precio_Desc.isNotEmpty()) {
                        //Habilitamos las vistas

                        holder.item_nota_p.visibility = View.VISIBLE
                        holder.item_precio_p_desc.visibility = View.VISIBLE
                        //Seteamos la info

                        holder.item_nota_p.text = "${nota_Desc}"
                        holder.item_precio_p_desc.text = "${precio_Desc}${" COP "}"

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
    }

    private fun cargarPrimeraImagen(
        modeloProducto: ModeloProducto, holder: AdaptadorProductoC.HolderProducto
    ) {

        val idProducto = modeloProducto.id
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContex)
                                .load(imagenUrl)
                                .placeholder(R.drawable.item_img_producto)
                                .into(holder.imagenP)

                        } catch (e: Exception) {

                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    inner class HolderProducto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_p_desc = binding.itemPrecioPDesc
        var item_nota_p = binding.itemNotaP
    }
}
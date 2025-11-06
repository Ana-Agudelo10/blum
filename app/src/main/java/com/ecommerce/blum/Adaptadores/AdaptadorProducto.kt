package com.ecommerce.blum.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.blum.Modelos.ModeloProducto
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ItemProductoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdaptadorProducto : RecyclerView.Adapter<AdaptadorProducto.HolderProducto> {

    private lateinit var binding : ItemProductoBinding

    private var mContex : Context
    private var productosArrayList : ArrayList<ModeloProducto>

    constructor(mContex: Context, productosArrayList: ArrayList<ModeloProducto>) {
        this.mContex = mContex
        this.productosArrayList = productosArrayList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderProducto {
       binding = ItemProductoBinding.inflate(LayoutInflater.from(mContex), parent, false)
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


    }

    private fun cargarPrimeraImagen(
        modeloProducto: ModeloProducto,
        holder: HolderProducto
    ) {

        val idProducto = modeloProducto.id
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        val imagenUrl ="${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContex)
                                .load(imagenUrl)
                                .placeholder(R.drawable.item_img_producto)
                                .into(holder.imagenP)

                        }catch (e:Exception){

                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    inner class HolderProducto(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_p_desc = binding.itemPrecioPDesc
    }


}
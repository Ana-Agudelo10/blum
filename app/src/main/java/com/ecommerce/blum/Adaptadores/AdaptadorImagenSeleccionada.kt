package com.ecommerce.blum.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.blum.R
import com.ecommerce.blum.Modelos.ModeloImagenSeleccionada
import com.ecommerce.blum.databinding.ItemImagenesSeleccionadasBinding

class AdaptadorImagenSeleccionada (
    private val context : Context,
    private val imagenesSelecArrayList : ArrayList<ModeloImagenSeleccionada>
    ): RecyclerView.Adapter<AdaptadorImagenSeleccionada.HolderImagenSeleccionada>() {

    private lateinit var binding : ItemImagenesSeleccionadasBinding

    // Muestra cada elemento que contiene la lista
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderImagenSeleccionada {
        binding = ItemImagenesSeleccionadasBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderImagenSeleccionada(binding.root)
    }

    // Pinta la información de cada elemento de la lista
    override fun onBindViewHolder(
        holder: HolderImagenSeleccionada,
        position: Int
    ) {
        val modelo = imagenesSelecArrayList[position]
        val imagenUri = modelo.imageUri

        //Leyendo la imagen(es)
        try {
            Glide.with(context)
                .load(imagenUri)
                .placeholder(R.drawable.item_imagen)
                .into(holder.imagenItem)
        } catch (e: Exception){

        }

        //Evento para eliminar una imagen de la lista
        holder.btn_borrar.setOnClickListener {
            imagenesSelecArrayList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    // Tamaño de la lista
    override fun getItemCount(): Int {
        return imagenesSelecArrayList.size
    }

    inner class HolderImagenSeleccionada(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imagenItem = binding.imagenItem
        var btn_borrar = binding.borrarItem
    }

}
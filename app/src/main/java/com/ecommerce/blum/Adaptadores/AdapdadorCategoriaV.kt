package com.ecommerce.blum.Adaptadores

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.blum.Modelos.ModeloCategoria
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ItemCategoriaVBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
/*import com.markwang.tiendavirtualapp_kotlin.Modelos.ModeloCategoria
import com.markwang.tiendavirtualapp_kotlin.R
import com.markwang.tiendavirtualapp_kotlin.Vendedor.Productos.ProductosCatVActivity
import com.markwang.tiendavirtualapp_kotlin.databinding.ItemCategoriaVBinding*/


class AdapdadorCategoriaV : RecyclerView.Adapter<AdapdadorCategoriaV.HolderCategoriaV> {

    private lateinit var binding : ItemCategoriaVBinding

    private val mContext : Context
    private val categoriaArrayList : ArrayList<ModeloCategoria>

    constructor(mContext: Context, categoriaArrayList: ArrayList<ModeloCategoria>){
        this.mContext = mContext
        this.categoriaArrayList = categoriaArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaV {
       binding = ItemCategoriaVBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaV(binding.root)
    }

    override fun getItemCount(): Int {
        return categoriaArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategoriaV, position: Int) {
        val modelo = categoriaArrayList[position]

        val id = modelo.id
        val categoria = modelo.categoria
        val imagen = modelo.imagenUrl

        holder.item_nombre_c_v.text = categoria


        holder.item_eliminar_c.setOnClickListener {
           // Toast.makeText(mContext, "Eliminar categoria", Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Eliminar categoria")
            builder.setMessage("¿Estás seguro de eliminar esta categoria?")
                .setPositiveButton("Confirmar"){a,d->
                    eliminarCategoria(modelo, holder)
                }
                .setNegativeButton("Cancelar"){a,d->
                    a.dismiss()
                }
            builder.show()
        }

    }

    private fun eliminarCategoria(modelo: ModeloCategoria, holder: AdapdadorCategoriaV.HolderCategoriaV) {
        val idCat = modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.child(idCat).removeValue()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Categoria eliminada ", Toast.LENGTH_SHORT).show()
                eliminarImgCat(idCat)
            }
            .addOnFailureListener {e->
                Toast.makeText(mContext, "No se eliminó la categoria debido a ${e.message} ", Toast.LENGTH_SHORT).show()

            }
    }

    private fun eliminarImgCat(idCat: String) {
        val nombreImg = idCat
        val rutaImagen = "Categorias/$nombreImg"
        val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageRef.delete()
            .addOnSuccessListener{
                Toast.makeText(mContext, "Se elimino la imagen de la categoria", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {e->
                Toast.makeText(mContext, " ${e.message} ", Toast.LENGTH_SHORT).show()

            }
    }



    inner class HolderCategoriaV(itemview : View) : RecyclerView.ViewHolder(itemview){
        var item_nombre_c_v = binding.itemNombreCV

        var item_eliminar_c = binding.itemEliminarC

    }

}
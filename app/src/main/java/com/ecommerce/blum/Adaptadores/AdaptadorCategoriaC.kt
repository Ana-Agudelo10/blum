package com.ecommerce.blum.Adaptadores


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.blum.Modelos.ModeloCategoria
import com.ecommerce.blum.R
import com.ecommerce.blum.cliente.ProductosC.ProductosCatCActivity
import com.ecommerce.blum.databinding.ItemCategoriaCBinding

class AdaptadorCategoriaC : RecyclerView.Adapter<AdaptadorCategoriaC.HolderCategoriaC> {
    private lateinit var binding : ItemCategoriaCBinding

    private var  mContext : Context
    private  var categoriaArrayList : ArrayList<ModeloCategoria>

    constructor(mContext: Context, categoriaArrayList: ArrayList<ModeloCategoria>) : super() {
        this.mContext = mContext
        this.categoriaArrayList = categoriaArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaC {
        binding = ItemCategoriaCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaC(binding.root)

    }

    override fun onBindViewHolder(
        holder: HolderCategoriaC,
        position: Int
    ) {
        var modelo = categoriaArrayList[position]

        val categoria = modelo.categoria
        val imagen = modelo.imagenUrl

        holder.item_nombre_c_c.text = categoria

        Glide.with(mContext)
            .load(imagen)
            .placeholder(R.drawable.categorias)
            .into(holder.item_img_cat)

        //Evento para ver productos de una categoria
        holder.item_ver_productos.setOnClickListener{
            val intent = Intent(mContext, ProductosCatCActivity::class.java)
            intent.putExtra("nombreCat", categoria)
            Toast.makeText(mContext,"Categoria Seleccionada ${categoria}", Toast.LENGTH_SHORT).show()
            mContext.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return  categoriaArrayList.size
    }

    inner class HolderCategoriaC (itemView : View) : RecyclerView.ViewHolder(itemView){
        var item_nombre_c_c = binding.itemNombreCC
        var item_img_cat = binding.imagenCateg
        var item_ver_productos = binding.itemVerProductos
    }
}
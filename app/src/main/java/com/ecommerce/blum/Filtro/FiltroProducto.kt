package com.ecommerce.blum.Filtro

import android.widget.Filter
import com.ecommerce.blum.Adaptadores.AdaptadorProductoC
import com.ecommerce.blum.Modelos.ModeloProducto
import java.util.Locale

class FiltroProducto (

    private val adaptador : AdaptadorProductoC, 
    private val filtroLista : ArrayList<ModeloProducto>
): Filter(){
    override fun performFiltering(filtro: CharSequence?): FilterResults? {
        var filtro = filtro
        val resultados = FilterResults()

        if(!filtro.isNullOrEmpty()){

            filtro = filtro.toString().uppercase(Locale.getDefault())
            val filtroProducto = ArrayList<ModeloProducto>()
            for(i in filtroLista.indices){
                
                if (filtroLista[i].nombre.uppercase(Locale.getDefault()).contains(filtro)){
                    filtroProducto.add(filtroLista[i])
                }
            }
            resultados.count = filtroProducto.size
            resultados.values = filtroProducto
        }else{
            resultados.count = filtroLista.size
            resultados.values = filtroLista
        }
        return resultados
    }

    override fun publishResults(filtro: CharSequence?, resultados: FilterResults) {

        adaptador.productosArrayList = resultados.values as ArrayList<ModeloProducto>
        adaptador.notifyDataSetChanged()

    }

}
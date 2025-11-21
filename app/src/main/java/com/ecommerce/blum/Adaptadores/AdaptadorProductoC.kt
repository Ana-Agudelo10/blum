package com.ecommerce.blum.Adaptadores

import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.blum.Filtro.FiltroProducto
import com.ecommerce.blum.Modelos.ModeloProducto
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ItemProductoCBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdaptadorProductoC : RecyclerView.Adapter<AdaptadorProductoC.HolderProducto>, Filterable {
    private lateinit var binding: ItemProductoCBinding

    private var mContex: Context
    var productosArrayList: ArrayList<ModeloProducto>
    private var filtroLista : ArrayList<ModeloProducto>
    private var filtro : FiltroProducto ?= null

    constructor(mContex: Context, productosArrayList: ArrayList<ModeloProducto>) {
        this.mContex = mContex
        this.productosArrayList = productosArrayList
        this.filtroLista = productosArrayList
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

        // Evento para agregar al carrito el producto seleccionado
        holder.agregar_carrito.setOnClickListener {
            verCarrito(modeloProducto)
        }

    }

    var costo : Double = 0.0
    var costoFinal : Double = 0.0
    var cantidadProd : Int = 0


    private fun verCarrito(modeloProducto: ModeloProducto) {
        var imagenSIV : ShapeableImageView
        var nombreTv : TextView
        var descripcionTv : TextView
        var notaDescTv : TextView
        var precioOriginalTv : TextView
        var precioDescuentoTv : TextView
        var precioFinalTv : TextView
        var btnDisminuir : ImageButton
        var btnAumentar : ImageButton
        var cantidadTv : TextView
        var btnAgregarCarrito : MaterialButton

        var dialog = Dialog(mContex)
        dialog.setContentView(R.layout.carrito_compras) //Hacemos referencia al carrito de compras

        imagenSIV = dialog.findViewById(R.id.imagenPCar)
        nombreTv = dialog.findViewById(R.id.nombrePCar)
        descripcionTv = dialog.findViewById(R.id.descripcionPCar)
        notaDescTv = dialog.findViewById(R.id.notaDescPCar)
        precioOriginalTv = dialog.findViewById(R.id.precioOriginalPCar)
        precioDescuentoTv = dialog.findViewById(R.id.precioDescPCar)
        precioFinalTv = dialog.findViewById(R.id.precioFinalPCar)
        btnDisminuir = dialog.findViewById(R.id.btnDisminuir)
        btnAumentar = dialog.findViewById(R.id.btnAumentar)
        cantidadTv = dialog.findViewById(R.id.cantidadPCar)
        btnAgregarCarrito = dialog.findViewById(R.id.btnAgregarCarrito)

        /*Obtener los datos o la información del modelo*/

        val productoId = modeloProducto.id
        val nombre = modeloProducto.nombre
        val descripcion = modeloProducto.descripcion
        val precio = modeloProducto.precio
        val precioDesc = modeloProducto.precioDesc
        val notaDesc = modeloProducto.notaDesc

        if(!precioDesc.equals("0") && !notaDesc.equals("")){

            /*El roducto si tiene descuento*/
            notaDescTv.visibility = View.VISIBLE
            precioDescuentoTv.visibility = View.VISIBLE

            notaDescTv.setText(notaDesc)
            precioDescuentoTv.setText(precioDesc.plus("USD"))
            precioOriginalTv.setText(precio.plus("USD"))
            precioOriginalTv.paintFlags = precioOriginalTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG //Marca como tachado
            costo = precioDesc.toDouble() /*Precio almacena el precio con descuento*/

        } else {
            /*El producto no tiene descuento*/
            precioOriginalTv.setText(precio.plus("USD"))
            precioOriginalTv.paintFlags = precioOriginalTv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() //Omitimos el tachado
            costo = precio.toDouble() /*Precio almacena el precio original*/
        }

        /*Setear la información*/
        nombreTv.setText(nombre)
        descripcionTv.setText(descripcion)

        costoFinal = costo
        cantidadProd = 1

        /*Incrementar cantidad*/
        btnAumentar.setOnClickListener {
            costoFinal = costoFinal + costo
            cantidadProd++

            precioFinalTv.text = costoFinal.toString()
            cantidadTv.text = cantidadProd.toString()
        }

        /*Disminuir cantidad*/
        btnDisminuir.setOnClickListener {
            /*Disminuir solo si la cantidad es mayor a 1*/
            if(cantidadProd > 1){
                costoFinal = costoFinal-costo
                cantidadProd--

                precioFinalTv.text = costoFinal.toString()
                cantidadTv.text = cantidadProd.toString()
            }
        }

        precioFinalTv.text = costo.toString()

       /*Obtener la primera imagen del producto */
        cargarImg(productoId, imagenSIV)

        btnAgregarCarrito.setOnClickListener {
            agregarCarrito(mContex, modeloProducto, costoFinal, cantidadProd)
        }
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)

    }

    private fun AdaptadorProductoC.agregarCarrito(mContex: Context, modeloProducto: ModeloProducto, costoFinal: Double, cantidadProd: Int) {
        val firebaseAuth = FirebaseAuth.getInstance()

        val hashMap = HashMap<String, Any>()
        hashMap["idProducto"] = "${modeloProducto.id}"
        hashMap["nombre"] = modeloProducto.nombre
        hashMap["precio"] = modeloProducto.precio
        hashMap["precioDesc"] = modeloProducto.precioDesc
        hashMap["precioFinal"] = costoFinal.toString()
        hashMap["cantidad"] = cantidadProd

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras").child(modeloProducto.id)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(mContex, "Se agregó al carrito el producto", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(mContex,"${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
    private fun cargarImg(productoId: String, imagenSIV: ShapeableImageView) {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(productoId).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                     //Extraer la url de la primera imagen
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContex)
                                .load(imagenUrl)
                                .placeholder(R.drawable.item_img_producto)
                                .into(imagenSIV)
                        }catch(e:Exception){

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

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
                        holder.item_precio_p.paintFlags = holder.item_precio_p.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

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

    override fun getFilter(): Filter {
        if (filtro == null){
            filtro = FiltroProducto(this, filtroLista)
        }
        return filtro as FiltroProducto
    }


    inner class HolderProducto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_p_desc = binding.itemPrecioPDesc
        var item_nota_p = binding.itemNotaP
        var agregar_carrito = binding.itemAgregarCarritoP
    }
}

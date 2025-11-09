package com.ecommerce.blum.Modelos

class ModeloProductoCarrito {

    var idProducto : String = ""
    var nombre : String = ""
    var precio: String = ""
    var precioFinal : String = ""
    var precioDesc : String = ""
    var cantidad : Int = 0

    constructor()
    constructor(
        idProducto: String,
        nombre: String,
        precio: String,
        precioFinal: String,
        precioDesc: String,
        cantidad: Int
    ) {
        this.idProducto = idProducto
        this.nombre = nombre
        this.precio = precio
        this.precioFinal = precioFinal
        this.precioDesc = precioDesc
        this.cantidad = cantidad
    }

}
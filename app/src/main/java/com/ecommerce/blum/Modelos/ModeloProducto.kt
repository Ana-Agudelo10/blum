package com.ecommerce.blum.Modelos

class ModeloProducto {

    var  id : String = ""
    var nombre : String = ""
    var descripcion : String = ""
    var categoria : String = ""
    var precio  : String = ""
    var precioDesc : String = ""
    var notaDesc : String = ""


    constructor()
    constructor(
        id: String,
        nombre: String,
        descripcion: String,
        categoria: String,
        precio: String,
        precioDesc: String,
        notaDesc: String
    ) {
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.categoria = categoria
        this.precio = precio
        this.precioDesc = precioDesc
        this.notaDesc = notaDesc
    }


}
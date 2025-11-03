package com.ecommerce.blum.Modelos

class ModeloCategoria {

    var id : String = ""
    var categoria : String = ""
    var imagenUrl : String = ""

    constructor()

    constructor(categoria: String, id: String) {
        this.categoria = categoria
        this.id = id
        this.imagenUrl = imagenUrl
    }


}
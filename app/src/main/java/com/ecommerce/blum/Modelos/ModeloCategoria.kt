package com.ecommerce.blum.Modelos

class ModeloCategoria {

    var id : String = ""
    var categoria : String = ""

    constructor()

    constructor(categoria: String, id: String) {
        this.categoria = categoria
        this.id = id
    }


}
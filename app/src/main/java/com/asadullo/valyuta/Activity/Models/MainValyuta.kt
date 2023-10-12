package com.asadullo.valyuta.Activity.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MainValyuta{
    @PrimaryKey()
    var id:Int? = null
    var title:String? = null
    var code:String? = null
    var cb_price:String? = null
    var nbu_buy_price:String? = null
    var nbu_cell_price:String? = null
    var date:String? = null




    constructor()
    constructor(
        title: String?,
        code: String?,
        cb_price: String?,
        nbu_buy_price: String?,
        nbu_cell_price: String?,
        date: String?
    ) {
        this.title = title
        this.code = code
        this.cb_price = cb_price
        this.nbu_buy_price = nbu_buy_price
        this.nbu_cell_price = nbu_cell_price
        this.date = date
    }

    constructor(
        id: Int?,
        title: String?,
        code: String?,
        cb_price: String?,
        nbu_buy_price: String?,
        nbu_cell_price: String?,
        date: String?
    ) {
        this.id = id
        this.title = title
        this.code = code
        this.cb_price = cb_price
        this.nbu_buy_price = nbu_buy_price
        this.nbu_cell_price = nbu_cell_price
        this.date = date
    }


}




package com.asadullo.valyuta.Activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.asadullo.valyuta.Activity.Adapters.KursAdapter
import com.asadullo.valyuta.Activity.DB.DbHelper
import com.asadullo.valyuta.Activity.Models.MainValyuta
import com.asadullo.valyuta.Activity.Network.NetworkHP
import com.asadullo.valyuta.databinding.ActivityHomeBinding
import com.asadullo.valyuta.databinding.ItemInfoBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private lateinit var adapter: KursAdapter
    private lateinit var requestQueue: RequestQueue
    private lateinit var dbHelper: DbHelper
    private lateinit var networkHP: NetworkHP
    private lateinit var offlineList:ArrayList<MainValyuta>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dbHelper = DbHelper.getIns(this)
        networkHP = NetworkHP(this)

        if (networkHP.isNetworkConnected()){
            trueNetwork()
        }else{
            falseNerwork()
        }
    }

    fun trueNetwork(){
        Toast.makeText(this, "Online rejim", Toast.LENGTH_SHORT).show()
        binding.online.visibility = View.GONE
        requestQueue = Volley.newRequestQueue(this)
        VolleyLog.DEBUG = true
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "https://nbu.uz/uz/exchange-rates/json/", null,
            { response ->

                val type = object : TypeToken<ArrayList<MainValyuta>>(){}.type
                val list = Gson().fromJson<ArrayList<MainValyuta>>(response.toString(), type)

                binding.info.setOnClickListener {
                    val alertDialog = AlertDialog.Builder(this)
                    val dialog = alertDialog.create()

                    dialog.setTitle("Ma'lumot")
                    val dialogView = ItemInfoBinding.inflate(layoutInflater)
                    dialog.setView(dialogView.root)

                    dialogView.dateInfo.text = "Kurslarning yangilangan sanasi: ${list[0].date}"

                    dialog.show()
                }

                binding.valyutaCursUsa.text = list[23].cb_price
                binding.valyutaCursRus.text = list[18].cb_price
                binding.valyutaCurs.text = list[7].cb_price

                binding.usa.setOnClickListener {
                    var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                    intent.putExtra("title", list[23].title)
                    intent.putExtra("code", list[23].code)
                    intent.putExtra("price", list[23].cb_price)
                    intent.putExtra("buy", list[23].nbu_buy_price)
                    intent.putExtra("cell", list[23].nbu_cell_price)
                    intent.putExtra("date", list[23].date)
                    startActivity(intent)
                }

                binding.rus.setOnClickListener {
                    var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                    intent.putExtra("title", list[18].title)
                    intent.putExtra("code", list[18].code)
                    intent.putExtra("price", list[18].cb_price)
                    intent.putExtra("buy", list[18].nbu_buy_price)
                    intent.putExtra("cell", list[18].nbu_cell_price)
                    intent.putExtra("date", list[18].date)
                    startActivity(intent)
                }

                binding.yevro.setOnClickListener {
                    var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                    intent.putExtra("title", list[7].title)
                    intent.putExtra("code", list[7].code)
                    intent.putExtra("price", list[7].cb_price)
                    intent.putExtra("buy", list[7].nbu_buy_price)
                    intent.putExtra("cell", list[7].nbu_cell_price)
                    intent.putExtra("date", list[7].date)
                    startActivity(intent)
                }

                adapter = KursAdapter(list, object : KursAdapter.Click{
                    override fun click(position: Int, list: List<MainValyuta>) {
                        var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                        intent.putExtra("title", list[position].title)
                        intent.putExtra("code", list[position].code)
                        intent.putExtra("price", list[position].cb_price)
                        intent.putExtra("buy", list[position].nbu_buy_price)
                        intent.putExtra("cell", list[position].nbu_cell_price)
                        intent.putExtra("date", list[position].date)
                        startActivity(intent)
                    }
                })
                binding.rvKurslar.adapter = adapter


                dbHelper.clearAllTables()
                list.forEach {
                    dbHelper.dao().add(it)
                }

                Log.d(TAG, "onResponse: $response")
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Volley Error: ${error.message}", error)
            }
        )

        jsonArrayRequest.tag = "tag1"
        requestQueue.add(jsonArrayRequest)
    }

    fun falseNerwork(){
        binding.root.visibility = View.GONE
        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("Internet Bilan Aloqa Yo'q")

        dialog.setCancelable(false)

        dialog.setPositiveButton("Qaytadan"
        ) { dialog, which ->
            if (networkHP.isNetworkConnected()){
                binding.root.visibility = View.VISIBLE
                trueNetwork()
            }else{
                falseNerwork()
            }
        }

        dialog.setNegativeButton("Offline rejim"){dialog,which ->
            binding.root.visibility = View.VISIBLE
            offlineListFun()
        }


        dialog.show()
    }

    fun offlineListFun(){
        try {
            Toast.makeText(this, "Offline rejim", Toast.LENGTH_SHORT).show()
            binding.online.visibility = View.VISIBLE
            binding.online.setOnClickListener {
                if (networkHP.isNetworkConnected()){
                    trueNetwork()
                }else{
                    Toast.makeText(this, "Internet aloqasi mavjud emas", Toast.LENGTH_SHORT).show()
                }
            }
            var list = dbHelper.dao().get()
            adapter = KursAdapter(list as ArrayList<MainValyuta>, object : KursAdapter.Click {
                override fun click(position: Int, list: List<MainValyuta>) {
                    var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                    intent.putExtra("title", list[position].title)
                    intent.putExtra("code", list[position].code)
                    intent.putExtra("price", list[position].cb_price)
                    intent.putExtra("buy", list[position].nbu_buy_price)
                    intent.putExtra("cell", list[position].nbu_cell_price)
                    intent.putExtra("date", list[position].date)
                    startActivity(intent)
                }
            })

            binding.rvKurslar.adapter = adapter

            binding.info.setOnClickListener {
                val alertDialog = AlertDialog.Builder(this)
                val dialog = alertDialog.create()

                dialog.setTitle("Ma'lumot")
                val dialogView = ItemInfoBinding.inflate(layoutInflater)
                dialog.setView(dialogView.root)

                dialogView.dateInfo.text = "Kurslarning yangilangan sanasi: ${list[0].date}"

                dialog.show()
            }

            binding.valyutaCursUsa.text = list[23].cb_price
            binding.valyutaCursRus.text = list[18].cb_price
            binding.valyutaCurs.text = list[7].cb_price

            binding.usa.setOnClickListener {
                var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                intent.putExtra("title", list[23].title)
                intent.putExtra("code", list[23].code)
                intent.putExtra("price", list[23].cb_price)
                intent.putExtra("buy", list[23].nbu_buy_price)
                intent.putExtra("cell", list[23].nbu_cell_price)
                intent.putExtra("date", list[23].date)
                startActivity(intent)
            }

            binding.rus.setOnClickListener {
                var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                intent.putExtra("title", list[18].title)
                intent.putExtra("code", list[18].code)
                intent.putExtra("price", list[18].cb_price)
                intent.putExtra("buy", list[18].nbu_buy_price)
                intent.putExtra("cell", list[18].nbu_cell_price)
                intent.putExtra("date", list[18].date)
                startActivity(intent)
            }

            binding.yevro.setOnClickListener {
                var intent = Intent(this@HomeActivity, AboutKurs::class.java)
                intent.putExtra("title", list[7].title)
                intent.putExtra("code", list[7].code)
                intent.putExtra("price", list[7].cb_price)
                intent.putExtra("buy", list[7].nbu_buy_price)
                intent.putExtra("cell", list[7].nbu_cell_price)
                intent.putExtra("date", list[7].date)
                startActivity(intent)
            }
        }catch (e:Exception){
            val dialog = AlertDialog.Builder(this)

            dialog.setTitle("Offline rejimga kirish uchun hech bo'lmasa bir marotaba \n" +
                    "online rejimda bo'lishingiz kerak")

            dialog.setCancelable(false)

            dialog.setPositiveButton("Online"
            ) { dialog, which ->
                if (networkHP.isNetworkConnected()){
                    binding.root.visibility = View.VISIBLE
                    trueNetwork()
                }else{
                    falseNerwork()
                }
            }


            dialog.show()
        }
    }

}

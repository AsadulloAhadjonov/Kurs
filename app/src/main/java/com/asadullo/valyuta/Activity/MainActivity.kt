package com.asadullo.valyuta.Activity

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityOptionsCompat
import com.asadullo.valyuta.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyTask().execute()
    }

    inner class MyTask: AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg params: Void?): Void? {
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
                val options = ActivityOptionsCompat.makeCustomAnimation(this@MainActivity,
                    R.anim.anim_1,
                    R.anim.anim_2
                )
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent, options.toBundle())
            }, 2000)
        }

    }
}
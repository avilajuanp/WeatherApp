package com.gncelectronic.climaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gncelectronic.climaapp.adapters.DiaAdaptador
import com.gncelectronic.climaapp.modelo.Dia
import kotlinx.android.synthetic.main.activity_clima_semanal.*

class ClimaSemanal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clima_semanal)
        intent.let{
            //rellenar la vista
            val dias: ArrayList<Dia>? = it.getParcelableArrayListExtra(MainActivity.CLIMA_SEMANAL)
            val baseAdapter = dias?.let {
                it1 -> DiaAdaptador(this, it1)      }
            listViewSemanal.adapter= baseAdapter
        }
    }
}

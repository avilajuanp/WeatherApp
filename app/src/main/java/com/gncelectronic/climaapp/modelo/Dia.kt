package com.gncelectronic.climaapp.modelo

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class Dia(var time: Long, val minTemp: Double, val maxTemp: Double) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(time)
        dest?.writeDouble(minTemp)
        dest?.writeDouble(maxTemp)

    }

    override fun describeContents(): Int {
       return 0
    }

    companion object CREATOR : Parcelable.Creator<Dia> {
        override fun createFromParcel(parcel: Parcel): Dia {
            return Dia(parcel)
        }

        override fun newArray(size: Int): Array<Dia?> {
            return arrayOfNulls(size)
        }
    }

    fun getFormattedTime():String{
        val formatter = SimpleDateFormat("EEEE",Locale.US) //Lunes , Martes ..
        val date = Date(time*1000)
        val diaSemana = formatter.format(date)
        return diaSemana
    }

}
package com.gncelectronic.climaapp.api
import com.gncelectronic.climaapp.extensions.iterator

import com.gncelectronic.climaapp.modelo.ClimaActual
import com.gncelectronic.climaapp.modelo.Dia
import org.json.JSONObject

class JSONParser {

    fun getClimaActualFromJson(response: JSONObject) :ClimaActual{
        val currentJSON = response.getJSONObject(currently)
        with(currentJSON){
            val climaActual = ClimaActual(
                icon = getString("icon"),
                summary = getString("summary"),
                temperature = getDouble("temperature"),
                precipProb= getDouble("precipProbability")

            )
            return climaActual
        }
    }


    fun getClimaSemanalFromJson(response: JSONObject):ArrayList<Dia>{
        val semanaJSON = response.getJSONObject(daily)
        val diaJSONArray = semanaJSON.getJSONArray(data)
        val dias = ArrayList<Dia>()

        for (diaJson in diaJSONArray){
            val minTemp = diaJson.getDouble(temperatureMin)
            val maxTemp = diaJson.getDouble(temperatureMax)
            val time = diaJson.getLong(time)
            dias.add(Dia(time,minTemp,maxTemp))
        }


        return dias

    }


    fun getTimeZone(response: JSONObject):String{
        return response.getString("timezone")
    }
}
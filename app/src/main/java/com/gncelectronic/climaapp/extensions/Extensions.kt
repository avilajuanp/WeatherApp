package com.gncelectronic.climaapp.extensions

import org.json.JSONArray
import org.json.JSONObject

operator fun JSONArray.iterator():Iterator<JSONObject> =(0 until length()).asSequence().map {
    get(it) as JSONObject}.iterator()

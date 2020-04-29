package com.benhan.bluegreen

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class ValidateRequest(email: String, listener: Response.Listener<String>): StringRequest(Request.Method.POST, URL, listener, null) {

    companion object{

        const val URL: String = "http://ec2-13-58-70-95.us-east-2.compute.amazonaws.com/emailvalidate.php"

    }


    val map = mutableMapOf<String, String>(

        "email" to email
    )

    override fun getParams(): MutableMap<String, String> {
        return map
    }

}
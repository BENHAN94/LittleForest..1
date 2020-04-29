package com.benhan.bluegreen

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.net.URL

class LoginRequest(email: String, password: String, listener: Response.Listener<String>):
    StringRequest(Method.POST, URL, listener, null) {


    // 서버 URL 설정(PHP 파일 연동)

    companion object {
        const val URL = "http://ec2-3-15-180-129.us-east-2.compute.amazonaws.com/login.php"
    }






    var map = mutableMapOf<String, String>(

        "email" to email, "password" to password)


    override fun getParams(): MutableMap<String, String> {
        return map
    }
}
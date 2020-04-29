package com.benhan.bluegreen

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.net.URL

class RegisterRequest(email: String, password: String, nickname: String, birthday: String, listener: Response.Listener<String>):
    StringRequest(Method.POST, URL, listener, null) {


    // 서버 URL 설정(PHP 파일 연동)

    companion object {
        const val URL = "http://ec2-13-58-70-95.us-east-2.compute.amazonaws.com/register.php"
    }






    private var map = mutableMapOf<String, String>(

        "email" to email, "password" to password, "nickname" to nickname, "birthday" to birthday
    )


    override fun getParams(): MutableMap<String, String> {
        return map
    }
}
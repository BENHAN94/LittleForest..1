package com.benhan.bluegreen

import android.content.ContentValues
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class RequestHttpConnection {

    fun request(_url: String, _params: ContentValues?): String? {



        val sbParams: StringBuffer = StringBuffer()

        if (_params == null)
            sbParams.append("")
        else {

            var isAnd = false

            /**
             * 1. StringBuffer에 파라미터 연결
             */

            for (parameter: Map.Entry<String, Any> in _params.valueSet()) {
                var key = parameter.key
                var value = parameter.value.toString()

                if (isAnd)
                    sbParams.append("&")

                sbParams.append(key).append("=").append(value)

                if(!isAnd)

                    if (_params.size() >= 2)
                        isAnd=true

            }


        }


        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         */

        var url = URL(_url)
        var urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection


        try{




            urlConn.setRequestMethod("POST")
            urlConn.setRequestProperty("Accept-Charset", "UTF-8")
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8")

            var strParams = sbParams.toString()
            var os: OutputStream = urlConn.outputStream
            var writer: Writer = OutputStreamWriter(os, Charset.forName("UTF-8"))
            writer.write(strParams)
            writer.flush()
            writer.close()

            if (urlConn.responseCode != HttpURLConnection.HTTP_OK)
                return null!!

            val reader: BufferedReader = BufferedReader(InputStreamReader(urlConn.inputStream, "UTF-8"))

            var line = reader.readLine()
            var page:String = ""

            while (line != null) {

                page += line
            }


            return page



        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if(urlConn != null)
                urlConn.disconnect()
        }

        return null!!
    }


}
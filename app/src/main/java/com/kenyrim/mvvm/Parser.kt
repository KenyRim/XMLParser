package com.kenyrim.mvvm;


import android.content.Context
import android.util.Log
import com.kenyrim.mvvm.storage.DataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.parser.Parser.xmlParser
import org.jsoup.select.Elements
import java.net.URL

class Parser {

    private val baseUrl = "https://lenta.ru/rss/news/"



    suspend fun parseArticles(listener: MainActivity): ArrayList<ArticleModel> {
        val data = ArrayList<ArticleModel>()
        val database = DataBase(listener)

        val parse: Deferred<ArrayList<ArticleModel>> = CoroutineScope(Dispatchers.IO).async {

            try {
                val doc = Jsoup.parse(URL(baseUrl).openStream(), "UTF-8", baseUrl, xmlParser())
                val elements: Elements = doc.select("item")
                val channelElements: Elements = doc.select("image")

                for (element in elements) {


                    var image = element.select("enclosure").attr("url")
                    if (image.isBlank()) {
                        image = channelElements.select("url").first().text()
                    }

                    database.insert(
                        ArticleModel(
                            0,
                            "",
                            "",
                            element.select("link").text(),
                            "",
                            image,
                            "",
                            ""
                        )
                    )

                }
            } catch (e: HttpStatusException) {
            }

            data
        }

        parse.await()
        database.close()
        listener.getResult()

        return data
    }


}

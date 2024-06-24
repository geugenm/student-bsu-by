package github.alexzhirkevich.studentbsuby.repo

import android.net.Uri
import github.alexzhirkevich.studentbsuby.api.ProfileApi
import github.alexzhirkevich.studentbsuby.dao.NewsDao
import github.alexzhirkevich.studentbsuby.data.models.News
import github.alexzhirkevich.studentbsuby.data.models.NewsContent
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: ProfileApi, private val dao: NewsDao, val baseUrl: Uri
                                        ) : CacheWebRepository<List<News>>()
{

    val newsUrl get() = "$baseUrl/${ProfileApi.URL_NEWS}"

    override suspend fun getFromCache(): List<News>? = kotlin.runCatching {
        dao.getAll()
    }.getOrNull()

    override suspend fun getFromWeb(): List<News>
    {
        return Jsoup.parse(api.news().html()).getElementsByClass("LineMain").first()
            ?.getElementsByTag("tbody")?.first()?.getElementsByTag("td")?.last()?.children()
            ?.apply {
                removeAll {
                    it.tag() == Tag.valueOf("br")
                }
            }?.drop(1)

            ?.groupUnless {
                it.tag() == Tag.valueOf("h2")
            }?.filter {
                it.isNotEmpty() && it[0].getElementsByAttributeValueContaining("href", "id=")
                    .isNotEmpty()
            }?.map {
                News(
                    id = it[0].getElementsByAttributeValueContaining("href", "id=").first()
                        ?.attr("href")?.substringAfter("=", "0")?.toInt() ?: 0,
                    title = it[0].text().trim(),
                    preview = it.filterNot {
                        it.allElements.any {
                            it.hasAttr("href")
                        }
                    }.takeIf(Collection<*>::isNotEmpty)
                        ?.joinToString("\n", transform = Element::text)?.trim()
                    )
            } ?: emptyList()

    }

    override suspend fun saveToCache(value: List<News>)
    {
        kotlin.runCatching {
            dao.clear()
            value.forEach { dao.insert(it) }
        }
    }

    fun getNewsItem(id: Int, dataSource: DataSource) = NewsContentRepository(
        id, dao, api
                                                                            ).get(dataSource)
}

fun <T> List<T>.groupUnless(predicate: (T) -> Boolean): List<List<T>>
{
    val result = mutableListOf<List<T>>()

    var start = 0
    forEachIndexed { index, t ->
        if (predicate(t) || index == size - 1)
        {
            if (index - start != 0) result.add(subList(start, index))
            start = index
        }
    }
    return result
}

class NewsContentRepository(
    private val id: Int, private val newsDao: NewsDao, private val profileApi: ProfileApi
                           ) : CacheWebRepository<NewsContent>()
{

    override suspend fun getFromCache(): NewsContent? = kotlin.runCatching {
        newsDao.getContent(id)
    }.getOrNull()

    override suspend fun getFromWeb(): NewsContent
    {
        return Jsoup.parse(profileApi.newsItem(id).html())
            .getElementById("ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_frmNewsItem")
            ?.apply {
                select("img").forEach {
                    it.attr("width", "100%")
                }
            }?.html().let {
                NewsContent(id, it)
            }
    }

    override suspend fun saveToCache(value: NewsContent)
    {
        kotlin.runCatching {
            newsDao.insertContent(value)
        }
    }

}
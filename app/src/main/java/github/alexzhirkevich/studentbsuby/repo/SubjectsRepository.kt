package github.alexzhirkevich.studentbsuby.repo

import android.content.SharedPreferences
import github.alexzhirkevich.studentbsuby.api.AllSubjectsRequest
import github.alexzhirkevich.studentbsuby.api.ProfileApi
import github.alexzhirkevich.studentbsuby.api.isSessionExpired
import github.alexzhirkevich.studentbsuby.dao.SubjectsDao
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.util.exceptions.EmptyResponseException
import github.alexzhirkevich.studentbsuby.util.exceptions.FailResponseException
import github.alexzhirkevich.studentbsuby.util.exceptions.IncorrectResponseException
import github.alexzhirkevich.studentbsuby.util.exceptions.SessionExpiredException
import github.alexzhirkevich.studentbsuby.util.exceptions.UsernameNotFoundException
import kotlinx.coroutines.flow.Flow
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_CURRENTSEMESTER_ = "PREF_CURRENTSEMESTER_"

@Singleton
class SubjectsRepository @Inject constructor(
    private val usernameProvider: UsernameProvider,
    private val profileApi: ProfileApi,
    private val subjectsDao: SubjectsDao,
                                            ) : CacheWebRepository<List<List<Subject>>>()
{

    override fun get(
        dataSource: DataSource,
        replaceCacheIf: (cached: List<List<Subject>>?, new: List<List<Subject>>) -> Boolean
                    ): Flow<List<List<Subject>>> = super.get(dataSource) { old, new ->
        replaceCacheIf(old, new) && new.isNotEmpty()
    }

    override suspend fun getFromWeb(): List<List<Subject>>
    {
        // TODO: refactor
        val username = usernameProvider.username

        if (username.isEmpty()) throw UsernameNotFoundException()

        val string = profileApi.subjects(profileApi.AllSubjectsRequest).html()

        if (!string.contains("updatePanel"))
        {
            throw IncorrectResponseException()
        }

        val firstTableIndex = string.indexOfFirst { it == '<' }
        val lastTableIndex = string.indexOfLast { it == '>' }

        if (firstTableIndex == -1 || lastTableIndex == -1) throw IncorrectResponseException()

        val html = string.substring(firstTableIndex, lastTableIndex)

        val jsoup = Jsoup.parse(html)

        val numbers = jsoup.getElementsByClass("styleNumberBody").map {
                it.text().toIntOrNull()
            }
        val lessons = jsoup.getElementsByClass("styleLessonBody").map {
            it.text()
        }
        val hours = jsoup.getElementsByClass("styleHoursSmallBody").map {
            it.text().toIntOrNull() ?: 0
        }.chunked(6)
        val zach = jsoup.getElementsByClass("styleZachBody").map {
            it.text().takeIf { !it.contains("&nbsp") && it.isNotEmpty() && it.isNotBlank() }
        }
        val exam = jsoup.getElementsByClass("styleExamBody").map {
            it.text().takeIf { it.contains("&nbsp").not() && it.isNotEmpty() && it.isNotBlank() }
        }

        //not same size
        if (setOf(
                numbers.size,
                lessons.size,
                hours.size,
                zach.size,
                exam.size
                 ).size != 1
        ) throw IncorrectResponseException()

        val subjects = mutableListOf<List<Subject>>()
        var semester = 1
        var list = mutableListOf<Subject>()

        var prevNumber = Int.MIN_VALUE

        for (i in numbers.indices)
        {
            val number = numbers[i] ?: continue
            if (prevNumber > number)
            {
                subjects.add(list)
                semester++
                list = mutableListOf()
            }

            prevNumber = number
            list.add(
                Subject(
                    semester = semester,
                    owner = username,
                    name = lessons[i],
                    lectures = hours[i][0],
                    practice = hours[i][1],
                    labs = hours[i][2],
                    seminars = hours[i][3],
                    facults = hours[i][4],
                    ksr = hours[i][5],
                    hasCredit = zach[i] != null,
                    creditPassed = if (zach[i]?.contains("+") == false && zach[i]?.contains("-") == false) null else zach[i]?.contains(
                        "+"
                                                                                                                                      ),
                    creditMark = zach[i]?.filter { it.isDigit() }?.toIntOrNull(),
                    creditRetakes = zach[i]?.filter { it == '\'' }?.length ?: 0,
                    hasExam = exam[i] != null,
                    examMark = exam[i]?.filter(Char::isDigit)?.toIntOrNull(),
                    examRetakes = exam[i]?.filter { it == '\'' }?.length ?: 0
                       )
                    )
            if (i == numbers.size - 1)
            {
                subjects.add(list)
            }
        }
        return subjects
    }

    override suspend fun saveToCache(value: List<List<Subject>>)
    {
        kotlin.runCatching {
            val subjects = value.flatten()
            if (subjects.isNotEmpty())
            {
                subjectsDao.clear(subjects[0].owner)
                subjects.forEach {
                    subjectsDao.insert(it)
                }
            }
        }
    }

    override suspend fun getFromCache(): List<List<Subject>>?
    {
        return kotlin.runCatching {
            usernameProvider.username.takeIf(String::isNotEmpty)?.let { login ->
                    subjectsDao.getAll(login).groupBy { it.semester }.values.toList()
                }
        }.getOrNull()
    }
}

class CurrentSemesterRepository @Inject constructor(
    private val usernameProvider: UsernameProvider,
    private val profileApi: ProfileApi,
    private val sharedPreferences: SharedPreferences
                                                   ) : CacheWebRepository<Int>()
{
    override suspend fun getFromCache(): Int?
    {
        return kotlin.runCatching {
            val username = usernameProvider.username
            if (username.isNotEmpty())
            {
                val semester = sharedPreferences.getInt(PREF_CURRENTSEMESTER_ + username, -1)
                semester.takeIf { it >= 0 }
            } else null
        }.getOrNull()
    }

    override suspend fun getFromWeb(): Int?
    {

        val resp = profileApi.studProgress()
        if (!resp.isSuccessful) throw FailResponseException(resp.code())

        if (resp.body()?.isSessionExpired == true) throw SessionExpiredException()


        val bytes = resp.body()?.byteStream()?.readBytes() ?: throw EmptyResponseException()
        val string = String(bytes)
        val semesterId =
            "ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ctlStudProgress1_selSemester"
        val jsoup = Jsoup.parse(string)
        var sem = 1
        return kotlin.runCatching {
            do
            {
                val elem = jsoup.getElementById("$semesterId$sem")?.also {
                    if (it.html().contains("<b>", true)) return@runCatching sem - 1
                    else sem++
                }
            } while (elem != null)
            return@runCatching null
        }.getOrNull()
    }

    override suspend fun saveToCache(value: Int)
    {
        kotlin.runCatching {
            val username = usernameProvider.username
            if (username.isNotEmpty())
            {
                sharedPreferences.edit().putInt(PREF_CURRENTSEMESTER_ + username, value).apply()
            }
        }
    }
}
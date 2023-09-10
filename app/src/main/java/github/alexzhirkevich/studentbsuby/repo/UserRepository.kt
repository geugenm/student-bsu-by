package github.alexzhirkevich.studentbsuby.repo

import github.alexzhirkevich.studentbsuby.api.ProfileApi
import github.alexzhirkevich.studentbsuby.dao.UsersDao
import github.alexzhirkevich.studentbsuby.data.models.User
import org.jsoup.Jsoup
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UsersDao,
    private val api: ProfileApi,
    private val usernameProvider: UsernameProvider
                                        ) : CacheWebRepository<User>()
{

    override suspend fun getFromCache(): User? =
        usernameProvider.username.takeIf(String::isNotBlank)
            ?.let { kotlin.runCatching { dao.get(it) }.getOrNull() }

    override suspend fun getFromWeb(): User
    {

        val doc = Jsoup.parse(api.studProgress().html())

        val name = doc.getElementById("ctl00_ctl00_ContentPlaceHolder0_lbFIO1").text()
        val faculty =
            doc.getElementById("ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ctlStudProgress1_lbStudFacultet")
                .text()
        val info =
            doc.getElementById("ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ctlStudProgress1_lbStudKurs")
                .text()
        val avg =
            doc.getElementById("ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ctlStudProgress1_lbStudBall")
                .text()

        return User(
            username = usernameProvider.username,
            name = name,
            faculty = faculty,
            info = info,
            avgGrade = avg
                   )
    }

    override suspend fun saveToCache(value: User)
    {
        kotlin.runCatching {
            dao.insert(value)
        }
    }
}

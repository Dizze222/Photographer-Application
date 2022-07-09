package ch.b.retrofitandcoroutines.data.authorization

import ch.b.retrofitandcoroutines.data.authorization.net.authorization.AuthenticationCloudDataSource
import ch.b.retrofitandcoroutines.data.core.ExceptionAuthMapper
import ch.b.retrofitandcoroutines.data.core.authorization.AuthorizationCloud
import ch.b.retrofitandcoroutines.data.core.authorization.AuthorizationData
import ch.b.retrofitandcoroutines.data.core.authorization.AuthorizationListData
import ch.b.retrofitandcoroutines.data.core.authorization.BaseRepositoryAuth
import ch.b.retrofitandcoroutines.data.core.authorization.cache.TokenToSharedPreferences
import ch.b.retrofitandcoroutines.data.core.authorization.mappers.AuthorizationListCloudMapper
import ch.b.retrofitandcoroutines.data.core.authorization.mappers.ToAuthorizationMapper
import ch.b.retrofitandcoroutines.data.registration.net.RegistrationCloudDataSource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AuthenticationRepositoryTest {
    private val unknownHostException = Exception()

    @Test
    fun authentication_success_test() = runBlocking {
        val testAuthCloudDataSource = TestAuthCloudDataSource(true)
        val testRegistrationCloudDataSource = TestRegistrationDataSource(true)
        val testCloudMapper = AuthorizationListCloudMapper.Base(ToAuthorizationMapper())
        val repository = BaseRepositoryAuth(
            testCloudMapper,
            ExceptionAuthMapper.Test(),
            ExceptionAuthMapper.Test(),
            TokenToSharedPreferences.Test(),
            testRegistrationCloudDataSource,
            testAuthCloudDataSource
        )
        val actual = repository.auth(1, "Ivan", "Ivanov", "pas", "auth")
        val expected = AuthorizationListData.Success(listOf(AuthorizationData.Base("accessToken","refreshToken",true)))
        assertEquals(expected, actual)
    }


    private inner class TestAuthCloudDataSource(private val success: Boolean) :
        AuthenticationCloudDataSource {
        override suspend fun authentication(
            phoneNumber: Long,
            password: String
        ): List<AuthorizationCloud.Base> {
            if (success) {
                return listOf(
                    AuthorizationCloud.Base("accessToken", "refreshToken", true)
                )
            } else {
                throw unknownHostException
            }
        }
    }

    private inner class TestRegistrationDataSource(private val success: Boolean) :
        RegistrationCloudDataSource {
        override suspend fun register(
            phoneNumber: Long,
            name: String,
            secondName: String,
            password: String
        ): List<AuthorizationCloud.Base> = if (success) {
            listOf(AuthorizationCloud.Base("accessToken", "refreshToken", true))
        } else {
            listOf(AuthorizationCloud.Base("null", "null", false))
        }
    }
}
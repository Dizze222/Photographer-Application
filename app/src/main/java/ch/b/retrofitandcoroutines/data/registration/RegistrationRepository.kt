package ch.b.retrofitandcoroutines.data.registration

import ch.b.retrofitandcoroutines.data.authorization.net.AuthenticationCloudDataSource
import ch.b.retrofitandcoroutines.data.core.ExceptionAuthMapper
import ch.b.retrofitandcoroutines.data.core.authorization.BaseRepositoryAuth
import ch.b.retrofitandcoroutines.data.core.authorization.cache.TokenToSharedPreferences
import ch.b.retrofitandcoroutines.data.core.authorization.mappers.AuthorizationListCloudMapper
import ch.b.retrofitandcoroutines.data.registration.net.RegistrationCloudDataSource
import javax.inject.Inject


class RegistrationRepository @Inject constructor(
    registrationCloudDataSource: RegistrationCloudDataSource,
    cloudMapper: AuthorizationListCloudMapper,
    exceptionMapper: ExceptionAuthMapper,
    tokenToSharedPreferences: TokenToSharedPreferences,
    authenticationCloudDataSource: AuthenticationCloudDataSource
) :
    BaseRepositoryAuth(
        cloudMapper,
        exceptionMapper,
        tokenToSharedPreferences,
        registrationCloudDataSource,
        authenticationCloudDataSource
    )

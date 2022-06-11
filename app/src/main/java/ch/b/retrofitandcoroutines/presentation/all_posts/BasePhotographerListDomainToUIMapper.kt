package ch.b.retrofitandcoroutines.presentation.all_posts

import ch.b.retrofitandcoroutines.domain.all_posts.PhotographerDomain
import ch.b.retrofitandcoroutines.domain.all_posts.PhotographerDomainToUIMapper
import ch.b.retrofitandcoroutines.domain.all_posts.PhotographerListDomainToUIMapper
import ch.b.retrofitandcoroutines.presentation.core.ResourceProvider

class BasePhotographerListDomainToUIMapper(
    private val photographerMapper: PhotographerDomainToUIMapper<PhotographerUI>,
    private val resourceProvider: ResourceProvider
) : PhotographerListDomainToUIMapper() {
    override fun map(data: List<PhotographerDomain>): PhotographerListUI =
        PhotographerListUI.Success(data, photographerMapper)


    override fun map(error: String): PhotographerListUI =
        PhotographerListUI.Fail(error)

    override fun map(): PhotographerListUI = PhotographerListUI.EmptyData

}
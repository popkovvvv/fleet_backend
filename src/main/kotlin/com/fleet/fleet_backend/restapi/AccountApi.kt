package com.fleet.fleet_backend.restapi

import com.fleet.fleet_backend.core.account.Account
import com.fleet.fleet_backend.core.account.ContactInfo
import com.fleet.fleet_backend.core.usecases.SaveAccountData
import com.fleet.fleet_backend.core.usecases.SaveAccountUseCase
import org.springframework.stereotype.Component

object AccountApiStatuses {
    const val ACCOUNT_CREATE_FAILED = "ACCOUNT_CREATE_FAILED"
    const val ACCESS_DENIED = "ACCESS_DENIED"
}

data class AccountVO(
    val accountId: String? = null,
    val username: String,
    val contactInfo: ContactInfo
) {
    fun toSaveAccountData(): SaveAccountData = SaveAccountData(
        username, contactInfo
    )

    companion object {
        const val type = "accounts"

        fun fromAccount(account: Account): AccountVO = AccountVO(
            accountId = account.id,
            username = account.username,
            contactInfo = account.contactInfo
        )
    }
}

@Component
class SaveAccountRequest :
    JSONApiRequest<SaveAccountUseCase.Request, AccountVO>() {
    override val usecase = SaveAccountUseCase()

    override fun toUseCaseRequest(data: JSONApiData<AccountVO>): SaveAccountUseCase.Request {
        val accountSettings = data.attributes.toSaveAccountData()
        return SaveAccountUseCase.Request(accountSettings)
    }

    override suspend fun execute(data: JSONApiData<AccountVO>): JSONApiResponse {
        return when (val resp = usecase.execute(toUseCaseRequest(data))) {
            is SaveAccountUseCase.Response.Success ->
                JSONApiResponse.Success(
                    listOf(
                        JSONApiData(
                            AccountVO.type,
                            resp.account.id,
                            AccountVO.fromAccount(resp.account)
                        )
                    )
                )
            is SaveAccountUseCase.Response.Error ->
                withRequestError(AccountApiStatuses.ACCOUNT_CREATE_FAILED)
            is SaveAccountUseCase.Response.AccessControlError ->
                withRequestError(AccountApiStatuses.ACCESS_DENIED)
        }
    }
}
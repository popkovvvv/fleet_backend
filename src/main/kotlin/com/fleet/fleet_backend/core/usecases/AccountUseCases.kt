package com.fleet.fleet_backend.core.usecases

import com.fleet.fleet_backend.core.account.Account
import com.fleet.fleet_backend.core.account.AccountRepository
import com.fleet.fleet_backend.core.account.ContactInfo
import com.fleet.fleet_backend.core.utils.IdentityGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

data class SaveAccountData(
    val username: String,
    val contactInfo: ContactInfo
) {
    fun toNewAccount(): Account = Account(
        IdentityGenerator.generateSimple(), username, contactInfo
    )
}

@Component
class SaveAccountUseCase : AccessControlledUseCase<SaveAccountUseCase.Request, SaveAccountUseCase.Response>() {
    override val accessControlPolicy: AccessControlPolicy = AnyoneCanCallPolicy()
    override val accessControlError: Response = Response.AccessControlError

    @Autowired
    private lateinit var accountRepository: AccountRepository

    data class Request(val data: SaveAccountData) : UseCaseRequest
    sealed class Response : UseCaseResponse {
        data class Success(val account: Account) : Response()
        object Error : Response()
        object AccessControlError : Response()
    }

    override suspend fun executeControlled(req: Request): Response {
        val newAccount = req.data.toNewAccount()
        logger().info { "Trying to save new user with id: ${newAccount.id}" }
        return try {
            accountRepository.save(newAccount)
            Response.Success(newAccount)
        } catch (e: Exception) {
            logger().error(e) { e.message }
            Response.Error
        }
    }
}
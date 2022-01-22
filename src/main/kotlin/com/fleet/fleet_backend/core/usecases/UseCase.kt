package com.fleet.fleet_backend.core.usecases

import mu.KotlinLogging

interface UseCase<in Request : UseCaseRequest, out Response : UseCaseResponse> {
    suspend fun execute(req: Request): Response
    fun logger() = KotlinLogging.logger {}
}

interface UseCaseResponse

interface UseCaseRequest

interface AccessControlPolicy {

    fun isValid(req: UseCaseRequest): Boolean
}

class AnyoneCanCallPolicy : AccessControlPolicy {
    override fun isValid(req: UseCaseRequest): Boolean {
        return true
    }
}

abstract class AccessControlledUseCase<in Request : UseCaseRequest, out Response : UseCaseResponse> :
    UseCase<Request, Response> {

    private val logger = KotlinLogging.logger {}

    abstract val accessControlPolicy: AccessControlPolicy

    abstract val accessControlError: Response

    protected abstract suspend fun executeControlled(req: Request): Response

    final override suspend fun execute(req: Request): Response {
        return if (accessControlPolicy.isValid(req)) {
            executeControlled(req)
        } else {
            logger.error {
                "UseCase execution access denied for user for usecase ${this::class.simpleName}"
            }
            accessControlError
        }
    }
}

class RequiresAdminUserPolicy : AccessControlPolicy {
    override fun isValid(req: UseCaseRequest): Boolean {
        return true
    }
}
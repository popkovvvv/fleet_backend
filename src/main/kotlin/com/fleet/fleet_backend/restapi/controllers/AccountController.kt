package com.fleet.fleet_backend.restapi.controllers

import com.fleet.fleet_backend.restapi.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("/accounts")
class AccountController(
    private val saveAccountRequest: SaveAccountRequest
) {

    @PostMapping
    suspend fun saveAccount(@RequestBody data: JSONApiData<AccountVO>): ResponseEntity<JSONApiResponse> {
        return ResponseEntity.ok(saveAccountRequest.execute(data))
    }
}
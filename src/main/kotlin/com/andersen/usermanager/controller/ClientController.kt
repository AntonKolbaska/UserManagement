package com.andersen.usermanager.controller

import com.andersen.usermanager.dto.request.ClientRequestDTO
import com.andersen.usermanager.dto.response.ClientResponseDTO
import com.andersen.usermanager.service.ClientServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/client")
class ClientController(var clientServiceImpl: ClientServiceImpl) {
    @PostMapping("/create")
    fun createClient(@RequestBody newClient: ClientRequestDTO): ClientResponseDTO {
        return clientServiceImpl.createClient(newClient)
    }

    @PutMapping("/update/{id}")
    fun updateClient(@PathVariable id: Long, @RequestBody newClient: ClientRequestDTO): ClientResponseDTO {
        return clientServiceImpl.updateClient(id, newClient)
    }

    @GetMapping("/{id}")
    fun getClient(@PathVariable id: Long): ClientResponseDTO {
        return clientServiceImpl.getClient(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")
    }

    @GetMapping("/list")
    fun getClientList(): List<ClientResponseDTO> {
        return clientServiceImpl.getAllClients()
    }

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Long) {
        return clientServiceImpl.deleteClient(id)
    }
}
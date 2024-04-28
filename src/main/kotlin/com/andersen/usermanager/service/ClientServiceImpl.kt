package com.andersen.usermanager.service

import com.andersen.usermanager.dto.request.ClientRequestDTO
import com.andersen.usermanager.dto.response.ClientResponseDTO
import com.andersen.usermanager.entity.Client
import com.andersen.usermanager.entity.Gender
import com.andersen.usermanager.exception.ClientNotFoundException
import com.andersen.usermanager.exception.GenderUndefinedException
import com.andersen.usermanager.exception.NoClientsExistException
import com.andersen.usermanager.exception.message.ExceptionMessage
import com.andersen.usermanager.repository.ClientRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull
import java.net.URL
import org.json.JSONObject
import org.springframework.transaction.annotation.Transactional

@Service
class ClientServiceImpl(var clientRepository: ClientRepository) {
    fun createClient(newClientDTO: ClientRequestDTO): ClientResponseDTO {
        val save = clientRepository.save(
            Client(
                id = null,
                firstName = newClientDTO.fistName,
                lastName = newClientDTO.lastName,
                email = newClientDTO.email,
                gender = Gender.valueOf(defineClientGender(newClientDTO.fistName)),
                job = newClientDTO.job,
                position = newClientDTO.position
            )
        )
        return ClientResponseDTO(
            id = save.id!!,
            firstName = save.firstName,
            lastName = save.lastName,
            email = save.email,
            gender = save.gender,
            job = save.job,
            position = save.position
        )
    }

    @Transactional
    fun updateClient(clientId: Long, clientDTO: ClientRequestDTO): ClientResponseDTO {
        val existingClient = clientRepository.findById(clientId)
            .orElseThrow()

        existingClient.apply {
            firstName = clientDTO.fistName
            lastName = clientDTO.lastName
            email = clientDTO.email
            job = clientDTO.job
            position = clientDTO.position
        }

        val updatedClient = clientRepository.save(existingClient)

        return ClientResponseDTO(
            id = updatedClient.id!!,
            firstName = updatedClient.firstName,
            lastName = updatedClient.lastName,
            email = updatedClient.email,
            gender = updatedClient.gender,
            job = updatedClient.job,
            position = updatedClient.position
        )
    }

    fun getClient(id: Long): ClientResponseDTO? {
        return clientRepository.findById(id)
            .map {
                ClientResponseDTO(
                    id = it.id!!,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    email = it.email,
                    gender = it.gender,
                    job = it.job,
                    position = it.position
                )
            }.orElseThrow {
                ClientNotFoundException(
                    ExceptionMessage.CLIENT_NOT_FOUND
                        .toString()
                        .format(id)
                )
            }
    }

    fun getAllClients(): List<ClientResponseDTO> {
        val clients = clientRepository.findAll()
        if (clients.isEmpty())
            throw NoClientsExistException(
                ExceptionMessage.NO_CLIENTS_EXIST
                    .toString()
            )
        return clients.map {
            ClientResponseDTO(
                id = it.id!!,
                firstName = it.firstName,
                lastName = it.lastName,
                email = it.email,
                gender = it.gender,
                job = it.job,
                position = it.position
            )
        }
    }

    fun deleteClient(id: Long) {
        clientRepository.deleteById(id)
    }

    private fun defineClientGender(firstName: String): String {
        val apiUrl = "https://api.genderize.io/?name=$firstName"
        val response = URL(apiUrl).readText()
        val json = JSONObject(response)
        val probability = json.getDouble("probability")

        if (probability >= 0.8) {
            return json.getString("gender").uppercase()
        } else {
            throw GenderUndefinedException(
                ExceptionMessage.GENDER_NOT_DEFINED
                    .toString()
                    .format(firstName)
            )
        }
    }
}
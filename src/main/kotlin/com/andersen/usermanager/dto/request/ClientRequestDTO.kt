package com.andersen.usermanager.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

class ClientRequestDTO(
    @JsonProperty("firstname")
    var fistName: String,
    @JsonProperty("lastname")
    var lastName: String,
    @JsonProperty("email")
    var email: String,
    @JsonProperty("job")
    var job: String?,
    @JsonProperty("position")
    var position: String?
)
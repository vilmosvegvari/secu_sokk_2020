package hu.bme.webshop.caff.dto

import javax.validation.constraints.NotBlank

class CommentRequest(
        var message: @NotBlank String
//        var userId: @NotBlank Long
)
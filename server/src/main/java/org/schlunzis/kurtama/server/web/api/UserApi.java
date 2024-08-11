package org.schlunzis.kurtama.server.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.schlunzis.kurtama.server.web.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/user/v1")
@Tag(name = "User API", description = "User API for managing the application")
public interface UserApi {

    default IUserService getService() {
        return new IUserService() {
        };
    }

    @GetMapping("summary/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Summary about user", description = "Provides a summary about the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully provided the summary"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to send new data", content = @Content)
    })
    default ResponseEntity<Void> summary(@Parameter(description = "The id of the user to be examined", required = true) @PathVariable("id") UUID id) {
        return getService().summary(id);
    }
}

package org.schlunzis.kurtama.server.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.schlunzis.kurtama.server.web.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/user/v1")
@Tag(name = "User API", description = "User API for managing the application")
public interface UserApi {

    default IUserService getService() {
        return new IUserService() {
        };
    }

    @GetMapping("summary")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Summary about user", description = "Provides a summary about the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully provided the summary"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to send new data", content = @Content)
    })
    default ResponseEntity<Void> summary() {
        return getService().summary();
    }
}

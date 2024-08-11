package org.schlunzis.kurtama.server.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.schlunzis.kurtama.server.web.service.IAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


@RequestMapping("/api/admin/v1")
@Tag(name = "Admin API", description = "Admin API for managing the application")
public interface AdminApi {

    default IAdminService getService() {
        return new IAdminService() {
        };
    }

    @DeleteMapping("user/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete user by id", description = "Deletes a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the user"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to send new data", content = @Content),
            @ApiResponse(responseCode = "404", description = "The user was not found", content = @Content)
    })
    default ResponseEntity<Void> newData(@Parameter(description = "The list of data entries to add", required = true) @PathVariable("id") UUID id) {
        return getService().deleteUser(id);
    }

}

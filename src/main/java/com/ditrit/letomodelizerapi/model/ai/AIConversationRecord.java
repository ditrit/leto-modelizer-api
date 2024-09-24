package com.ditrit.letomodelizerapi.model.ai;

import com.ditrit.letomodelizerapi.model.file.FileRecord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * A record representing the data required to create a new conversation with AI.
 *
 * @param project the name of the project associated with the conversation. Cannot be blank.
 * @param diagram the path to the diagram related to the conversation. Cannot be blank.
 * @param plugin the name of the plugin involved in the conversation. Cannot be blank.
 * @param checksum the checksum of the diagram files for ensuring data integrity. May be null.
 * @param files a list of all diagram files associated with the conversation. Cannot be null.
 */
public record AIConversationRecord(
        @NotBlank String project,
        @NotNull String diagram,
        @NotBlank String plugin,
        String checksum,
        @NotNull List<FileRecord> files
) {
}

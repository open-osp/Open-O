/**
 * Copyright (c) 2026. Magenta Health. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package ca.openosp.openo.tickler.dto;

import ca.openosp.openo.commn.model.Tickler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TicklerListDTO helper methods.
 *
 * @since 2026-01-30
 */
@Tag("unit")
@Tag("fast")
@Tag("read")
@Tag("tickler")
@Tag("dto")
@DisplayName("TicklerListDTO")
class TicklerListDTOTest {

    private TicklerListDTO dto;

    @BeforeEach
    void setUp() {
        dto = new TicklerListDTO();
    }

    @Nested
    @DisplayName("getDemographicFormattedName")
    class DemographicFormattedName {

        @Test
        @DisplayName("should format name when both names present")
        void shouldFormatName_whenBothNamesPresent() {
            dto.setDemographicLastName("Smith");
            dto.setDemographicFirstName("John");

            assertThat(dto.getDemographicFormattedName()).isEqualTo("Smith, John");
        }

        @Test
        @DisplayName("should return just last name when only last name present")
        void shouldReturnJustLastName_whenOnlyLastNamePresent() {
            dto.setDemographicLastName("Smith");
            dto.setDemographicFirstName(null);

            assertThat(dto.getDemographicFormattedName()).isEqualTo("Smith");
        }

        @Test
        @DisplayName("should return just first name when only first name present")
        void shouldReturnJustFirstName_whenOnlyFirstNamePresent() {
            dto.setDemographicLastName(null);
            dto.setDemographicFirstName("John");

            assertThat(dto.getDemographicFormattedName()).isEqualTo("John");
        }

        @Test
        @DisplayName("should return empty string when both names null")
        void shouldReturnEmptyString_whenBothNamesNull() {
            dto.setDemographicLastName(null);
            dto.setDemographicFirstName(null);

            assertThat(dto.getDemographicFormattedName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("getCreatorFormattedName")
    class CreatorFormattedName {

        @Test
        @DisplayName("should format name when both names present")
        void shouldFormatName_whenBothNamesPresent() {
            dto.setCreatorLastName("Doctor");
            dto.setCreatorFirstName("Jane");

            assertThat(dto.getCreatorFormattedName()).isEqualTo("Doctor, Jane");
        }

        @Test
        @DisplayName("should return N/A when both names null")
        void shouldReturnNA_whenBothNamesNull() {
            dto.setCreatorLastName(null);
            dto.setCreatorFirstName(null);

            assertThat(dto.getCreatorFormattedName()).isEqualTo("N/A");
        }

        @Test
        @DisplayName("should return just last name when only last name present")
        void shouldReturnJustLastName_whenOnlyLastNamePresent() {
            dto.setCreatorLastName("Doctor");
            dto.setCreatorFirstName(null);

            assertThat(dto.getCreatorFormattedName()).isEqualTo("Doctor");
        }
    }

    @Nested
    @DisplayName("getAssigneeFormattedName")
    class AssigneeFormattedName {

        @Test
        @DisplayName("should format name when both names present")
        void shouldFormatName_whenBothNamesPresent() {
            dto.setAssigneeLastName("Nurse");
            dto.setAssigneeFirstName("Bob");

            assertThat(dto.getAssigneeFormattedName()).isEqualTo("Nurse, Bob");
        }

        @Test
        @DisplayName("should return N/A when both names null")
        void shouldReturnNA_whenBothNamesNull() {
            dto.setAssigneeLastName(null);
            dto.setAssigneeFirstName(null);

            assertThat(dto.getAssigneeFormattedName()).isEqualTo("N/A");
        }

        @Test
        @DisplayName("should return just first name when only first name present")
        void shouldReturnJustFirstName_whenOnlyFirstNamePresent() {
            dto.setAssigneeLastName(null);
            dto.setAssigneeFirstName("Bob");

            assertThat(dto.getAssigneeFormattedName()).isEqualTo("Bob");
        }
    }

    @Nested
    @DisplayName("getStatusDesc")
    class StatusDesc {

        @Test
        @DisplayName("should return empty string when status is null")
        void shouldReturnEmptyString_whenStatusIsNull() {
            dto.setStatus(null);

            assertThat(dto.getStatusDesc(Locale.ENGLISH)).isEmpty();
        }

        @Test
        @DisplayName("should return non-empty string for active status")
        void shouldReturnNonEmptyString_forActiveStatus() {
            dto.setStatus(Tickler.STATUS.A);

            // Returns localized message, or the key itself as fallback
            String statusDesc = dto.getStatusDesc(Locale.ENGLISH);
            assertThat(statusDesc).isNotNull();
            assertThat(statusDesc).isNotEmpty();
        }

        @Test
        @DisplayName("should return non-empty string for completed status")
        void shouldReturnNonEmptyString_forCompletedStatus() {
            dto.setStatus(Tickler.STATUS.C);

            String statusDesc = dto.getStatusDesc(Locale.ENGLISH);
            assertThat(statusDesc).isNotNull();
            assertThat(statusDesc).isNotEmpty();
        }

        @Test
        @DisplayName("should return non-empty string for deleted status")
        void shouldReturnNonEmptyString_forDeletedStatus() {
            dto.setStatus(Tickler.STATUS.D);

            String statusDesc = dto.getStatusDesc(Locale.ENGLISH);
            assertThat(statusDesc).isNotNull();
            assertThat(statusDesc).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("getMessage")
    class GetMessage {

        @Test
        @DisplayName("should return message when set")
        void shouldReturnMessage_whenSet() {
            dto.setMessage("Test message");

            assertThat(dto.getMessage()).isEqualTo("Test message");
        }

        @Test
        @DisplayName("should return empty string when message is null")
        void shouldReturnEmptyString_whenMessageIsNull() {
            dto.setMessage(null);

            assertThat(dto.getMessage()).isEmpty();
        }
    }

    @Nested
    @DisplayName("comments")
    class Comments {

        @Test
        @DisplayName("should initialize with empty list")
        void shouldInitializeWithEmptyList() {
            assertThat(dto.getComments()).isNotNull();
            assertThat(dto.getComments()).isEmpty();
        }

        @Test
        @DisplayName("should set comments list")
        void shouldSetCommentsList() {
            ArrayList<TicklerCommentDTO> comments = new ArrayList<>();
            comments.add(new TicklerCommentDTO());
            dto.setComments(comments);

            assertThat(dto.getComments()).hasSize(1);
        }

        @Test
        @DisplayName("should handle null by setting empty list")
        void shouldHandleNull_bySettingEmptyList() {
            dto.setComments(null);

            assertThat(dto.getComments()).isNotNull();
            assertThat(dto.getComments()).isEmpty();
        }
    }

    @Nested
    @DisplayName("links")
    class Links {

        @Test
        @DisplayName("should initialize with empty list")
        void shouldInitializeWithEmptyList() {
            assertThat(dto.getLinks()).isNotNull();
            assertThat(dto.getLinks()).isEmpty();
        }

        @Test
        @DisplayName("should set links list")
        void shouldSetLinksList() {
            ArrayList<TicklerLinkDTO> links = new ArrayList<>();
            links.add(new TicklerLinkDTO());
            dto.setLinks(links);

            assertThat(dto.getLinks()).hasSize(1);
        }

        @Test
        @DisplayName("should handle null by setting empty list")
        void shouldHandleNull_bySettingEmptyList() {
            dto.setLinks(null);

            assertThat(dto.getLinks()).isNotNull();
            assertThat(dto.getLinks()).isEmpty();
        }

        @Test
        @DisplayName("should defensively copy links list")
        void shouldDefensivelyCopy_linksList() {
            ArrayList<TicklerLinkDTO> links = new ArrayList<>();
            TicklerLinkDTO link = new TicklerLinkDTO();
            links.add(link);
            dto.setLinks(links);

            // Mutate original list after setting
            links.add(new TicklerLinkDTO());

            // DTO's list should remain unchanged
            assertThat(dto.getLinks()).hasSize(1);
        }

        @Test
        @DisplayName("should defensively copy comments list")
        void shouldDefensivelyCopy_commentsList() {
            ArrayList<TicklerCommentDTO> comments = new ArrayList<>();
            TicklerCommentDTO comment = new TicklerCommentDTO();
            comments.add(comment);
            dto.setComments(comments);

            // Mutate original list after setting
            comments.add(new TicklerCommentDTO());

            // DTO's list should remain unchanged
            assertThat(dto.getComments()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("constructor with all arguments")
    class FullConstructor {

        @Test
        @DisplayName("should initialize all fields correctly")
        void shouldInitializeAllFields_correctly() {
            Date serviceDate = new Date();
            Date createDate = new Date();

            TicklerListDTO fullDto = new TicklerListDTO(
                1, "Test message", serviceDate, createDate,
                Tickler.STATUS.A, Tickler.PRIORITY.High,
                100, "PatientLast", "PatientFirst",
                "CreatorLast", "CreatorFirst",
                "AssigneeLast", "AssigneeFirst"
            );

            assertThat(fullDto.getId()).isEqualTo(1);
            assertThat(fullDto.getMessage()).isEqualTo("Test message");
            assertThat(fullDto.getServiceDate()).isEqualTo(serviceDate);
            assertThat(fullDto.getCreateDate()).isEqualTo(createDate);
            assertThat(fullDto.getStatus()).isEqualTo(Tickler.STATUS.A);
            assertThat(fullDto.getPriority()).isEqualTo(Tickler.PRIORITY.High);
            assertThat(fullDto.getDemographicNo()).isEqualTo(100);
            assertThat(fullDto.getDemographicLastName()).isEqualTo("PatientLast");
            assertThat(fullDto.getDemographicFirstName()).isEqualTo("PatientFirst");
            assertThat(fullDto.getCreatorLastName()).isEqualTo("CreatorLast");
            assertThat(fullDto.getCreatorFirstName()).isEqualTo("CreatorFirst");
            assertThat(fullDto.getAssigneeLastName()).isEqualTo("AssigneeLast");
            assertThat(fullDto.getAssigneeFirstName()).isEqualTo("AssigneeFirst");
            assertThat(fullDto.getComments()).isNotNull().isEmpty();
        }
    }
}

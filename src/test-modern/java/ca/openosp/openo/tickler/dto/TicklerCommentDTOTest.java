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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TicklerCommentDTO helper methods.
 *
 * @since 2026-01-30
 */
@Tag("unit")
@Tag("fast")
@Tag("read")
@Tag("tickler")
@Tag("dto")
@DisplayName("TicklerCommentDTO")
class TicklerCommentDTOTest {

    private TicklerCommentDTO dto;

    @BeforeEach
    void setUp() {
        dto = new TicklerCommentDTO();
    }

    @Nested
    @DisplayName("getProviderFormattedName")
    class ProviderFormattedName {

        @Test
        @DisplayName("should format name when both names present")
        void shouldFormatName_whenBothNamesPresent() {
            dto.setProviderLastName("Smith");
            dto.setProviderFirstName("John");

            assertThat(dto.getProviderFormattedName()).isEqualTo("Smith, John");
        }

        @Test
        @DisplayName("should return just last name when only last name present")
        void shouldReturnJustLastName_whenOnlyLastNamePresent() {
            dto.setProviderLastName("Smith");
            dto.setProviderFirstName(null);

            assertThat(dto.getProviderFormattedName()).isEqualTo("Smith");
        }

        @Test
        @DisplayName("should return just first name when only first name present")
        void shouldReturnJustFirstName_whenOnlyFirstNamePresent() {
            dto.setProviderLastName(null);
            dto.setProviderFirstName("John");

            assertThat(dto.getProviderFormattedName()).isEqualTo("John");
        }

        @Test
        @DisplayName("should return empty string when both names null")
        void shouldReturnEmptyString_whenBothNamesNull() {
            dto.setProviderLastName(null);
            dto.setProviderFirstName(null);

            assertThat(dto.getProviderFormattedName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("isUpdateDateToday")
    class IsUpdateDateToday {

        @Test
        @DisplayName("should return true when update date is today")
        void shouldReturnTrue_whenUpdateDateIsToday() {
            dto.setUpdateDate(new Date());

            assertThat(dto.isUpdateDateToday()).isTrue();
        }

        @Test
        @DisplayName("should return false when update date is yesterday")
        void shouldReturnFalse_whenUpdateDateIsYesterday() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            dto.setUpdateDate(cal.getTime());

            assertThat(dto.isUpdateDateToday()).isFalse();
        }

        @Test
        @DisplayName("should return false when update date is tomorrow")
        void shouldReturnFalse_whenUpdateDateIsTomorrow() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            dto.setUpdateDate(cal.getTime());

            assertThat(dto.isUpdateDateToday()).isFalse();
        }

        @Test
        @DisplayName("should return false when update date is null")
        void shouldReturnFalse_whenUpdateDateIsNull() {
            dto.setUpdateDate(null);

            assertThat(dto.isUpdateDateToday()).isFalse();
        }

        @Test
        @DisplayName("should return true when update date is earlier today")
        void shouldReturnTrue_whenUpdateDateIsEarlierToday() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 1);
            dto.setUpdateDate(cal.getTime());

            assertThat(dto.isUpdateDateToday()).isTrue();
        }
    }

    @Nested
    @DisplayName("date formatting methods")
    class DateFormatting {

        @Test
        @DisplayName("should format time correctly")
        void shouldFormatTime_correctly() {
            dto.setUpdateDate(new Date());

            String time = dto.getUpdateTime(Locale.ENGLISH);
            assertThat(time).isNotNull();
            assertThat(time).isNotEmpty();
        }

        @Test
        @DisplayName("should format date time correctly")
        void shouldFormatDateTime_correctly() {
            dto.setUpdateDate(new Date());

            String dateTime = dto.getUpdateDateTime(Locale.ENGLISH);
            assertThat(dateTime).isNotNull();
            assertThat(dateTime).isNotEmpty();
        }

        @Test
        @DisplayName("should format date correctly")
        void shouldFormatDate_correctly() {
            dto.setUpdateDate(new Date());

            String date = dto.getUpdateDate(Locale.ENGLISH);
            assertThat(date).isNotNull();
            assertThat(date).isNotEmpty();
        }

        @Test
        @DisplayName("should return empty string for time when updateDate is null")
        void shouldReturnEmptyString_forTime_whenUpdateDateIsNull() {
            dto.setUpdateDate(null);

            String time = dto.getUpdateTime(Locale.ENGLISH);
            assertThat(time).isEmpty();
        }

        @Test
        @DisplayName("should return space for date time when updateDate is null")
        void shouldReturnSpace_forDateTime_whenUpdateDateIsNull() {
            dto.setUpdateDate(null);

            // formatDateTime concatenates formatDate + " " + formatTime
            // When date is null, both return "", so result is " "
            String dateTime = dto.getUpdateDateTime(Locale.ENGLISH);
            assertThat(dateTime).isEqualTo(" ");
        }

        @Test
        @DisplayName("should return empty string for date when updateDate is null")
        void shouldReturnEmptyString_forDate_whenUpdateDateIsNull() {
            dto.setUpdateDate(null);

            String date = dto.getUpdateDate(Locale.ENGLISH);
            assertThat(date).isEmpty();
        }
    }

    @Nested
    @DisplayName("constructor with all arguments")
    class FullConstructor {

        @Test
        @DisplayName("should initialize all fields correctly")
        void shouldInitializeAllFields_correctly() {
            Date updateDate = new Date();

            TicklerCommentDTO fullDto = new TicklerCommentDTO(
                1, 100, "Test comment", updateDate,
                "ProviderLast", "ProviderFirst"
            );

            assertThat(fullDto.getId()).isEqualTo(1);
            assertThat(fullDto.getTicklerNo()).isEqualTo(100);
            assertThat(fullDto.getMessage()).isEqualTo("Test comment");
            assertThat(fullDto.getUpdateDate()).isEqualTo(updateDate);
            assertThat(fullDto.getProviderLastName()).isEqualTo("ProviderLast");
            assertThat(fullDto.getProviderFirstName()).isEqualTo("ProviderFirst");
        }
    }

    @Nested
    @DisplayName("getters and setters")
    class GettersAndSetters {

        @Test
        @DisplayName("should set and get id")
        void shouldSetAndGetId() {
            dto.setId(42);
            assertThat(dto.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("should set and get ticklerNo")
        void shouldSetAndGetTicklerNo() {
            dto.setTicklerNo(123);
            assertThat(dto.getTicklerNo()).isEqualTo(123);
        }

        @Test
        @DisplayName("should set and get message")
        void shouldSetAndGetMessage() {
            dto.setMessage("A comment message");
            assertThat(dto.getMessage()).isEqualTo("A comment message");
        }

        @Test
        @DisplayName("should set and get updateDate")
        void shouldSetAndGetUpdateDate() {
            Date date = new Date();
            dto.setUpdateDate(date);
            assertThat(dto.getUpdateDate()).isEqualTo(date);
        }
    }
}

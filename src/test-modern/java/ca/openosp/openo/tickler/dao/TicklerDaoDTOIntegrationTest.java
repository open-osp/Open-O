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
 * <p>
 * This software was written for
 * Magenta Health
 * Toronto, Ontario, Canada
 */
package ca.openosp.openo.tickler.dao;

import ca.openosp.openo.commn.dao.TicklerCommentDao;
import ca.openosp.openo.commn.model.CustomFilter;
import ca.openosp.openo.commn.model.Tickler;
import ca.openosp.openo.commn.model.TicklerComment;
import ca.openosp.openo.tickler.dto.TicklerCommentDTO;
import ca.openosp.openo.tickler.dto.TicklerListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for TicklerDao DTO projection operations.
 *
 * <p>This test class validates the new DTO projection methods that provide
 * optimized data retrieval for list views, reducing database queries from
 * ~25 per page load to exactly 2 queries.</p>
 *
 * @since 2026-01-30
 * @see ca.openosp.openo.commn.dao.TicklerDao
 * @see TicklerListDTO
 * @see TicklerCommentDTO
 */
@DisplayName("Tickler DAO DTO Projection Integration Tests")
@Tag("integration")
@Tag("database")
@Tag("dao")
@Tag("slow")
@Tag("read")
@Tag("dto")
public class TicklerDaoDTOIntegrationTest extends TicklerDaoBaseIntegrationTest {

    @Autowired
    private TicklerCommentDao ticklerCommentDao;

    private Integer ticklerWithCommentsId;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        // Create a tickler with comments for testing batch comment loading
        Tickler ticklerWithComments = new Tickler();
        ticklerWithComments.setDemographicNo(2001);
        ticklerWithComments.setMessage("Tickler with comments");
        ticklerWithComments.setStatus(Tickler.STATUS.A);
        ticklerWithComments.setPriority(Tickler.PRIORITY.Normal);
        ticklerWithComments.setCreator("999998");
        ticklerWithComments.setTaskAssignedTo("999999");
        ticklerWithComments.setServiceDate(new Date());
        ticklerDao.persist(ticklerWithComments);
        entityManager.flush();

        ticklerWithCommentsId = ticklerWithComments.getId();

        // Add some comments to the tickler
        for (int i = 0; i < 3; i++) {
            TicklerComment comment = new TicklerComment();
            comment.setTicklerNo(ticklerWithCommentsId);
            comment.setProviderNo("999998");
            comment.setMessage("Test comment " + (i + 1));
            ticklerCommentDao.persist(comment);
        }
        entityManager.flush();
    }

    @Nested
    @DisplayName("getTicklerDTOs with filter")
    class GetTicklerDTOsWithFilter {

        @Test
        @Tag("query")
        @DisplayName("should return DTOs when filter matches active ticklers")
        void shouldReturnDTOs_whenFilterMatchesActiveTicklers() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then
            assertThat(results).isNotNull();
            assertThat(results).isNotEmpty();
            assertThat(results).allMatch(dto -> dto.getStatus() == Tickler.STATUS.A);
        }

        @Test
        @Tag("query")
        @DisplayName("should return empty list when filter matches no ticklers")
        void shouldReturnEmptyList_whenFilterMatchesNoTicklers() {
            // Given - filter for a specific demographic that doesn't exist
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");
            filter.setDemographicNo("999999999"); // Non-existent demographic

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then
            assertThat(results).isNotNull();
            assertThat(results).isEmpty();
        }

        @Test
        @Tag("query")
        @DisplayName("should populate all DTO fields correctly")
        void shouldPopulateAllDTOFields_correctly() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then
            assertThat(results).isNotEmpty();
            TicklerListDTO dto = results.get(0);

            assertThat(dto.getId()).isNotNull();
            assertThat(dto.getMessage()).isNotNull();
            assertThat(dto.getServiceDate()).isNotNull();
            assertThat(dto.getCreateDate()).isNotNull();
            assertThat(dto.getStatus()).isNotNull();
            assertThat(dto.getPriority()).isNotNull();
            assertThat(dto.getDemographicNo()).isNotNull();
        }
    }

    @Nested
    @DisplayName("getTicklerDTOs with pagination")
    class GetTicklerDTOsWithPagination {

        @Test
        @Tag("query")
        @DisplayName("should apply pagination correctly")
        void shouldApplyPagination_correctly() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> page1 = ticklerDao.getTicklerDTOs(filter, 0, 2);
            List<TicklerListDTO> page2 = ticklerDao.getTicklerDTOs(filter, 2, 2);

            // Then
            assertThat(page1).hasSizeLessThanOrEqualTo(2);
            assertThat(page2).hasSizeLessThanOrEqualTo(2);

            if (!page1.isEmpty() && !page2.isEmpty()) {
                assertThat(page1.get(0).getId()).isNotEqualTo(page2.get(0).getId());
            }
        }

        @Test
        @Tag("query")
        @DisplayName("should return first page when offset is zero")
        void shouldReturnFirstPage_whenOffsetIsZero() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> firstPage = ticklerDao.getTicklerDTOs(filter, 0, 100);
            List<TicklerListDTO> allResults = ticklerDao.getTicklerDTOs(filter);

            // Then
            assertThat(firstPage).hasSameSizeAs(allResults);
        }
    }

    @Nested
    @DisplayName("batch comment loading")
    class BatchCommentLoading {

        @Test
        @Tag("query")
        @DisplayName("should batch load comments for ticklers")
        void shouldBatchLoadComments_forTicklers() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then
            // Find the tickler with comments
            TicklerListDTO ticklerWithCommentsDTO = results.stream()
                .filter(dto -> dto.getId().equals(ticklerWithCommentsId))
                .findFirst()
                .orElse(null);

            assertThat(ticklerWithCommentsDTO).isNotNull();
            assertThat(ticklerWithCommentsDTO.getComments()).isNotNull();
            assertThat(ticklerWithCommentsDTO.getComments()).hasSize(3);
        }

        @Test
        @Tag("query")
        @DisplayName("should populate comment DTO fields correctly")
        void shouldPopulateCommentDTOFields_correctly() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then
            TicklerListDTO ticklerWithCommentsDTO = results.stream()
                .filter(dto -> dto.getId().equals(ticklerWithCommentsId))
                .findFirst()
                .orElse(null);

            assertThat(ticklerWithCommentsDTO).isNotNull();
            assertThat(ticklerWithCommentsDTO.getComments()).isNotEmpty();

            TicklerCommentDTO comment = ticklerWithCommentsDTO.getComments().get(0);
            assertThat(comment.getId()).isNotNull();
            assertThat(comment.getTicklerNo()).isEqualTo(ticklerWithCommentsId);
            assertThat(comment.getMessage()).isNotNull();
            assertThat(comment.getUpdateDate()).isNotNull();
        }

        @Test
        @Tag("query")
        @DisplayName("should return empty comments list when tickler has no comments")
        void shouldReturnEmptyCommentsList_whenTicklerHasNoComments() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then
            // Find a tickler without comments (from base test data)
            TicklerListDTO ticklerWithoutComments = results.stream()
                .filter(dto -> !dto.getId().equals(ticklerWithCommentsId))
                .findFirst()
                .orElse(null);

            if (ticklerWithoutComments != null) {
                assertThat(ticklerWithoutComments.getComments()).isNotNull();
                assertThat(ticklerWithoutComments.getComments()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("null relationship handling")
    class NullRelationshipHandling {

        @Test
        @Tag("query")
        @DisplayName("should handle null demographics gracefully")
        void shouldHandleNullDemographics_gracefully() {
            // Given - Create a tickler with a non-existent demographic
            Tickler ticklerWithNullDemo = new Tickler();
            ticklerWithNullDemo.setDemographicNo(99999); // Non-existent
            ticklerWithNullDemo.setMessage("Orphan tickler");
            ticklerWithNullDemo.setStatus(Tickler.STATUS.A);
            ticklerWithNullDemo.setPriority(Tickler.PRIORITY.Normal);
            ticklerWithNullDemo.setCreator("999998");
            ticklerWithNullDemo.setTaskAssignedTo("999998");
            ticklerWithNullDemo.setServiceDate(new Date());
            ticklerDao.persist(ticklerWithNullDemo);
            entityManager.flush();

            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then - Should not throw exception
            assertThat(results).isNotNull();
            assertThat(results).isNotEmpty();
        }

        @Test
        @Tag("query")
        @DisplayName("should handle null providers gracefully")
        void shouldHandleNullProviders_gracefully() {
            // Given - Create a tickler with non-existent provider
            Tickler ticklerWithNullProvider = new Tickler();
            ticklerWithNullProvider.setDemographicNo(2002);
            ticklerWithNullProvider.setMessage("Tickler with bad provider");
            ticklerWithNullProvider.setStatus(Tickler.STATUS.A);
            ticklerWithNullProvider.setPriority(Tickler.PRIORITY.Normal);
            ticklerWithNullProvider.setCreator("ZZZZZZ"); // Non-existent
            ticklerWithNullProvider.setTaskAssignedTo("ZZZZZZ"); // Non-existent
            ticklerWithNullProvider.setServiceDate(new Date());
            ticklerDao.persist(ticklerWithNullProvider);
            entityManager.flush();

            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then - Should not throw exception
            assertThat(results).isNotNull();

            // Verify the DTO handles null provider names gracefully
            TicklerListDTO orphanTickler = results.stream()
                .filter(dto -> "Tickler with bad provider".equals(dto.getMessage()))
                .findFirst()
                .orElse(null);

            if (orphanTickler != null) {
                // Should return N/A for null providers
                assertThat(orphanTickler.getCreatorFormattedName()).isEqualTo("N/A");
                assertThat(orphanTickler.getAssigneeFormattedName()).isEqualTo("N/A");
            }
        }
    }

    @Nested
    @DisplayName("priority filter")
    class PriorityFilter {

        @Test
        @Tag("filter")
        @DisplayName("should filter by priority when specified")
        void shouldFilterByPriority_whenSpecified() {
            // Given
            CustomFilter filter = new CustomFilter();
            filter.setStatus("A");
            filter.setPriority("High");

            // When
            List<TicklerListDTO> results = ticklerDao.getTicklerDTOs(filter);

            // Then
            assertThat(results).isNotNull();
            assertThat(results).allMatch(dto -> dto.getPriority() == Tickler.PRIORITY.High);
        }
    }
}

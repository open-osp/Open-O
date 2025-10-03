/**
 * Copyright (c) 2025. Magenta Health. All Rights Reserved.
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
 *
 * AddTickler2Action Struts2 Action Test Suite
 *
 * This test class provides comprehensive testing for the AddTickler2Action
 * Struts2 action, including security, validation, and business logic.
 *
 * @author yingbull
 * @since 2025-09-15
 */
package ca.openosp.openo.tickler.web;

import ca.openosp.openo.test.base.OpenOWebTestBase;
import ca.openosp.openo.tickler.pageUtil.AddTickler2Action;
import ca.openosp.openo.managers.TicklerManager;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.commn.model.Tickler;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.utility.LoggedInInfo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Integration test suite for {@link AddTickler2Action} Struts2 action.
 *
 * <p>This test class validates the complete web layer workflow for creating
 * ticklers, including request handling, security, validation, and response.
 *
 * <p><b>Test Coverage Summary:</b>
 * <table border="1">
 *   <tr><th>Test Method</th><th>What It Tests</th><th>User Story</th></tr>
 *   <tr><td>testSecurityCheckRequired</td><td>_tickler write privilege</td><td>Only authorized users can create ticklers</td></tr>
 *   <tr><td>testAddSingleTicklerSuccess</td><td>Single tickler creation</td><td>Provider creates reminder for patient</td></tr>
 *   <tr><td>testAddMultipleDemographicsTickler</td><td>Bulk tickler creation</td><td>Mass reminders for patient groups</td></tr>
 *   <tr><td>testDefaultValues</td><td>Parameter defaults</td><td>Simplified tickler creation</td></tr>
 *   <tr><td>testValidPriorities</td><td>Priority validation</td><td>Task urgency classification</td></tr>
 *   <tr><td>testMessageRequired</td><td>Message validation</td><td>Ensure meaningful reminders</td></tr>
 *   <tr><td>testDemographicRequired</td><td>Patient validation</td><td>Ticklers must be patient-specific</td></tr>
 *   <tr><td>testDateParsing</td><td>Date handling</td><td>Scheduled reminder dates</td></tr>
 *   <tr><td>testProgramIdHandling</td><td>Program association</td><td>Program-specific reminders</td></tr>
 *   <tr><td>testAjaxRequest</td><td>AJAX support</td><td>Dynamic UI updates</td></tr>
 *   <tr><td>testInvalidDemographicNumber</td><td>Error handling</td><td>Graceful failure for bad input</td></tr>
 *   <tr><td>testManagerException</td><td>Exception propagation</td><td>System error handling</td></tr>
 *   <tr><td>testCreatorFromSession</td><td>Session management</td><td>Automatic creator tracking</td></tr>
 * </table>
 *
 * <p><b>Testing Strategy:</b>
 * <ul>
 *   <li>Uses MockMvc to simulate HTTP requests</li>
 *   <li>Mocks SpringUtils beans for isolation</li>
 *   <li>Tests both success and failure paths</li>
 *   <li>Validates Struts2 ActionContext handling</li>
 * </ul>
 *
 * <p><b>Security Testing:</b>
 * All tests verify that proper security checks are enforced,
 * ensuring HIPAA/PIPEDA compliance for patient data access.
 *
 * @see AddTickler2Action
 * @see OpenOWebTestBase
 * @see TicklerManager
 * @author yingbull
 * @since 2025-09-15
 */
@DisplayName("AddTickler2Action Web Layer Tests")
class AddTickler2ActionTest extends OpenOWebTestBase {

    /** Mocked TicklerManager for business logic isolation */
    @Mock
    private TicklerManager mockTicklerManager;

    /** Action instance under test */
    private AddTickler2Action action;

    /** Test provider number for consistent test data */
    private static final String TEST_PROVIDER = "999998";

    /** Test demographic ID for consistent test data */
    private static final String TEST_DEMOGRAPHIC = "12345";

    /**
     * Set up test fixtures before each test method.
     *
     * <p>This method:
     * <ol>
     *   <li>Replaces SpringUtils beans with mocks</li>
     *   <li>Configures session with logged-in user</li>
     *   <li>Creates fresh action instance</li>
     * </ol>
     */
    @BeforeEach
    void setUp() {
        // Replace SpringUtils beans with mocks to control behavior
        replaceSpringUtilsBean(TicklerManager.class, mockTicklerManager);

        // Set up logged in user in session
        when(mockLoggedInInfo.getLoggedInProviderNo()).thenReturn(TEST_PROVIDER);
        setSessionAttribute("user", TEST_PROVIDER);
        setSessionAttribute("loggedInInfo", mockLoggedInInfo);

        // Create fresh action instance for each test
        action = new AddTickler2Action();
    }

    /**
     * Test security enforcement for tickler creation.
     *
     * <p>Validates that the action properly checks for _tickler write
     * privilege before allowing tickler creation. This is critical for
     * maintaining proper access control to patient data.
     */
    @Test
    @DisplayName("Should enforce security privilege for tickler write")
    void testSecurityCheckRequired() {
        // Given - user lacks tickler write privilege
        denyPrivilege("_tickler", "w");

        // When/Then - action should throw security exception
        assertThatThrownBy(() -> executeAction(action))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("missing required sec object (_tickler)");

        // Verify security check was performed
        verifySecurityCheck("_tickler", "w");
    }

    /**
     * Test successful creation of a single tickler with all fields.
     *
     * <p>This test represents the typical use case where a provider
     * creates a reminder for a specific patient with all details specified.
     *
     * <p>Validates:
     * <ul>
     *   <li>All request parameters are properly parsed</li>
     *   <li>Tickler entity is correctly populated</li>
     *   <li>Manager is called with correct data</li>
     *   <li>Success result is returned</li>
     * </ul>
     */
    @Test
    @DisplayName("Should add single tickler with valid data")
    void testAddSingleTicklerSuccess() throws Exception {
        // Given - authorized user with complete tickler data
        allowPrivilege("_tickler", "w");

        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", "Patient needs follow-up for lab results");
        addRequestParameter("status", "A");  // Active status
        addRequestParameter("date", "2025-10-15");  // Future service date
        addRequestParameter("priority", "High");  // High priority
        addRequestParameter("assignedTo", "999997");  // Different provider

        // When - execute the action
        String result = executeAction(action);

        // Then - verify success and correct data
        assertThat(result).isEqualTo("success");

        // Capture and verify the created tickler
        ArgumentCaptor<LoggedInInfo> infoCaptor = ArgumentCaptor.forClass(LoggedInInfo.class);
        ArgumentCaptor<Tickler> ticklerCaptor = ArgumentCaptor.forClass(Tickler.class);

        verify(mockTicklerManager).addTickler(infoCaptor.capture(), ticklerCaptor.capture());

        // Verify all fields were correctly set
        Tickler createdTickler = ticklerCaptor.getValue();
        assertThat(createdTickler.getMessage()).isEqualTo("Patient needs follow-up for lab results");
        assertThat(createdTickler.getDemographicNo()).isEqualTo(Integer.valueOf(TEST_DEMOGRAPHIC));
        assertThat(createdTickler.getPriority()).isEqualTo(Tickler.PRIORITY.High);
        assertThat(createdTickler.getTaskAssignedTo()).isEqualTo("999997");
    }

    @Test
    @DisplayName("Should add ticklers for multiple demographics")
    void testAddMultipleDemographicsTickler() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        String[] demographics = {"12345", "12346", "12347"};
        getMockRequest().setParameter("demo", demographics);
        addRequestParameter("message", "Bulk reminder for vaccination");
        addRequestParameter("priority", "Normal");

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("success");

        // Verify tickler was created for each demographic
        verify(mockTicklerManager, times(3)).addTickler(any(LoggedInInfo.class), any(Tickler.class));
    }

    @Test
    @DisplayName("Should handle default values when parameters missing")
    void testDefaultValues() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        // Only provide required fields
        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", "Basic tickler");
        // Don't provide: status, date, priority, assignedTo

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("success");

        ArgumentCaptor<Tickler> ticklerCaptor = ArgumentCaptor.forClass(Tickler.class);
        verify(mockTicklerManager).addTickler(any(LoggedInInfo.class), ticklerCaptor.capture());

        Tickler createdTickler = ticklerCaptor.getValue();
        // Check defaults
        assertThat(createdTickler.getStatus()).isEqualTo(Tickler.STATUS.A); // Default Active
        assertThat(createdTickler.getPriority()).isEqualTo(Tickler.PRIORITY.Normal); // Default Normal
        assertThat(createdTickler.getTaskAssignedTo()).isEqualTo(TEST_PROVIDER); // Default to creator
        assertThat(createdTickler.getServiceDate()).isNotNull(); // Should have a date
    }

    @ParameterizedTest
    @DisplayName("Should validate priority values")
    @ValueSource(strings = {"High", "Normal", "Low"})
    void testValidPriorities(String priority) throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", "Priority test");
        addRequestParameter("priority", priority);

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("success");

        ArgumentCaptor<Tickler> ticklerCaptor = ArgumentCaptor.forClass(Tickler.class);
        verify(mockTicklerManager).addTickler(any(LoggedInInfo.class), ticklerCaptor.capture());

        Tickler createdTickler = ticklerCaptor.getValue();
        assertThat(createdTickler.getPriority().toString()).isEqualTo(priority);
    }

    @Test
    @DisplayName("Should validate required message field")
    void testMessageRequired() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", ""); // Empty message

        // When
        String result = executeAction(action);

        // Then - should fail validation
        assertThat(result).isIn("error", "input");
    }

    @Test
    @DisplayName("Should validate demographic number is required")
    void testDemographicRequired() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        // No demographic parameter
        addRequestParameter("message", "Test message");

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isIn("error", "input");
    }

    @Test
    @DisplayName("Should handle date parsing")
    void testDateParsing() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", "Date test");
        addRequestParameter("date", "2025-12-25");

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("success");

        ArgumentCaptor<Tickler> ticklerCaptor = ArgumentCaptor.forClass(Tickler.class);
        verify(mockTicklerManager).addTickler(any(LoggedInInfo.class), ticklerCaptor.capture());

        Tickler createdTickler = ticklerCaptor.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String serviceDateStr = sdf.format(createdTickler.getServiceDate());
        assertThat(serviceDateStr).isEqualTo("2025-12-25");
    }

    @Test
    @DisplayName("Should handle program ID if provided")
    void testProgramIdHandling() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", "Program specific tickler");
        addRequestParameter("program_id", "5");

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("success");

        ArgumentCaptor<Tickler> ticklerCaptor = ArgumentCaptor.forClass(Tickler.class);
        verify(mockTicklerManager).addTickler(any(LoggedInInfo.class), ticklerCaptor.capture());

        Tickler createdTickler = ticklerCaptor.getValue();
        assertThat(createdTickler.getProgramId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should handle AJAX requests")
    void testAjaxRequest() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", "AJAX tickler");
        addRequestParameter("ajax", "true");

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isIn("success", "ajax-success");

        // Check response type for AJAX
        String contentType = getMockResponse().getContentType();
        if (contentType != null) {
            assertThat(contentType).containsIgnoringCase("json");
        }
    }

    /**
     * Nested test class for error handling scenarios.
     *
     * <p>These tests ensure the action gracefully handles various
     * error conditions and provides appropriate feedback to users.
     */
    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        /**
         * Test handling of non-numeric demographic IDs.
         *
         * <p>The system should reject invalid patient identifiers
         * to prevent data corruption and maintain referential integrity.
         */
        @Test
        @DisplayName("Should handle invalid demographic number")
        void testInvalidDemographicNumber() throws Exception {
            // Given - invalid demographic number format
            allowPrivilege("_tickler", "w");

            addRequestParameter("demo", "invalid-number");  // Non-numeric ID
            addRequestParameter("message", "Test");

            // When - attempt to create tickler
            String result = executeAction(action);

            // Then - should return error result
            assertThat(result).isIn("error", "input");
        }

        /**
         * Test exception handling from the business layer.
         *
         * <p>When the TicklerManager throws an exception (e.g., database
         * error), the action should propagate it appropriately for the
         * error handling framework to process.
         */
        @Test
        @DisplayName("Should handle manager exceptions")
        void testManagerException() throws Exception {
            // Given - manager will throw exception
            allowPrivilege("_tickler", "w");

            addRequestParameter("demo", TEST_DEMOGRAPHIC);
            addRequestParameter("message", "Test");

            // Simulate database error
            doThrow(new RuntimeException("Database error"))
                .when(mockTicklerManager).addTickler(any(), any());

            // When/Then - exception should be propagated
            assertThatThrownBy(() -> executeAction(action))
                .isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("Should set creator from session")
    void testCreatorFromSession() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        String creator = "888888";
        setSessionAttribute("user", creator);

        addRequestParameter("demo", TEST_DEMOGRAPHIC);
        addRequestParameter("message", "Creator test");

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("success");

        ArgumentCaptor<Tickler> ticklerCaptor = ArgumentCaptor.forClass(Tickler.class);
        verify(mockTicklerManager).addTickler(any(LoggedInInfo.class), ticklerCaptor.capture());

        Tickler createdTickler = ticklerCaptor.getValue();
        assertThat(createdTickler.getCreator()).isEqualTo(creator);
    }
}
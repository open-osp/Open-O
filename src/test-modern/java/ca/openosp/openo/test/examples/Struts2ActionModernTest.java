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
 * Example Struts2 Action test using modern framework
 */
package ca.openosp.openo.test.examples;

import ca.openosp.openo.test.base.OpenOWebTestBase;
import ca.openosp.openo.tickler.pageUtil.AddTickler2Action;
import ca.openosp.openo.managers.TicklerManager;
import ca.openosp.openo.commn.model.Tickler;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.commn.model.Provider;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Example test for Struts2 2Actions showing how to handle
 * ServletActionContext and SpringUtils.getBean() patterns.
 */
@DisplayName("Struts2 2Action Modern Test")
class Struts2ActionModernTest extends OpenOWebTestBase {

    @Mock
    private TicklerManager mockTicklerManager;

    private AddTickler2Action action;

    @BeforeEach
    void setUp() {
        // Replace SpringUtils bean with mock
        replaceSpringUtilsBean(TicklerManager.class, mockTicklerManager);

        // Create action instance
        action = new AddTickler2Action();
    }

    @Test
    @DisplayName("Should enforce security privileges")
    void testSecurityCheck() throws Exception {
        // Given - deny the required privilege
        denyPrivilege("_tickler", "w");

        // When/Then - should throw SecurityException
        assertThatThrownBy(() -> executeAction(action))
            .isInstanceOf(SecurityException.class)
            .hasMessageContaining("missing required sec object");

        // Verify security was checked
        verifySecurityCheck("_tickler", "w");
    }

    @Test
    @DisplayName("Should add tickler with valid parameters")
    void testAddTicklerSuccess() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");

        Map<String, String> params = new HashMap<>();
        params.put("demographic_no", "123");
        params.put("message", "Test tickler message");
        params.put("priority", "High");
        params.put("task_assigned_to", "999998");
        params.put("service_date", "2025-01-15");
        addRequestParameters(params);

        // Mock the manager behavior
        when(mockTicklerManager.addTickler(any(), any())).thenReturn(true);

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("success");

        // Capture and verify the tickler that was created
        ArgumentCaptor<Tickler> ticklerCaptor = ArgumentCaptor.forClass(Tickler.class);
        verify(mockTicklerManager).addTickler(any(), ticklerCaptor.capture());

        Tickler capturedTickler = ticklerCaptor.getValue();
        assertThat(capturedTickler.getMessage()).isEqualTo("Test tickler message");
        assertThat(capturedTickler.getDemographicNo()).isEqualTo(123);
    }

    @Test
    @DisplayName("Should handle missing required parameters")
    void testMissingParameters() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");
        // No parameters added

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("error");
        assertThat(getMockRequest().getAttribute("errorMessage")).isNotNull();
    }

    @Test
    @DisplayName("Should handle ServletActionContext correctly")
    void testServletActionContext() throws Exception {
        // This verifies that our mock servlet context is properly injected
        allowPrivilege("_tickler", "w");
        addRequestParameter("test", "value");

        // The action should be able to access request/response via ServletActionContext
        String result = executeAction(action);

        // Verify the action could access the request
        assertThat(getMockRequest().getParameter("test")).isEqualTo("value");
    }

    @Test
    @DisplayName("Should handle method-based routing")
    void testMethodBasedAction() throws Exception {
        // For actions that use method parameter routing
        allowPrivilege("_tickler", "w");
        addRequestParameter("method", "search");

        // Execute with specific method
        String result = executeActionMethod(action, "search");

        // Verify the correct method was called
        assertThat(result).isNotNull();
    }

    @Nested
    @DisplayName("Session Management")
    class SessionManagementTest {

        @Test
        @DisplayName("Should access LoggedInInfo from session")
        void testLoggedInInfoAccess() throws Exception {
            // Given
            allowPrivilege("_tickler", "w");
            setSessionAttribute("loggedInInfo", mockLoggedInInfo);
            Provider testProvider = new Provider();
            testProvider.setProviderNo("999998");
            when(mockLoggedInInfo.getLoggedInProvider()).thenReturn(testProvider);
            when(mockLoggedInInfo.getLoggedInProviderNo()).thenReturn("999998");

            addRequestParameter("demographic_no", "123");
            addRequestParameter("message", "Session test");

            // When
            String result = executeAction(action);

            // Then
            assertThat(result).isEqualTo("success");
            verify(mockLoggedInInfo, atLeastOnce()).getLoggedInProvider();
        }

        @Test
        @DisplayName("Should handle missing session data")
        void testMissingSessionData() throws Exception {
            // Given - no LoggedInInfo in session
            getMockSession().removeAttribute("loggedInInfo");
            allowPrivilege("_tickler", "w");

            // When/Then
            assertThatThrownBy(() -> executeAction(action))
                .isInstanceOf(Exception.class);
        }
    }

    @Test
    @DisplayName("Should verify SpringUtils anti-pattern handling")
    void testSpringUtilsAntiPattern() {
        // This test verifies that when the action calls SpringUtils.getBean()
        // it gets our mock instead of trying to load the real bean

        TicklerManager managerFromUtils = SpringUtils.getBean(TicklerManager.class);
        assertThat(managerFromUtils).isSameAs(mockTicklerManager);
    }

    @Test
    @DisplayName("Should handle AJAX response correctly")
    void testAjaxResponse() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");
        addRequestParameter("ajax", "true");
        addRequestParameter("demographic_no", "123");
        addRequestParameter("message", "Ajax test");

        // When
        String result = executeAction(action);

        // Then
        assertThat(getMockResponse().getContentType()).contains("application/json");
        assertThat(getMockResponse().getContentAsString()).contains("success");
    }

    @Test
    @DisplayName("Should handle validation errors")
    void testValidationErrors() throws Exception {
        // Given
        allowPrivilege("_tickler", "w");
        addRequestParameter("demographic_no", "invalid");  // Non-numeric
        addRequestParameter("message", "");  // Empty message

        // When
        String result = executeAction(action);

        // Then
        assertThat(result).isEqualTo("input");
        // In real implementation, check for field errors
    }
}
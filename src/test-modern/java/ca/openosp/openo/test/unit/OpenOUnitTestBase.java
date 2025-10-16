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
 */
package ca.openosp.openo.test.unit;

import ca.openosp.openo.utility.SpringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

/**
 * Base class for unit tests in OpenO EMR that need to mock the SpringUtils static dependency.
 *
 * <p>This class provides infrastructure for true unit testing despite the SpringUtils anti-pattern
 * used throughout the codebase. It uses Mockito's MockedStatic feature to mock static method calls
 * to SpringUtils.getBean(), allowing tests to inject mocks without requiring a Spring context.</p>
 *
 * <p><b>Usage Pattern:</b></p>
 * <pre>
 * public class MyServiceUnitTest extends OpenOUnitTestBase {
 *     &#64;Mock
 *     private SomeDao mockDao;
 *
 *     &#64;BeforeEach
 *     void setUp() {
 *         registerMock(SomeDao.class, mockDao);
 *     }
 *
 *     &#64;Test
 *     void testServiceMethod() {
 *         // Service will get mockDao from SpringUtils.getBean()
 *         MyService service = new MyService();
 *         service.doSomething();
 *         verify(mockDao).save(any());
 *     }
 * }
 * </pre>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Automatic MockedStatic setup and teardown for SpringUtils</li>
 *   <li>Registry for managing mock beans returned by SpringUtils.getBean()</li>
 *   <li>No Spring context required - tests run in milliseconds</li>
 *   <li>Proper cleanup to prevent test pollution</li>
 * </ul>
 *
 * @since 2025-01-17
 * @see SpringUtils
 */
@Tag("unit")
@Tag("fast")
public abstract class OpenOUnitTestBase {

    /**
     * MockedStatic instance for SpringUtils that will be closed after each test.
     */
    protected MockedStatic<SpringUtils> springUtilsMock;

    /**
     * Registry of mocked beans that SpringUtils.getBean() should return.
     * Key: Class type, Value: Mock instance
     */
    protected Map<Class<?>, Object> mockedBeans = new HashMap<>();

    /**
     * Sets up the MockedStatic for SpringUtils before each test.
     * Configures SpringUtils.getBean() to return mocks from the registry.
     */
    @BeforeEach
    void setUpSpringUtilsMocking() {
        // Create MockedStatic for SpringUtils
        springUtilsMock = mockStatic(SpringUtils.class);

        // Configure SpringUtils.getBean() to return mocks from our registry
        springUtilsMock.when(() -> SpringUtils.getBean(any(Class.class)))
            .thenAnswer(invocation -> {
                Class<?> clazz = invocation.getArgument(0);
                Object mock = mockedBeans.get(clazz);
                if (mock == null) {
                    throw new IllegalStateException(
                        "No mock registered for " + clazz.getName() +
                        ". Call registerMock() in your @BeforeEach method."
                    );
                }
                return mock;
            });

        // Note: SpringUtils only has getBean(Class) method, not getBean(String, Class)
    }

    /**
     * Cleans up the MockedStatic after each test to prevent test pollution.
     */
    @AfterEach
    void tearDownSpringUtilsMocking() {
        if (springUtilsMock != null) {
            springUtilsMock.close();
        }
        mockedBeans.clear();
    }

    /**
     * Registers a mock to be returned by SpringUtils.getBean() for the given class.
     *
     * @param clazz The class type to register
     * @param mock The mock instance to return
     * @param <T> The type of the class
     */
    protected <T> void registerMock(Class<T> clazz, T mock) {
        mockedBeans.put(clazz, mock);
    }

    /**
     * Creates a mock and registers it automatically.
     * Useful for quick mock setup.
     *
     * @param clazz The class to mock
     * @param <T> The type of the class
     * @return The created mock
     */
    protected <T> T createAndRegisterMock(Class<T> clazz) {
        T mock = Mockito.mock(clazz);
        registerMock(clazz, mock);
        return mock;
    }

    /**
     * Clears all registered mocks. Useful for resetting state between test methods.
     */
    protected void clearMocks() {
        mockedBeans.clear();
    }
}
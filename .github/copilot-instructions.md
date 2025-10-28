# OpenO EMR - GitHub Copilot Instructions

**PROJECT IDENTITY**: Always refer to this system as "OpenO EMR" or "OpenO" - NOT "OSCAR EMR" or "OSCAR McMaster"

## Core Context

**Domain**: Canadian healthcare EMR system with multi-jurisdictional compliance (BC, ON, generic)
**Stack**: Java 21, Spring 5.3.39, Maven 3, Tomcat 9.0.97, MariaDB/MySQL  
**Architecture**: Multi-layered healthcare web application with complex medical database schema
**Regulatory**: HIPAA/PIPEDA compliance REQUIRED - PHI protection is CRITICAL

## CRITICAL SECURITY REQUIREMENTS

### Web Security - MANDATORY for ALL Code

#### XSS Prevention (Output Encoding)
```java
// JSP/Web Output - Use OWASP Java Encoder
import org.owasp.encoder.Encode;

// HTML contexts
<%= Encode.forHtml(userInput) %>
<div><%= Encode.forHtmlContent(userInput) %></div>

// HTML attributes  
<input value="<%= Encode.forHtmlAttribute(userInput) %>" />

// JavaScript contexts
<script>
var data = '<%= Encode.forJavaScript(userInput) %>';
var json = <%= Encode.forJavaScriptBlock(jsonString) %>;
</script>

// URL contexts
<a href="<%= Encode.forUriComponent(userInput) %>">Link</a>

// CSS contexts
<style>
.class { background: url('<%= Encode.forCssUrl(userInput) %>'); }
</style>
```

#### SQL Injection Prevention
```java
// ALWAYS use parameterized queries - NEVER concatenate strings
// Good - Using PreparedStatement
String sql = "SELECT * FROM demographic WHERE demographic_no = ?";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setInt(1, demographicNo);

// Good - Using Hibernate/JPA
Query query = entityManager.createQuery("FROM Demographic d WHERE d.demographicNo = :id");
query.setParameter("id", demographicNo);

// NEVER DO THIS - SQL Injection vulnerability
String sql = "SELECT * FROM demographic WHERE demographic_no = " + demographicNo; // WRONG!
```

#### File Path Sanitization
```java
// Use Apache Commons IO for path traversal prevention
import org.apache.commons.io.FilenameUtils;
import java.nio.file.Paths;
import java.nio.file.Path;

// Sanitize filename
String cleanFilename = FilenameUtils.getName(userProvidedFilename);
// Remove any path separators
cleanFilename = cleanFilename.replaceAll("[\\\\/]", "");

// Validate against whitelist pattern
if (!cleanFilename.matches("^[a-zA-Z0-9._-]+$")) {
    throw new SecurityException("Invalid filename");
}

// Use Path API for safe path construction
Path basePath = Paths.get("/secure/base/directory");
Path userPath = basePath.resolve(cleanFilename).normalize();

// Ensure resolved path is within base directory
if (!userPath.startsWith(basePath)) {
    throw new SecurityException("Path traversal attempt detected");
}

// For file uploads - additional validation
String extension = FilenameUtils.getExtension(cleanFilename);
List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "png", "doc");
if (!allowedExtensions.contains(extension.toLowerCase())) {
    throw new SecurityException("File type not allowed");
}
```

#### Command Injection Prevention
```java
// Use ProcessBuilder with array arguments - NEVER concatenate
ProcessBuilder pb = new ProcessBuilder(
    "command",
    "--flag",
    userInput  // Passed as separate argument, not concatenated
);

// NEVER use Runtime.exec() with string concatenation
// WRONG: Runtime.getRuntime().exec("cmd " + userInput);
```

### Authorization - REQUIRED for EVERY Action

```java
public class Example2Action extends ActionSupport {
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public String execute() {
        // MANDATORY security check - MUST be first operation
        if (!securityInfoManager.hasPrivilege(
                LoggedInInfo.getLoggedInInfoFromSession(request), 
                "_objectname",  // Security object name
                "r",           // Permission type: r=read, w=write, d=delete
                null)) {
            throw new SecurityException("missing required security object: _objectname");
        }
        
        // Log PHI access for audit trail
        LogAction.addLogSynchronous(
            LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo(),
            LogConst.ACTION_READ,  // or ACTION_UPDATE, ACTION_DELETE
            LogConst.CON_DEMOGRAPHIC,  // Content type
            demographicNo,  // Record ID
            request.getRemoteAddr(),
            demographicData  // Data accessed
        );
        
        // Business logic here
        return "success";
    }
}
```

### CSRF Protection
```java
// CSRF tokens are automatically handled by OWASP CSRF Guard
// Ensure forms include the token:
<form action="action.do" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <!-- form fields -->
</form>
```

### Session Security
```java
// Always use LoggedInInfo for session management
LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

// Validate session
if (loggedInInfo == null || loggedInInfo.getLoggedInProvider() == null) {
    throw new SecurityException("Not authenticated");
}

// Check timeout
if (loggedInInfo.isExpired()) {
    session.invalidate();
    return "login";
}
```

## Package Structure (2025 Migration)

**CRITICAL**: Use NEW namespace `ca.openosp.openo.*` for ALL new code
- **Old**: `org.oscarehr.*`, `oscar.*` → **New**: `ca.openosp.openo.*`
- **DAO Classes**: `ca.openosp.openo.commn.dao.*` (note: "commn" not "common")
- **Models**: `ca.openosp.openo.commn.model.*`
- **Special Case**: `ProviderDao` at `ca.openosp.openo.dao.ProviderDao`

## Struts2 Action Pattern ("2Action")

### REQUIRED Structure for ALL New Actions
```java
package ca.openosp.openo.module.web;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.owasp.encoder.Encode;

public class Feature2Action extends ActionSupport {
    private static final long serialVersionUID = 1L;
    
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();
    
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private SomeManager someManager = SpringUtils.getBean(SomeManager.class);
    
    public String execute() {
        // 1. MANDATORY Security Check - ALWAYS FIRST
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_object", "r", null)) {
            throw new SecurityException("missing required security object");
        }
        
        // 2. Input Validation & Sanitization
        String userInput = request.getParameter("input");
        if (userInput != null) {
            // Sanitize for logging
            userInput = Encode.forJava(userInput);
            // Validate against expected pattern
            if (!userInput.matches("^[a-zA-Z0-9-]+$")) {
                addActionError("Invalid input format");
                return "error";
            }
        }
        
        // 3. Audit Logging for PHI Access
        if (accessingPatientData) {
            LogAction.addLogSynchronous(
                loggedInInfo.getLoggedInProviderNo(),
                LogConst.ACTION_READ,
                LogConst.CON_DEMOGRAPHIC,
                demographicNo,
                request.getRemoteAddr(),
                "Accessed patient record"
            );
        }
        
        // 4. Business Logic with Transaction Management
        try {
            someManager.performOperation(userInput);
        } catch (Exception e) {
            // Log error without exposing sensitive data
            logger.error("Operation failed for user: " + 
                        loggedInInfo.getLoggedInProviderNo(), e);
            addActionError("Operation failed");
            return "error";
        }
        
        // 5. Set response data with proper encoding
        request.setAttribute("result", Encode.forHtml(result));
        
        return "success";
    }
}
```

## Healthcare Domain Standards

### HL7 Message Processing
```java
// Always validate HL7 messages
MSH msh = (MSH) message.get("MSH");
if (!isValidSendingFacility(msh.getSendingFacility())) {
    throw new HL7Exception("Invalid sending facility");
}
```

### FHIR Resource Handling
```java
// Use HAPI FHIR validation
FhirValidator validator = fhirContext.newValidator();
ValidationResult result = validator.validateWithResult(resource);
if (!result.isSuccessful()) {
    throw new ValidationException(result.getMessages());
}
```

### Provincial Billing Security
```java
// Validate provider billing privileges
if (!billingSecurityManager.canBillForService(
        provider, serviceCode, province)) {
    throw new BillingSecurityException("Provider not authorized for service");
}
```

## Database Security Patterns

### Audit Trail - REQUIRED for ALL Tables
```sql
-- Every table MUST include:
lastUpdateUser VARCHAR(100) NOT NULL,
lastUpdateDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
```

### Hibernate Security
```java
// Use Hibernate Criteria API with restrictions
Criteria criteria = session.createCriteria(Demographic.class);
criteria.add(Restrictions.eq("demographicNo", demographicNo));
criteria.add(Restrictions.eq("activeCount", 1)); // Only active records

// NEVER build HQL with string concatenation
// WRONG: "FROM Demographic WHERE id = " + userId
```

### Data Encryption for PHI
```java
// Encrypt sensitive data before storage
import ca.openosp.openo.utilities.encryption.EncryptionUtils;

String encryptedSIN = EncryptionUtils.encrypt(socialInsuranceNumber);
demographic.setHin(encryptedSIN);

// Decrypt when needed
String sin = EncryptionUtils.decrypt(demographic.getHin());
```

## Testing Requirements

### Security Test Patterns
```java
@Test
public void testUnauthorizedAccess() {
    // Test missing privileges
    when(securityInfoManager.hasPrivilege(any(), eq("_object"), any(), any()))
        .thenReturn(false);
    
    assertThrows(SecurityException.class, () -> {
        action.execute();
    });
}

@Test
public void testSQLInjectionPrevention() {
    String maliciousInput = "'; DROP TABLE demographic; --";
    
    // Should sanitize or reject
    assertDoesNotThrow(() -> {
        dao.findByName(maliciousInput);
    });
    
    // Verify parameterized query was used
    verify(preparedStatement).setString(1, maliciousInput);
}

@Test
public void testXSSPrevention() {
    String xssPayload = "<script>alert('XSS')</script>";
    action.setUserInput(xssPayload);
    
    String result = action.execute();
    
    // Verify output is encoded
    String output = (String) request.getAttribute("output");
    assertFalse(output.contains("<script>"));
    assertTrue(output.contains("&lt;script&gt;"));
}
```

## Code Review Checklist

Before submitting ANY code:

- [ ] All user inputs are validated and sanitized using OWASP Encoder
- [ ] All database queries use parameterized statements or Hibernate Criteria
- [ ] File operations validate paths and prevent directory traversal
- [ ] Security privilege checks are present and correct
- [ ] PHI access is logged for audit trail
- [ ] No sensitive data in logs or error messages
- [ ] CSRF tokens included in forms
- [ ] Session validation performed
- [ ] Unit tests include security test cases
- [ ] CodeQL security scan passes

## Common Security Anti-Patterns to AVOID

```java
// NEVER: String concatenation in SQL
String sql = "SELECT * FROM table WHERE id = " + userId;  // WRONG!

// NEVER: Direct output of user input
out.println(request.getParameter("input"));  // WRONG!

// NEVER: Path concatenation without validation
File file = new File("/base/" + userPath);  // WRONG!

// NEVER: Logging sensitive data
logger.info("User SIN: " + socialInsuranceNumber);  // WRONG!

// NEVER: Skipping security checks
// if (isDevelopment) return "success";  // WRONG!

// NEVER: Catching and ignoring security exceptions
try {
    securityCheck();
} catch (SecurityException e) {
    // Ignored  // WRONG!
}

// NEVER: Using weak encryption
MD5 md5 = new MD5();  // WRONG! Use BCrypt or Argon2

// NEVER: Hardcoded credentials
String password = "admin123";  // WRONG!
```

## Security Resources

- **OWASP Java Encoder**: https://github.com/OWASP/owasp-java-encoder
- **Apache Commons IO**: For file operations and path validation
- **Apache Commons Validator**: For input validation patterns
- **OWASP CSRF Guard**: Automatic CSRF protection (already integrated)
- **BCrypt**: For password hashing (already integrated)
- **CodeQL**: GitHub security scanning (must pass before merge)

## Critical Files to Review

- `SecurityInfoManager.java` - Core authorization patterns
- `LoggedInInfo.java` - Session management
- `Encode` usage in JSPs - XSS prevention examples
- `*2Action.java` files - Proper action implementation
- `web.xml` - Security filter configuration

## Remember

1. **Security is NOT optional** - Every PR will be reviewed for security
2. **PHI protection is legally required** - Violations have serious consequences  
3. **When in doubt, ask** - Better to clarify than introduce vulnerabilities
4. **Follow the patterns** - Existing secure code provides examples
5. **Test security cases** - Include security tests for all new features

This is a healthcare system handling sensitive patient data. Security is our top priority.
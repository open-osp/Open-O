# OpenO EMR - Healthcare Electronic Medical Records System

**PROJECT IDENTITY**: Always refer to this system as "OpenO EMR" or "OpenO" - NOT "OSCAR EMR" or "OSCAR McMaster"

## Core Context 

**Domain**: Canadian healthcare EMR system with multi-jurisdictional compliance (BC, ON, generic)
**Stack**: Java 21, Spring 5.3.39, Maven 3, Tomcat 9.0.97, MariaDB/MySQL  
**Architecture**: Multi-layered healthcare web application with complex medical database schema
**Regulatory**: HIPAA/PIPEDA compliance REQUIRED - PHI protection is CRITICAL

## Essential Commands

```bash
# Development Workflow
make clean                    # Clean project and remove deployed app
make install                  # Build and deploy without tests  
make install --run-tests      # Build, test, and deploy
server start/stop/restart     # Tomcat management
server log                    # Tail application logs

# Maven Operations
mvn clean compile            # Build Java project
mvn test                     # Run test suite
mvn package                  # Create WAR file

# Database & Environment
db-connect                   # Connect to MariaDB as root
debug-on / debug-off         # Toggle DEBUG/INFO logging levels

# AI Development Tools
claude                       # Claude Code CLI
aider                        # AI pair programming
aider --model claude-3-5-sonnet-20241022
gh pr create                 # GitHub pull request creation
```

## Critical Security Requirements

**MANDATORY for all code changes:**
- Use `Encode.forHtml()`, `Encode.forJavaScript()` for ALL user inputs
- Parameterized queries ONLY - never string concatenation
- ALL actions MUST include `SecurityInfoManager.hasPrivilege()` checks
- CodeQL security scanning must pass
- PHI (Patient Health Information) must NEVER be logged or exposed

## CSRF Protection Requirements (OWASP CSRF Guard 4.5.0)

**CRITICAL**: OpenO uses OWASP CSRF Guard 4.5.0 with custom filters. Forms loaded dynamically via AJAX require special handling.

### Form Requirements
All POST/PUT/DELETE forms MUST have:
```html
<form action="/path" method="post" name="formName" id="formId">
```

### Dynamic Content Pattern
**CRITICAL**: Forms loaded via AJAX after page init won't have CSRF tokens automatically!

CSRF Guard 4.5.0 provides global variables:
- `window.CSRF_TOKEN_NAME` = "CSRF-TOKEN" (field name)
- `window.CSRF_TOKEN_VALUE` = actual token value

```javascript
// WRONG - Will cause 403 Forbidden
$('#container').html(ajaxResponseHtml);

// CORRECT - Manually add token to dynamically loaded forms
$('#container').html(ajaxResponseHtml);
setTimeout(function() {
    if (window.CSRF_TOKEN_NAME && window.CSRF_TOKEN_VALUE) {
        $('form').each(function() {
            if ($(this).find('input[name="' + window.CSRF_TOKEN_NAME + '"]').length === 0) {
                $('<input>').attr({
                    type: 'hidden',
                    name: window.CSRF_TOKEN_NAME,
                    value: window.CSRF_TOKEN_VALUE
                }).appendTo(this);
            }
        });
    }
}, 100);
```

### Common CSRF Issues
- **403 on form submit**: Check if form was loaded dynamically
- **Missing token**: Form needs `method="post"` and `name` attributes
- **AJAX forms**: Must call `injectTokens()` after DOM insertion
- **Token in URL**: Never use `window.location` with parameters - use `postNavigate()` function instead

### URL Parameter Protection Pattern (November 2024 Update)
**CRITICAL**: CSRF tokens must NEVER appear in URLs. Use POST for navigation with parameters:

```javascript
// WRONG - Exposes CSRF token in URL
window.location.href = 'page.jsp?id=' + id;

// CORRECT - Uses POST to prevent token exposure
function postNavigate(url, params) {
    var form = document.createElement('form');
    form.method = 'POST';
    form.action = url;
    
    for (var key in params) {
        if (params.hasOwnProperty(key)) {
            var field = document.createElement('input');
            field.type = 'hidden';
            field.name = key;
            field.value = params[key];
            form.appendChild(field);
        }
    }
    
    // Add CSRF token if available
    if (window.CSRF_TOKEN_NAME && window.CSRF_TOKEN_VALUE) {
        var csrfField = document.createElement('input');
        csrfField.type = 'hidden';
        csrfField.name = window.CSRF_TOKEN_NAME;
        csrfField.value = window.CSRF_TOKEN_VALUE;
        form.appendChild(csrfField);
    }
    
    document.body.appendChild(form);
    form.submit();
}

// Usage
postNavigate('page.jsp', {id: '123', action: 'edit'});
```

**Full Documentation**: See `/workspace/docs/csrf-protection.md` for complete CSRF implementation details

## Package Structure (2025 Migration)

**CRITICAL**: Use NEW namespace `ca.openosp.openo.*` for ALL code
- **Old**: `org.oscarehr.*`, `oscar.*` → **New**: `ca.openosp.openo.*`
- **DAO Classes**: `ca.openosp.openo.commn.dao.*` (note: "commn" not "common")
- **Models**: `ca.openosp.openo.commn.model.*`
- **Exception**: `ProviderDao` at `ca.openosp.openo.dao.ProviderDao`

## Struts2 Migration Pattern ("2Action")

**CRITICAL PATTERN**: All new Struts2 actions use `*2Action.java` naming convention

### 2Action Structure Template:
```java
public class Example2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();
    
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public String execute() {
        // MANDATORY security check
        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_object", "r", null)) {
            throw new SecurityException("missing required sec object");
        }
        // Business logic
        return "success";
    }
}
```

### 2Action Categories:
1. **Simple Execute**: Single `execute()` method (e.g., `AddTickler2Action`)
2. **Method-Based**: Route via `method` parameter (e.g., `CaseloadContent2Action`) 
3. **Inheritance-Based**: Extend `EctDisplayAction` for encounter components

## Healthcare Domain Context

**Core Medical Modules**:
- **PMmodule/**: Program management and case management
- **billing/**: Province-specific billing (BC, ON) with diagnostic codes
- **prescription/**: Drug management with ATC codes, interaction checking
- **lab/**: HL7 lab results, OLIS integration (Ontario)
- **prevention/**: Immunization tracking with provincial schedules
- **demographic/**: Patient data with HIN (Health Insurance Number) management

## Development Workflow

**DevContainer Environment**:
- Docker-based development with debugging on port 8000
- AI tools available: Claude Code CLI, Aider, GitHub CLI, Docusaurus
- Custom terminal with tool reminders on bash startup

**Build & Deploy Cycle**:
1. `make clean` → `make install --run-tests` → `server log`
2. For quick iterations: `make install` (skips tests)
3. Debug logging: `debug-on` → `server restart` → `debug-off`

## Code Quality Standards

**Security (CodeQL Integration)**:
- OWASP Encoder for all JSP outputs
- Parameterized SQL queries (never concatenation)
- File upload filename validation
- CodeQL security scanning must pass

**Spring Integration Pattern**:
```java
private SomeManager someManager = SpringUtils.getBean(SomeManager.class);
```

**Documentation Standards**:
- **JavaDoc Required**: All public classes and methods MUST have comprehensive JavaDoc
- **No @author Tags**: Do NOT add @author annotations (misleading after Bitbucket→GitHub migration)
- **@since Tags**: Use git history to determine accurate dates: `git log --follow --format="%ai" <file> | tail -1`
- **Parameter Documentation**: Include specific data types in @param tags (e.g., `@param id String the unique identifier`)
- **Return Documentation**: Specify exact return types in @return tags (e.g., `@return List<Provider> list of healthcare providers`)
- **Exception Documentation**: Document all thrown exceptions with @throws tags
- **Deprecation**: Use @deprecated with migration guidance to newer APIs
- **JSP Documentation**: Add comprehensive JSP comment blocks after copyright headers with purpose, features, parameters, and @since
- **Inline Comments**: Add comments for complex logic on separate lines (not same line as code)

**Database Patterns**:
- All tables include `lastUpdateUser`, `lastUpdateDate` for audit trails
- Complex healthcare schema with 50+ fields in `demographic` table
- Multi-jurisdictional support (BC, ON, generic provinces)

## Healthcare Integration Standards

**Standards & Protocols**:
- **HL7 v2/v3**: Full message processing with MSH, PID, OBX, ORC, OBR segment handlers
- **FHIR R4**: HAPI FHIR 5.4.0 with resource filters and healthcare provider context
- **SNOMED CT**: Clinical terminology with core dataset loading
- **ICD-9/ICD-10**: Diagnosis coding systems fully integrated
- **ATC Codes**: Anatomical Therapeutic Chemical classification for medications
- **DICOM**: Medical imaging support for diagnostic images

**Provincial Healthcare Systems**:
- **OLIS**: Ontario Labs Information System integration
- **Teleplan**: BC MSP billing system with specialized upload/download
- **MCEDT**: Medical Certificate Electronic Data Transfer
- **DrugRef**: Drug reference database integration
- **MyOSCAR**: Patient portal integration

**Medical Forms Integration**:
- **Rourke Growth Charts**: Multiple versions (2006, 2009, 2017, 2020) for pediatric care
- **BCAR Forms**: British Columbia Antenatal Record for pregnancy care
- **Mental Health Assessments**: Standardized clinical assessment forms
- **Laboratory Requisitions**: Province-specific lab ordering forms

## Technology Stack Details

### Core Technologies
- **Java 21** with modern language features and JAXB compatibility
- **Spring Framework 5.3.39**: IoC container, MVC, AOP, Security, transaction management
- **Hibernate 5.6.15**: ORM framework with custom MySQL dialect (`OscarMySQL5Dialect`)
- **Maven 3**: Build management with 200+ healthcare-specific dependencies
- **Apache Tomcat 9.0.97**: Web application server with debugging enabled
- **MariaDB/MySQL**: Database with custom connection tracking (`OscarTrackingBasicDataSource`)

### Web Technologies  
- **Struts 2.5.33**: Modern actions (2Action pattern) coexisting with legacy Struts 1.x
- **Apache CXF 3.5.10**: Web services framework for healthcare integrations
- **JSP/JSTL**: View layer with extensive medical form templates
- **Bootstrap 5.3.0**: Modern UI framework loaded from CDN for responsive design
- **JavaScript/CSS/jQuery**: Frontend with healthcare-specific UI components
- **Vanilla JavaScript**: Progressively replacing jQuery dependencies where possible

### Security Libraries
- **OWASP CSRF Guard**: CSRF protection with healthcare exclusions
- **OWASP Encoder**: Output encoding for XSS prevention
- **BCrypt**: Password hashing for provider authentication
- **Bouncy Castle**: Cryptographic functions for PHI protection

### Spring Configuration Architecture
Multiple modular application contexts:
- `applicationContext.xml` - Core Spring configuration
- `applicationContextREST.xml` - REST APIs with OAuth 1.0a  
- `applicationContextOLIS.xml` - Ontario Labs Information System
- `applicationContextHRM.xml` - Hospital Report Manager
- `applicationContextCaisi.xml` - CAISI community integration
- `applicationContextFax.xml`, `applicationContextJobs.xml` - Specialized modules

## REST API & Web Services

### OAuth 1.0a Authentication (Migration in Progress)
- **Current Migration**: CXF OAuth2 → ScribeJava OAuth1.0a
- **New Classes**: `OscarOAuthDataProvider`, `OAuth1Executor`, `OAuth1Utils`
- **Healthcare Context**: Provider-specific credentials with facility integration
- **Services Migrated**: ProviderService, ConsentService with enhanced error handling

### Core API Services (25+ endpoints)
- **DemographicService**: Patient demographics with HIN management
- **ScheduleService**: Appointment scheduling with reason codes and billing types
- **PrescriptionService**: Medication management with ATC codes and interaction checking
- **LabService**: Laboratory results with HL7 integration and OLIS connectivity
- **PreventionService**: Immunization tracking with provincial schedules
- **ConsultationWebService**: Referral management and specialist communication
- **DocumentService**: Medical document management with privacy statement injection

### SOAP Web Services
- **CXF-based**: Healthcare system integration with WS-Security
- **Inter-EMR**: Data sharing via Integrator system across multiple OSCAR installations
- **Provincial Billing**: Direct integration with Teleplan (BC MSP) and other systems

## Recent Development Context (2025)

### Encounter Window Modernization (September 2025)
**COMPLETED**: Major refactoring of the encounter window interface
- **Bootstrap 5 Migration**: Upgraded to Bootstrap 5.3.0 from CDN for modern UI patterns
- **Vanilla JavaScript**: Replaced jQuery dependencies with vanilla JS where possible
- **CSRF Protection**: Fixed critical issues with dynamically loaded forms via AJAX
- **Note Layout Fix**: Corrected note entry positioning to remain at bottom of scrollable area
- **Window Sizing**: Implemented content-based auto-sizing instead of full-screen maximization

### Major Namespace Migration (August 2025)
**CRITICAL**: Completed migration `org.oscarehr.*` / `oscar.*` → `ca.openosp.openo.*`
- **DAO Classes**: `ca.openosp.openo.commn.dao.*` (note: "commn" not "common")
- **Models**: `ca.openosp.openo.commn.model.*`
- **Special Case**: `ProviderDao` at `ca.openosp.openo.dao.ProviderDao`
- **Test Utilities**: Remain at `org.oscarehr.common.dao.*` for backward compatibility

### Active Code Cleanup (2025)
- **Modules Removed**: MyDrugRef, BORN integration, HealthSafety, legacy email notifications
- **Philosophy**: Reduce attack surface by removing unused functionality
- **Security Focus**: CodeQL integration with mandatory security scanning

## Architecture Patterns

### Layered Architecture
- **Web Layer**: Controllers (Actions) handle HTTP requests
- **Service Layer**: Business logic and workflow orchestration
- **DAO Layer**: Data access objects for database operations
- **Model Layer**: Domain entities and value objects

### Spring Configuration
- Extensive use of Spring IoC container
- Transaction management with Spring AOP
- Security configuration with Spring Security
- Multiple application contexts for different modules

### Legacy Integration & Unique Struts2 Migration Pattern

#### **Migration Strategy Overview**
OpenO EMR uses a unique incremental migration approach from Struts 1.x to Struts 2.x using a "2Action" naming convention that allows both frameworks to coexist during the transition period.

#### **2Action Naming Convention & Structure**
- **Naming Pattern**: All migrated Struts2 actions follow `*2Action.java` naming (e.g., `AddTickler2Action`, `DisplayDashboard2Action`, `Login2Action`)
- **Class Structure**: 
  ```java
  public class Example2Action extends ActionSupport {
      HttpServletRequest request = ServletActionContext.getRequest();
      HttpServletResponse response = ServletActionContext.getResponse();
      
      private SomeManager someManager = SpringUtils.getBean(SomeManager.class);
      
      public String execute() {
          // Security check pattern
          if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_object", "r", null)) {
              throw new SecurityException("missing required sec object");
          }
          // Business logic
          return "success";
      }
  }
  ```

#### **Struts2 Action Categories**

**1. Simple Execute Actions**
- Single `execute()` method handling all logic
- Examples: `AddTickler2Action`, `EditTickler2Action`
- Return simple result strings like "success", "close", "error"

**2. Method-Based Actions**
- Use `method` parameter to route to different methods within the action
- Pattern: `String mtd = request.getParameter("method");`
- Examples: `CaseloadContent2Action` (noteSearch/search methods), `SystemMessage2Action`
- Allows multiple related operations in one action class

**3. Inheritance-Based Actions**
- Extend specialized base classes like `EctDisplayAction`
- Examples: `EctDisplayMeasurements2Action`, `EctDisplayRx2Action`
- Inherit common functionality while implementing specific `getInfo()` methods
- Used for encounter display components in left navbar

#### **Integration Patterns**

**Request/Response Access**
```java
HttpServletRequest request = ServletActionContext.getRequest();
HttpServletResponse response = ServletActionContext.getResponse();
```
- Direct servlet API access maintained for compatibility
- No dependency on Struts2 action properties

**Spring Integration**
```java
private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
```
- Spring dependency injection via `SpringUtils.getBean()`
- Maintains loose coupling with Spring container
- No need for Struts2-Spring plugin complexity

**Security Pattern (Required)**
```java
if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_objectname", "r", null)) {
    throw new SecurityException("missing required sec object");
}
```
- Every 2Action MUST include security validation
- Uses healthcare-specific role-based access control
- Throws SecurityException for unauthorized access

#### **Configuration Approach**

**Struts.xml Mapping**
```xml
<action name="login" class="ca.openosp.openo.login.Login2Action">
    <result name="provider" type="redirect">/provider/providercontrol.jsp</result>
    <result name="failure">/logout.jsp</result>
</action>
```
- Maintains `.do` extension for backward compatibility
- Spring object factory integration: `<constant name="struts.objectFactory" value="spring"/>`
- Mixed namespace support for gradual migration

**URL Compatibility**
- Legacy URLs ending in `.do` continue to work
- No changes required to existing JSP forms and links
- Seamless user experience during migration

#### **Migration Benefits**

**1. Risk Mitigation**
- Incremental migration reduces risk of breaking changes
- Both frameworks run simultaneously
- Easy rollback capability if issues arise

**2. Team Productivity** 
- Developers can work on both legacy and new actions
- No need to stop feature development during migration
- Clear naming convention prevents confusion

**3. Maintenance Efficiency**
- Clear identification of migrated vs. legacy actions
- Consistent patterns for new development
- Simplified testing and debugging

#### **Best Practices for 2Action Development**

**1. Security First**
- Always include security privilege checks
- Use appropriate security objects for healthcare data
- Log security violations appropriately

**2. Error Handling**
- Use OWASP encoding for user inputs: `Encode.forJava(parameter)`
- Implement proper exception handling
- Return appropriate result strings

**3. Spring Integration**
- Use `SpringUtils.getBean()` for dependency injection
- Leverage existing Spring-managed services
- Maintain transactional boundaries

**4. Healthcare Context**
- Include audit logging for patient data access
- Follow PHI protection patterns
- Use healthcare-specific validation

This migration pattern allows OpenO EMR to modernize incrementally while maintaining system stability and regulatory compliance throughout the transition process.

## File Patterns

### Key File Types
- `**/*DAO.java` - Database access objects
- `**/*Action.java` - Struts/Spring MVC controllers
- `**/web/**` - Web layer components
- `**/model/**` - Entity/domain models
- `**/service/**` - Business logic services
- `database/mysql/**` - Schema migrations and SQL scripts
- `src/main/webapp/**` - Web resources (JSP, CSS, JS)

### Configuration Files
- **Spring Contexts** (11+ modular configurations):
  - `applicationContext.xml` - Core Spring configuration
  - `applicationContextREST.xml` - REST APIs with OAuth 1.0a
  - `applicationContextOLIS.xml` - Ontario Labs Information System
  - `applicationContextHRM.xml` - Hospital Report Manager
  - `applicationContextCaisi.xml` - CAISI integration
  - `applicationContextFax.xml`, `applicationContextJobs.xml` - Specialized modules
- **Struts Configuration**:
  - `struts.xml` - Struts2 configuration with `.do` extension and Spring integration
  - Mixed Struts 1.x and 2.x action mappings
- **Database Configuration**:
  - Custom MySQL dialect: `OscarMySQL5Dialect`
  - Connection tracking: `OscarTrackingBasicDataSource`
  - Legacy MySQL compatibility settings
- **Security Configuration**:
  - `web.xml` - Complex filter chain with OWASP CSRF protection
  - Privacy statement filters and audit logging
  - Multi-factor authentication and SAML 2.0 support
- `pom.xml` - Maven with 200+ healthcare-specific dependencies

## Development Environment

### Docker Setup
- Development environment runs in Docker containers
- Tomcat container with Java 21 and debugging enabled
- MariaDB container for database
- Maven repository caching for faster builds
- Port 8080 for web application, 3306 for database

### IDE Configuration
- VS Code with Java extension pack
- Remote development in Docker container
- Maven integration for dependency management
- Debugging support on port 8000

## REST API Architecture

### OAuth 1.0a Authentication
- Healthcare provider context with facility-specific credentials
- Per-facility service endpoints with WS-Security for inter-facility communication
- Token-based authentication for patient portal integration

### Core API Services (25+ endpoints)
- **DemographicService**: Patient demographics with HIN (Health Insurance Number) management
- **ScheduleService**: Appointment scheduling with reason codes and billing types  
- **PrescriptionService**: Medication management with ATC codes and interaction checking
- **LabService**: Laboratory results with HL7 integration
- **PreventionService**: Immunization tracking with provincial schedules
- **ConsultationWebService**: Referral management and specialist communication
- **DocumentService**: Medical document management with privacy statement injection

### SOAP Web Services
- CXF-based services for healthcare system integration
- Inter-EMR data sharing via Integrator system
- Province-specific billing system integration (Teleplan for BC MSP)

## Database Schema Patterns

### Core Healthcare Tables
- **demographic**: 50+ fields including HIN, rostering status, multiple addresses
- **allergies**: Drug/non-drug allergies with severity, reaction tracking, regional identifiers  
- **appointment**: Scheduling with reason codes, billing types, status tracking
- **casemgmt_note**: Clinical notes with encryption support and issue-based organization
- **prevention**: Immunization/prevention tracking with configurable schedules
- **drugs**: Prescription management with ATC codes, interaction checking, renal dosing
- **measurementType**: Vital signs and clinical measurements with flowsheet integration
- **billing**: Province-specific billing with diagnostic codes and claims processing

### Audit and Compliance Patterns
- Every table includes `lastUpdateUser`, `lastUpdateDate` for audit trails
- Comprehensive logging of all patient data access via `UserActivityFilter`
- Privacy-compliant data handling with PHI filtering throughout application
- Multi-jurisdictional support with province-specific configurations

## Form and Document Management

### Medical Forms Library
- **Rourke Growth Charts**: Multiple versions (2006, 2009, 2017, 2020) for pediatric care
- **BCAR Forms**: British Columbia Antenatal Record for pregnancy care
- **Mental Health Assessments**: Standardized clinical assessment forms
- **Laboratory Requisitions**: Province-specific lab ordering forms
- **Immunization Forms**: Vaccination record management

### Document Processing
- **PDF Generation**: Custom servlets with medical template rendering
- **E-forms**: Electronic form management with digital signature support
- **Privacy Compliance**: Automatic privacy statement injection on all documents
- **Document Categories**: Configurable types with clinical workflow integration

## Recent Development Context (2025)

### Major Namespace Migration (August 2025)
- **CRITICAL**: Project completed major namespace migration `org.oscarehr.*` / `oscar.*` → `ca.openosp.openo.*`
- **When writing new code**: Always use `ca.openosp.openo.*` package structure
- **When referencing existing code**: May encounter both old and new package names in comments/documentation
- **Import statements**: Update all imports to use new namespace structure
- **Git history**: Many files show as "renamed" due to this migration

#### **Package Migration Details**
- **Primary Migration**: `org.oscarehr.common.*` → `ca.openosp.openo.commn.*` (note: intentionally "commn" not "common")
- **DAO Classes**: All DAO interfaces moved to `ca.openosp.openo.commn.dao.*`
- **Model Classes**: Entity models moved to `ca.openosp.openo.commn.model.*`
- **Special Cases**: 
  - `ProviderDao` specifically located at `ca.openosp.openo.dao.ProviderDao` (not in commn.dao)
  - Forms DAOs at `ca.openosp.openo.commn.dao.forms.*`
- **Test Utilities**: Test framework classes remain at `org.oscarehr.common.dao.*` (e.g., `EntityDataGenerator`, `SchemaUtils`)

#### **Import Management Patterns**
When fixing compilation errors after package refactoring:
- **Main Source Code**: Use systematic find/replace operations for bulk import updates
- **Test Files**: Manually add missing DAO imports following the pattern:
  ```java
  import ca.openosp.openo.commn.model.EntityName;
  import ca.openosp.openo.commn.dao.EntityNameDao;
  import ca.openosp.openo.utility.SpringUtils;
  ```
- **Batch Processing**: Use MultiEdit tool for efficient bulk import fixes across multiple files
- **Verification**: Always read files before applying edits to understand the context

### Mandatory Security Practices (CodeQL Integration)
- **OWASP Encoder**: Use `Encode.forHtml()`, `Encode.forJavaScript()` for all user inputs in JSPs
- **SQL Injection Prevention**: Use parameterized queries, never string concatenation
- **File Upload Security**: Implement filename validation for all uploads
- **XSS Prevention**: All JSP outputs must be encoded
- **CodeQL Compliance**: Code must pass GitHub CodeQL security scanning

### OAuth Implementation (Migration in Progress)
- **Current migration**: Moving from CXF OAuth2 to ScribeJava OAuth1.0a
- **New classes**: Use `OscarOAuthDataProvider`, `OAuth1Executor`, `OAuth1Utils`
- **Services affected**: ProviderService, ConsentService migrated to new OAuth
- **Pattern**: Enhanced error handling and logging for OAuth flows

### Code Maintenance Approach
- **Active cleanup**: Project aggressively removes unused code and dependencies
- **Recently removed**: MyDrugRef, BORN integration, HealthSafety, legacy email notifications
- **Assumption**: Don't assume legacy features still exist - check current codebase
- **Philosophy**: Reduce attack surface by removing unused functionality

### Development Environment Context
- **DevContainer primary**: Development done in Docker containers with debugging enabled
- **Debug port**: 8000 for remote debugging
- **Database**: Hibernate schema validation disabled - manual migration control
- **Logging**: Enhanced logging in development environment with `debug-on`/`debug-off` aliases
- **AI Tools Available**: Claude Code CLI, Aider AI pair programming, GitHub CLI, Docusaurus
- **Custom Terminal**: Welcome message displays all available tools and shortcuts on bash startup

### DevContainer Custom Scripts
Located in `/scripts` directory within the container (copied from `.devcontainer/development/scripts/`):

#### **Build and Deploy Script (`make`)**
```bash
make clean                    # Clean project and remove deployed app
make install                  # Build and deploy without tests
make install --run-tests      # Build, test, and deploy
make lock                     # Update Maven dependency lock file
make help                     # Show usage instructions
```
- **Process**: Stops Tomcat → Builds WAR → Creates symlink → Starts Tomcat  
- **Configuration**: Auto-creates `over_ride_config.properties` from template
- **Parallel builds**: Uses `-T 1C` for faster Maven builds
- **Deployment**: Handles versioned WAR directories with symlinks to `/usr/local/tomcat/webapps/oscar`

#### **Tomcat Management Script (`server`)**
```bash
server start                  # Start Tomcat and save PID
server stop                   # Stop Tomcat and kill process  
server restart                # Stop and start Tomcat
server log                    # Tail Tomcat logs (catalina.out)
```
- **PID Management**: Saves Tomcat process ID to `/tomcat.pid` for reliable stop/restart
- **Process Control**: Uses both `catalina.sh` and direct PID kill for clean shutdowns
- **Log Access**: Direct access to Tomcat logs via `server log` command

### Development Workflow with Custom Scripts
```bash
# Full development cycle
make clean                    # Clean previous build
make install --run-tests      # Build with tests and deploy
server log                    # Monitor application startup

# Quick rebuild without tests  
make install                  # Fast rebuild and deploy
server restart                # Restart if needed

# Debug logging control
debug-on                      # Enable DEBUG level logging
server restart                # Restart to apply logging changes
debug-off                     # Return to INFO level logging
```

## Database Schema & Migration System

**Database**: MariaDB/MySQL with comprehensive healthcare schema dating back to 2006
**Migration Pattern**: Date-based SQL scripts (`update-YYYY-MM-DD-description.sql`)

### Core Database Files (`database/mysql/`)
```bash
# Initial Schema Setup
oscarinit.sql          # Core database schema
oscarinit_2025.sql     # Current 2025 schema version  
oscardata.sql          # Initial reference data
oscarinit_bc.sql       # British Columbia specific
oscarinit_on.sql       # Ontario specific

# Medical Coding Systems
icd9.sql / icd10.sql   # Diagnosis codes (ICD-9/ICD-10)
measurementMapData.sql # Clinical measurements mapping
SnomedCore/           # SNOMED CT clinical terminology
olis/                 # Ontario Labs Information System

# Provincial Healthcare Data
bc_billingServiceCodes.sql     # BC medical service codes
bc_pharmacies.sql              # BC pharmacy directory
firstNationCommunities_lu_list.sql # First Nations communities
```

### Recent Database Changes (2025)
- `update-2025-08-26-remove-waitlist-email.sql` - Email functionality removal
- `update-2025-08-25-remove-healthsafety.sql` - HealthSafety module cleanup
- `update-2025-08-21-k2a-removal.sql` - K2A integration removal
- `update-2025-08-14-genericintake-removal.sql` - Generic intake forms cleanup

### Key Healthcare Tables Schema
```sql
demographic: 50+ fields (HIN, rostering_status, addresses, demographics)
allergies: severity, reaction, regional_identifier, drug_allergies  
appointment: reason_code, billing_type, status, provider_no
casemgmt_note: encrypted clinical notes, issue-based organization
prevention: immunization_type, prevention_date, provider_prevention_type
drugs: ATC_code, generic_name, dosage, interaction_checking
measurementType: vital signs, clinical measurements, flowsheet_integration
billing: diagnostic_codes, provincial_billing_integration
```

**Development Database**: 
- Container: `db-connect` alias → MariaDB as root user
- Port 3306 with health checks, 2G memory limit
- Seeded with medical forms (Rourke charts, BCAR) and reference data

---

## Commit Standards & Quick Reference

**Commit Format**: [Conventional Commits](https://www.conventionalcommits.org/) - `feat:`, `fix:`, `chore:`, `update:`

**Key Files**:
- `CLAUDE.md` - AI context (this file)
- `pom.xml` - 200+ healthcare Maven dependencies
- `database/mysql/` - 19+ years of healthcare schema evolution (2006-2025)  
- `.devcontainer/` - Docker development with AI tools

**Critical Patterns**:
- **Project Name**: "OpenO EMR" (NOT "OSCAR EMR")
- **Security**: `SecurityInfoManager.hasPrivilege()` + OWASP encoding required
- **Actions**: `*2Action.java` pattern for Struts2 migration
- **Packages**: `ca.openosp.openo.*` (new) vs `org.oscarehr.*` (legacy)
- **Database**: Date-based migrations, audit trails (`lastUpdateUser`, `lastUpdateDate`)

---

## Key Code References & Further Information

### Essential Configuration Files
```bash
# Spring Configuration Examples
src/main/resources/applicationContext.xml           # Core Spring setup patterns
src/main/resources/applicationContextREST.xml      # OAuth 1.0a implementation examples
src/main/webapp/WEB-INF/web.xml                   # Security filter chain configuration

# Struts Configuration
src/main/webapp/WEB-INF/classes/struts.xml        # 2Action mapping examples
src/main/java/ca/openosp/openo/*/web/*2Action.java # 2Action implementation patterns

# Database Configuration
src/main/resources/OscarDatabaseBase.xml           # Hibernate configuration
database/mysql/oscarinit_2025.sql                 # Current database schema
database/mysql/updates/update-2025-*.sql          # Recent migration patterns
```

### Security Implementation Examples
```bash
# Security Patterns (READ THESE FIRST)
src/main/java/ca/openosp/openo/managers/SecurityInfoManager.java    # Authorization patterns
src/main/java/ca/openosp/openo/utility/LoggedInInfo.java           # Session management
src/main/webapp/WEB-INF/classes/oscar/oscarSecurity/                # Security filter examples

# OWASP Integration Examples
src/main/webapp/*/*.jsp                            # Look for Encode.forHtml() usage patterns
src/main/java/ca/openosp/openo/*/web/*2Action.java # Security check implementations

# CSRF Protection Implementation
src/main/webapp/WEB-INF/Owasp.CsrfGuard.properties # CSRF Guard configuration
src/main/webapp/WEB-INF/csrfguard.js               # Client-side token injection
src/main/java/ca/openosp/openo/app/CSRFPreservingFilter.java    # Custom CSRF filter
src/main/java/ca/openosp/openo/app/CsrfJavaScriptInjectionFilter.java # JS injection
docs/csrf-protection.md                            # Complete CSRF documentation
```

### Healthcare Domain Examples
```bash
# Medical Data Patterns
src/main/java/ca/openosp/openo/commn/model/Demographic.java        # Patient data model
src/main/java/ca/openosp/openo/commn/model/Allergies.java          # Medical allergy model
src/main/java/ca/openosp/openo/commn/dao/DemographicDao.java       # Healthcare DAO patterns

# Provincial Healthcare Integration
src/main/java/ca/openosp/openo/billing/CA/BC/                      # BC-specific billing
src/main/java/ca/openosp/openo/billing/CA/ON/                      # Ontario-specific billing
src/main/java/ca/openosp/openo/olis/                               # Ontario Labs integration

# HL7 & FHIR Examples
src/main/java/ca/openosp/openo/hl7/                                # HL7 message handling
src/main/java/ca/openosp/openo/fhir/                               # FHIR implementation patterns
```

### 2Action Migration Examples
```bash
# Study These 2Action Implementations
src/main/java/ca/openosp/openo/tickler/pageUtil/AddTickler2Action.java      # Simple execute pattern
src/main/java/ca/openosp/openo/caseload/CaseloadContent2Action.java         # Method-based routing
src/main/java/ca/openosp/openo/encounter/pageUtil/EctDisplay*2Action.java   # Inheritance pattern

# Base Classes for 2Actions
src/main/java/ca/openosp/openo/encounter/pageUtil/EctDisplayAction.java     # Base class example
```

### Spring Integration Patterns
```bash
# Spring Utility Usage Examples
src/main/java/ca/openosp/openo/utility/SpringUtils.java           # Spring bean access patterns
src/main/java/ca/openosp/openo/managers/*Manager.java             # Service layer examples
src/main/java/ca/openosp/openo/commn/dao/*Dao.java               # DAO injection patterns
```

### Database Schema References
```bash
# Database Structure Examples
database/mysql/oscardata.sql                      # Reference data examples
database/mysql/caisi/initcaisi.sql               # Community integration schema
database/mysql/olis/olisinit.sql                 # Provincial lab integration schema
database/mysql/SnomedCore/snomedinit.sql         # Medical terminology integration
```

### Testing Patterns
```bash
# Test Implementation Examples (for understanding patterns)
src/test/java/ca/openosp/openo/                   # Test structure examples
src/test/resources/over_ride_config.properties    # Test configuration template
```

### Development Environment References
```bash
# DevContainer Configuration
.devcontainer/devcontainer.json                   # VS Code dev environment setup
.devcontainer/development/Dockerfile              # Container build configuration
.devcontainer/development/scripts/make            # Build and deploy automation
.devcontainer/development/scripts/server          # Tomcat management automation
.devcontainer/development/config/bashrc           # Terminal customization
```

### Documentation & Architecture
```bash
# Project Documentation
docs/csrf-protection.md                           # CSRF protection implementation & troubleshooting
docs/encounter-window-architecture.md             # Encounter window & AJAX patterns
docs/Password_System.md                           # Security architecture details
docs/struts-actions-detailed.md                   # Action mapping documentation
pom.xml                                            # Complete dependency list with versions
README.md                                          # Project setup and overview
```

### When You Need Help Understanding:
- **Security Patterns**: Check `SecurityInfoManager.java` and existing 2Action implementations
- **Database Access**: Look at DAO implementations in `commn.dao` package  
- **Healthcare Standards**: Examine `hl7/` and `fhir/` packages for integration patterns
- **Provincial Variations**: Study `billing/CA/BC/` vs `billing/CA/ON/` implementations
- **Spring Configuration**: Reference the multiple `applicationContext*.xml` files
- **2Action Migration**: Compare legacy Action classes with their 2Action equivalents
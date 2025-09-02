# Legacy HAPI Library Dynamic Loading

## Overview
OpenO contains legacy HAPI 0.5.1 libraries that are dynamically loaded at runtime from the resources directory. This unusual architecture exists to support parsing of MDS and IHA lab formats that have not been migrated to current HAPI versions.

## Architecture

### Files Involved
- `/src/main/resources/hapi_libs/fork/hacked_patched_hapi-0.5.1.jar` - Modified HAPI 0.5.1
- `/src/main/resources/hapi_libs/fork/commons-logging-1.1.1.jar` - Required dependency
- `/src/main/resources/hapi_libs/fork/readme.txt` - Original documentation
- `/src/main/java/org/oscarehr/common/hl7/v2/oscar_to_oscar/DynamicHapiLoaderUtils.java` - Dynamic loader

### Why JARs Are in Resources
Unlike normal library dependencies managed by Maven, these JARs are placed in `/src/main/resources/` because:
1. They need to be available on the classpath at runtime
2. They conflict with newer HAPI versions (1.0.1 and HAPI FHIR 5.4.0) used elsewhere
3. They contain custom patches/hacks specific to MDS lab parsing
4. They must be isolated from the main classloader to avoid conflicts

### Dynamic Loading Mechanism
```java
// DynamicHapiLoaderUtils.java creates an isolated classloader
URLClassLoader classLoader = new URLClassLoader(urls, null);

// Loads JARs from classpath resources
urls[0] = DynamicHapiLoaderUtils.class.getResource("/hapi_libs/fork/hacked_patched_hapi-0.5.1.jar");
urls[1] = DynamicHapiLoaderUtils.class.getResource("/hapi_libs/fork/commons-logging-1.1.1.jar");
```

The `null` parent classloader ensures complete isolation from the main application's HAPI libraries.

## Current Usage

### Active Consumers
1. **MDSHandler.java** (`oscar.oscarLab.ca.all.parsers`)
   - 50+ method calls to DynamicHapiLoaderUtils
   - Parses MDS (Medical Data Systems) lab format
   - Configured in `message_config.xml` as handler "MDS"

2. **IHAHandler.java** (`oscar.oscarLab.ca.all.parsers`)
   - Uses DynamicHapiLoaderUtils for IHA POI lab parsing
   - Configured in `message_config.xml` as handlers "IHA" and "IHAPOI"

3. **IHAHandler.java** (`oscar.oscarLab.ca.all.upload.handlers`)
   - Upload handler variant for IHA labs

### Reflection-Based API
Since the legacy HAPI classes can't be directly imported (would conflict), all interaction happens through reflection:
```java
// Example: Parsing an HL7 message
Object msg = DynamicHapiLoaderUtils.parseMdsMsg(hl7Body);
Object terser = DynamicHapiLoaderUtils.getMdsTerser(msg);
String value = DynamicHapiLoaderUtils.terserGet(terser, "/.PID-5-1");
```

## Version Conflicts

### Current HAPI Versions in OpenO
- **HAPI 1.0.1** - Main HL7 v2 parsing library
- **HAPI FHIR 5.4.0** - FHIR support
- **HAPI 0.5.1 (legacy)** - MDS/IHA lab parsing only

### Why Not Upgrade?
The legacy HAPI contains custom modifications ("hacked_patched") specific to MDS lab parsing that would need to be:
1. Identified and documented
2. Reimplemented using HAPI 1.0.1 API
3. Thoroughly tested with real MDS/IHA lab data
4. Validated in production environments

## Migration Path

### Prerequisites for Removal
1. Rewrite MDSHandler to use HAPI 1.0.1
2. Rewrite IHAHandler to use HAPI 1.0.1  
3. Identify and port any custom patches from the "hacked" version
4. Comprehensive testing with actual MDS/IHA lab messages
5. Remove DynamicHapiLoaderUtils entirely

### Risks
- MDS and IHA lab uploads would break if removed prematurely
- Custom patches in the legacy version may handle edge cases not covered by standard HAPI
- Limited documentation on what the "hacked/patched" modifications actually do

## Technical Debt Impact

### Problems
1. **Security**: Old libraries may have unpatched vulnerabilities
2. **Maintenance**: Reflection-based code is harder to understand and debug
3. **Testing**: Can't use modern testing frameworks with dynamically loaded classes
4. **IDE Support**: No autocomplete or type checking for reflected calls
5. **Build Complexity**: JARs in resources is non-standard

### Benefits of Current Approach
1. Allows incompatible HAPI versions to coexist
2. Preserves working lab upload functionality
3. Isolates legacy code from main application

## Recommendations

### Short Term
- Document the specific "hacks/patches" in the legacy HAPI
- Add logging to track MDS/IHA lab upload usage
- Create test suite for MDS/IHA message parsing

### Long Term  
- Prioritize migration to current HAPI version
- Consider if MDS/IHA labs are still actively used
- Evaluate dropping support if usage is minimal

## Related Files
- `/src/main/resources/oscar/oscarLab/ca/all/upload/message_config.xml` - Handler configuration
- `/src/main/java/oscar/oscarLab/ca/all/parsers/MDSHandler.java` - MDS parser
- `/src/main/java/oscar/oscarLab/ca/all/parsers/IHAHandler.java` - IHA parser

## History
Based on code comments and structure, this dynamic loading approach was implemented when OpenO needed to support both old (0.5.1) and new (1.0+) HAPI versions simultaneously. The MDS lab format parser was never updated, creating this permanent technical debt.
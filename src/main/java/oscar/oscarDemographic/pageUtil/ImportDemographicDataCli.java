/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package oscar.oscarDemographic.pageUtil;

import oscar.OscarProperties;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Command-line runner for ImportDemographicDataAction4.
 *
 * Requires that oscar.properties is on the classpath (sets DOCUMENT_DIR and DB connection info).
 *
 * Usage:
 *   java oscar.oscarDemographic.pageUtil.ImportDemographicDataCli \
 *     --input <path> --provider <providerNo> [options]
 *
 * Options:
 *   --input <path>        XML file, ZIP file, or directory of patient XML files (required)
 *   --provider <no>       Provider number performing the import (required)
 *   --program <id>        Program ID to associate with imported patients (default: 0)
 *   --timeshift <days>    Shift appointment/note dates by N days (default: 0)
 *   --no-match-providers  Disable provider name matching (default: matching enabled)
 *   --help                Print this help and exit
 *
 * Exit codes:
 *   0  import completed (warnings may still have been recorded)
 *   1  bad or missing arguments
 *   2  configuration problem (oscar.properties missing, context failed to start)
 *   3  provider unknown, inactive, or not permitted to import
 *   4  the import itself failed
 *
 * Progress and diagnostics go through log4j2. log4j2.xml defaults the root level to
 * ${env:LOG_VERBOSITY:-error}, so this class raises its own logger to INFO and quiets the
 * java.util.logging stack that Spring's bootstrap chatter arrives on. Set LOG_VERBOSITY to
 * hand logging config back to the shared setup and see everything both stacks emit.
 *
 * Import warnings name patients, so they are not printed. They are written to
 * <importlog-basename>.warnings.log beside the import log; the console reports the count
 * and the path.
 */
public class ImportDemographicDataCli {

    private static final Logger logger = MiscUtils.getLogger();

    /** Provider.status value that marks an account as active; see ProviderDaoImpl. */
    private static final String PROVIDER_STATUS_ACTIVE = "1";

    /** Security object the import writes through; matches DemographicManagerImpl.addDemographic. */
    private static final String DEMOGRAPHIC_SECURITY_OBJECT = "_demographic";

    /** The log.ip column is narrow, so the audit tag is truncated to fit rather than dropped. */
    private static final int AUDIT_TAG_MAX_LENGTH = 50;

    // Distinct codes so a wrapper script can tell "denied" from "misconfigured" from "import failed".
    private static final int EXIT_OK = 0;
    private static final int EXIT_USAGE = 1;
    private static final int EXIT_CONFIG = 2;
    private static final int EXIT_DENIED = 3;
    private static final int EXIT_FAILED = 4;

    public static void main(String[] args) {
        configureLogging();
        System.exit(run(args));
    }

    /**
     * All the work, returning an exit code rather than calling System.exit, so there is a single
     * exit point and the Spring context is closed on every path.
     */
    private static int run(String[] args) {
        String inputPathStr = null;
        String providerNo = null;
        String programId = "0";
        int timeshiftInDays = 0;
        boolean matchProviderNames = true;

        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--input":
                        inputPathStr = requireValue(args, ++i, "--input");
                        break;
                    case "--provider":
                        providerNo = requireProviderNo(requireValue(args, ++i, "--provider"));
                        break;
                    case "--program":
                        programId = requireInteger(requireValue(args, ++i, "--program"), "--program");
                        break;
                    case "--timeshift":
                        timeshiftInDays = Integer.parseInt(
                                requireInteger(requireValue(args, ++i, "--timeshift"), "--timeshift"));
                        break;
                    case "--no-match-providers":
                        matchProviderNames = false;
                        break;
                    case "--help":
                        printUsage();
                        return EXIT_OK;
                    default:
                        throw new IllegalArgumentException("Unknown argument: " + args[i]);
                }
            }
        } catch (IllegalArgumentException e) {
            // A missing option value or a malformed number is an operator error, not a crash:
            // report the problem and the usage rather than an argument-index stack trace.
            logger.error(e.getMessage());
            printUsage();
            return EXIT_USAGE;
        }

        if (inputPathStr == null || providerNo == null) {
            logger.error("--input and --provider are required.");
            printUsage();
            return EXIT_USAGE;
        }

        Path inputPath;
        try {
            // toRealPath resolves symlinks and fails if the path does not exist, so the path that
            // gets imported is the same one that was checked - no TOCTOU gap through a swapped link.
            inputPath = Paths.get(inputPathStr).toAbsolutePath().normalize().toRealPath();
        } catch (IOException e) {
            logger.error("Input path cannot be resolved: {}", inputPathStr, e);
            return EXIT_USAGE;
        }
        if (!Files.isReadable(inputPath)) {
            logger.error("Input path is not readable: {}", inputPath);
            return EXIT_USAGE;
        }

        // OscarProperties singleton loads oscar_mcmaster.properties (dev defaults) at class-init time.
        // Override with oscar.properties from the classpath so the real DB credentials are used.
        if (!loadOscarProperties()) {
            return EXIT_CONFIG;
        }

        // Bootstrap Spring — oscar.properties must be on the classpath.
        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext()) {
            ctx.getEnvironment().setActiveProfiles("cli");
            ctx.setConfigLocation("classpath:applicationContext.xml");
            ctx.refresh();
            SpringUtils.setBeanFactory(ctx);

            // Load the provider performing the import.
            ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
            Provider provider = providerDao.getProvider(providerNo);
            if (provider == null) {
                logger.error("Provider not found: {}", providerNo);
                return EXIT_DENIED;
            }

            LoggedInInfo loggedInInfo = new LoggedInInfo();
            loggedInInfo.setLoggedInProvider(provider);
            loggedInInfo.setInitiatingCode(ImportDemographicDataCli.class.getName());
            // LogAction copies this into every audit row the import writes. Without it the CLI's
            // writes are indistinguishable from the provider's own web-session activity.
            loggedInInfo.setIp(auditTag());

            if (!isAuthorized(loggedInInfo, provider)) {
                return EXIT_DENIED;
            }

            logger.info("Importing {} (provider={} program={} timeshift={} matchProviders={})",
                    inputPath, providerNo, programId, timeshiftInDays, matchProviderNames);

            ImportDemographicDataAction4 action = new ImportDemographicDataAction4();
            ImportDemographicDataAction4.ImportResult result;
            try {
                result = action.importFromPath(
                        loggedInInfo, inputPath, providerNo, programId, matchProviderNames, timeshiftInDays);
            } catch (Exception e) {
                logger.error("Import failed for input path: {}", inputPath, e);
                return EXIT_FAILED;
            }

            reportResult(result);
            return EXIT_OK;
        } catch (Exception e) {
            // The import itself is caught above, so anything reaching here is a startup or
            // bean-wiring failure — oscar.properties pointing somewhere unreachable, usually.
            logger.error("Could not start the application context", e);
            return EXIT_CONFIG;
        }
    }

    /**
     * Import warnings quote patient names and dates of birth, so they are not written to the
     * console, where they would land in terminal scrollback, CI job output, or a redirected
     * nohup file. The console gets counts and file paths; the text goes to a file beside the
     * import log, which is already the PHI-bearing artefact of a run.
     */
    private static void reportResult(ImportDemographicDataAction4.ImportResult result) {
        if (result == null) {
            logger.warn("Import returned no result.");
            return;
        }

        logger.info("Import log written to: {}", result.getImportLogPath());

        List<String> warnings = result.getWarnings();
        if (warnings.isEmpty()) {
            logger.info("Done. 0 warning(s).");
            return;
        }

        try {
            Path warningsPath = writeWarningsFile(result.getImportLogPath(), warnings);
            logger.info("Done. {} warning(s) written to: {}", warnings.size(), warningsPath);
        } catch (IOException e) {
            // Falling back to the console would print the PHI this method exists to contain,
            // so report the failure and the count only.
            logger.error("Done. {} warning(s), but the warnings file could not be written.",
                    warnings.size(), e);
        }
    }

    /**
     * Writes the warnings next to the import log as <importlog-basename>.warnings.log, readable
     * only by the invoking user where the filesystem supports POSIX permissions.
     */
    private static Path writeWarningsFile(String importLogPath, List<String> warnings) throws IOException {
        Path logPath = Paths.get(importLogPath).toAbsolutePath();
        String logName = logPath.getFileName().toString();
        int dot = logName.lastIndexOf('.');
        String baseName = dot > 0 ? logName.substring(0, dot) : logName;
        Path warningsPath = logPath.resolveSibling(baseName + ".warnings.log");

        // Create the file with restrictive permissions first: setting them after the write would
        // leave a window where the content is on disk under the default umask.
        Set<PosixFilePermission> ownerOnly =
                EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE);
        try {
            Files.createFile(warningsPath, PosixFilePermissions.asFileAttribute(ownerOnly));
        } catch (FileAlreadyExistsException e) {
            // Same-second rerun; the write below truncates it.
        } catch (UnsupportedOperationException e) {
            // Non-POSIX filesystem (Windows): the file inherits the directory ACL instead.
            logger.debug("POSIX permissions unsupported; warnings file inherits directory ACL.");
        }

        try (BufferedWriter out = Files.newBufferedWriter(warningsPath, StandardCharsets.UTF_8)) {
            for (String warning : warnings) {
                out.write(warning);
                out.newLine();
            }
        }
        return warningsPath;
    }

    /**
     * Fails closed before any patient data is touched.
     *
     * The web import runs inside an authenticated session and DemographicManagerImpl re-checks
     * _demographic write on every save. The CLI has no session, so without this check the only
     * gate is that same per-save check — which throws mid-run, after earlier patients have
     * already been written. Checking up front means an unauthorised run writes nothing, and a
     * terminated provider's number cannot be used to import at all.
     */
    private static boolean isAuthorized(LoggedInInfo loggedInInfo, Provider provider) {
        if (!PROVIDER_STATUS_ACTIVE.equals(provider.getStatus())) {
            logger.error("Provider {} is not active (status={}); refusing to import.",
                    provider.getProviderNo(), provider.getStatus());
            return false;
        }

        SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
        // The (String) null selects the overload that checks the object privilege itself rather
        // than a per-patient one — see the contract on SecurityInfoManager.hasPrivilege.
        if (!securityInfoManager.hasPrivilege(
                loggedInInfo, DEMOGRAPHIC_SECURITY_OBJECT, SecurityInfoManager.WRITE, (String) null)) {
            logger.error("Provider {} does not have write privilege on {}; refusing to import.",
                    provider.getProviderNo(), DEMOGRAPHIC_SECURITY_OBJECT);
            return false;
        }
        return true;
    }

    /** Identifies the CLI run in the audit log, in place of the remote address a web request has. */
    private static String auditTag() {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            host = "unknown-host";
        }
        String tag = "cli/" + System.getProperty("user.name", "unknown-user") + "@" + host;
        return tag.length() > AUDIT_TAG_MAX_LENGTH ? tag.substring(0, AUDIT_TAG_MAX_LENGTH) : tag;
    }

    private static String requireValue(String[] args, int index, String option) {
        if (index >= args.length) {
            throw new IllegalArgumentException(option + " requires a value.");
        }
        return args[index];
    }

    private static String requireInteger(String value, String option) {
        try {
            Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(option + " must be an integer, got: " + value);
        }
        return value.trim();
    }

    /**
     * Provider numbers are looked up through JPA, so this is not an injection guard — it rejects
     * values that could only be a mistyped command line (blank, padded, or containing whitespace
     * or control characters) before they are used as an audit-log identity.
     */
    private static String requireProviderNo(String value) {
        String providerNo = value.trim();
        if (providerNo.isEmpty()) {
            throw new IllegalArgumentException("--provider must not be blank.");
        }
        for (int i = 0; i < providerNo.length(); i++) {
            char c = providerNo.charAt(i);
            if (Character.isWhitespace(c) || Character.isISOControl(c)) {
                throw new IllegalArgumentException("--provider must not contain whitespace or control characters.");
            }
        }
        return providerNo;
    }

    /**
     * Two unrelated logging stacks are in play, so both need a nudge:
     *
     * 1. This class logs through log4j2, but the shared log4j2.xml pins the root level to
     *    ${env:LOG_VERBOSITY:-error}, which would drop our progress output. Raise our own
     *    logger to INFO.
     * 2. Spring/CXF/Hibernate log through commons-logging, which lands in java.util.logging
     *    because slf4j-jdk14 is the SLF4J binding on this classpath. JUL's default root level
     *    is INFO, which is where the "Loading XML bean definitions from ..." bootstrap chatter
     *    comes from. Lift the JUL root to WARNING so warnings and errors still surface.
     *
     * Both are skipped when LOG_VERBOSITY is set, so an operator asking for a specific
     * verbosity gets it — including the third-party bootstrap detail.
     */
    private static void configureLogging() {
        if (System.getenv("LOG_VERBOSITY") != null) {
            return;
        }
        Configurator.setLevel(logger.getName(), Level.INFO);
        quietJulBootstrapNoise();
    }

    private static void quietJulBootstrapNoise() {
        java.util.logging.Logger julRoot = java.util.logging.LogManager.getLogManager().getLogger("");
        if (julRoot == null) {
            return;
        }
        julRoot.setLevel(java.util.logging.Level.WARNING);
        // Handlers filter independently of the logger, so raise them too — otherwise a handler
        // left at a finer level would still emit anything a child logger chose to publish.
        for (java.util.logging.Handler handler : julRoot.getHandlers()) {
            handler.setLevel(java.util.logging.Level.WARNING);
        }
    }

    /**
     * @return true if the real configuration was loaded and the import may proceed.
     *
     * Fails closed. Continuing without oscar.properties leaves the singleton on the
     * oscar_mcmaster.properties dev defaults, which point at a different database and
     * DOCUMENT_DIR — so an import that was meant for production would either fail late or,
     * worse, write patient data into whichever database those defaults happen to reach.
     */
    private static boolean loadOscarProperties() {
        // The -Doscar_override_properties system property is the standard override mechanism.
        // If the user set it explicitly, OscarProperties already handles it — nothing to do.
        if (System.getProperty("oscar_override_properties") != null) {
            return true;
        }
        // Otherwise, look for oscar.properties on the classpath (e.g. from the config dir the
        // user added to -cp) and merge it into the singleton so Spring gets the right DB URL.
        URL url = findProperties("oscar.properties");
        if (url == null) {
            // Keep compatibility with the build-produced name used by older launch scripts.
            url = findProperties("oscar-0-SNAPSHOT.properties");
        }
        if (url == null) {
            logger.error("oscar.properties not found on classpath and -Doscar_override_properties not set. "
                    + "Refusing to run against the dev defaults (oscar_mcmaster.properties). "
                    + "Fix: add the directory containing oscar.properties to -cp, or pass "
                    + "-Doscar_override_properties=/path/to/oscar.properties");
            return false;
        }
        try (InputStream is = url.openStream()) {
            OscarProperties.getInstance().load(is);
            return true;
        } catch (IOException e) {
            logger.error("Could not load {}", url, e);
            return false;
        }
    }

    private static URL findProperties(String fileName) {
        return ImportDemographicDataCli.class.getResource("/" + fileName);
    }

    // Help text stays on stdout: it is the program's interface, not a log record, and the
    // console layout (timestamp + level + class + file:line) would prefix every line of it.
    private static void printUsage() {
        System.out.println("Usage: ImportDemographicDataCli [options]");
        System.out.println();
        System.out.println("Required:");
        System.out.println("  --input <path>        XML file, ZIP file, or directory of patient XML files");
        System.out.println("  --provider <no>       Provider number performing the import");
        System.out.println();
        System.out.println("Optional:");
        System.out.println("  --program <id>        Program ID to associate (default: 0)");
        System.out.println("  --timeshift <days>    Shift dates by N days (default: 0)");
        System.out.println("  --no-match-providers  Disable provider name matching");
        System.out.println("  --help                Show this help and exit");
        System.out.println();
        System.out.println("oscar.properties (with DOCUMENT_DIR and DB config) must be on the classpath,");
        System.out.println("or pass -Doscar_override_properties=/path/to/oscar.properties.");
        System.out.println("The provider must be active and hold write privilege on _demographic.");
        System.out.println();
        System.out.println("Exit codes: 0 ok, 1 usage, 2 config, 3 not permitted, 4 import failed.");
        System.out.println("Warnings name patients and are written to a file beside the import log,");
        System.out.println("not to the console.");
    }
}

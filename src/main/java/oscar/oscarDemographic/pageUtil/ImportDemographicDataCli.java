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
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
 */
public class ImportDemographicDataCli {

    public static void main(String[] args) throws Exception {
        String inputPathStr = null;
        String providerNo = null;
        String programId = "0";
        int timeshiftInDays = 0;
        boolean matchProviderNames = true;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--input":
                    inputPathStr = args[++i];
                    break;
                case "--provider":
                    providerNo = args[++i];
                    break;
                case "--program":
                    programId = args[++i];
                    break;
                case "--timeshift":
                    timeshiftInDays = Integer.parseInt(args[++i]);
                    break;
                case "--no-match-providers":
                    matchProviderNames = false;
                    break;
                case "--help":
                    printUsage();
                    return;
                default:
                    System.err.println("Unknown argument: " + args[i]);
                    printUsage();
                    System.exit(1);
            }
        }

        if (inputPathStr == null || providerNo == null) {
            System.err.println("Error: --input and --provider are required.");
            printUsage();
            System.exit(1);
        }

        Path inputPath = Paths.get(inputPathStr).toAbsolutePath();
        if (!Files.exists(inputPath)) {
            System.err.println("Error: input path does not exist: " + inputPath);
            System.exit(1);
        }

        // OscarProperties singleton loads oscar_mcmaster.properties (dev defaults) at class-init time.
        // Override with oscar.properties from the classpath so the real DB credentials are used.
        loadOscarProperties();

        // Bootstrap Spring — oscar.properties must be on the classpath.
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringUtils.setBeanFactory(ctx);

        // Load the provider performing the import.
        ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
        Provider provider = providerDao.getProvider(providerNo);
        if (provider == null) {
            System.err.println("Error: provider not found: " + providerNo);
            ctx.close();
            System.exit(1);
        }

        LoggedInInfo loggedInInfo = new LoggedInInfo();
        loggedInInfo.setLoggedInProvider(provider);
        loggedInInfo.setInitiatingCode(ImportDemographicDataCli.class.getName());

        System.out.printf("Importing %s (provider=%s program=%s timeshift=%d matchProviders=%b)%n",
                inputPath, providerNo, programId, timeshiftInDays, matchProviderNames);

        ImportDemographicDataAction4 action = new ImportDemographicDataAction4();
        ImportDemographicDataAction4.ImportResult result = action.importFromPath(
                loggedInInfo, inputPath, providerNo, programId, matchProviderNames, timeshiftInDays);

        if (!result.warnings.isEmpty()) {
            System.out.println("\n=== WARNINGS ===");
            for (String warning : result.warnings) {
                System.out.println("  " + warning);
            }
        }

        System.out.println("\nImport log written to: " + result.importLogPath);
        System.out.println("Done.");

        ctx.close();
    }

    private static void loadOscarProperties() {
        // The -Doscar_override_properties system property is the standard override mechanism.
        // If the user set it explicitly, OscarProperties already handles it — nothing to do.
        if (System.getProperty("oscar_override_properties") != null) {
            return;
        }
        // Otherwise, look for oscar.properties on the classpath (e.g. from the config dir the
        // user added to -cp) and merge it into the singleton so Spring gets the right DB URL.
        URL url = ImportDemographicDataCli.class.getResource("/oscar-0-SNAPSHOT.properties");
        if (url == null) {
            System.err.println("WARNING: oscar.properties not found on classpath and -Doscar_override_properties not set.");
            System.err.println("         Spring will use dev defaults (oscar_mcmaster.properties) — DB connection will likely fail.");
            System.err.println("         Fix: add the directory containing oscar.properties to -cp, or pass -Doscar_override_properties=/path/to/oscar.properties");
            return;
        }
        try (InputStream is = url.openStream()) {
            OscarProperties.getInstance().load(is);
        } catch (IOException e) {
            System.err.println("Warning: could not load " + url + ": " + e.getMessage());
        }
    }

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
        System.out.println("oscar.properties (with DOCUMENT_DIR and DB config) must be on the classpath.");
    }
}

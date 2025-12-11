//CHECKSTYLE:OFF
package ca.openosp.openo.utility;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for validating file paths to prevent path traversal attacks.
 *
 * <p>Usage for validating a path within a directory (read or write):</p>
 * <pre>
 * File safePath = PathValidationUtils.validatePath(userProvidedFileName, allowedDir);
 * // Now safe to read from or write to safePath
 * </pre>
 *
 * <p>Usage for validating uploaded temp files:</p>
 * <pre>
 * PathValidationUtils.validateUpload(sourceFile);
 * // Now safe to read from sourceFile
 * </pre>
 *
 * <p>Usage for complete upload validation (source + destination):</p>
 * <pre>
 * File destination = PathValidationUtils.validateUpload(
 *     sourceFile, userProvidedFileName, destinationDir);
 * // Now safe to copy sourceFile to destination
 * </pre>
 *
 * @since 2025-12-09
 */
public final class PathValidationUtils {

    private static final Logger logger = MiscUtils.getLogger();

    private PathValidationUtils() {
        // Utility class - prevent instantiation
    }

    // ========================================================================
    // PATH VALIDATION - For validating user-provided paths within a directory
    // ========================================================================

    /**
     * Validates a user-provided filename and returns a safe path within the allowed directory.
     * Use this for both read and write operations where a user provides a filename.
     *
     * <p>Performs the following validations:</p>
     * <ol>
     *   <li>Sanitizes the user-provided filename (strips path components, rejects hidden files)</li>
     *   <li>Validates the resulting path is within the allowed directory</li>
     * </ol>
     *
     * @param userProvidedFileName the filename provided by the user
     * @param allowedDir the directory the file must be within
     * @return the validated File path
     * @throws SecurityException if validation fails
     */
    public static File validatePath(String userProvidedFileName, File allowedDir) {
        // 1. Sanitize filename
        String safeName = sanitizeFileName(userProvidedFileName);

        // 2. Build and validate path
        File path = new File(allowedDir, safeName);
        validateWithinDirectory(path, allowedDir);

        return path;
    }

    // ========================================================================
    // UPLOAD VALIDATION - For validating uploaded files
    // ========================================================================

    /**
     * Validates an uploaded source file is from an allowed temp location.
     * Use this when reading uploaded file content without writing to a destination.
     *
     * @param sourceFile the uploaded file (from Struts2/Tomcat)
     * @throws SecurityException if validation fails
     */
    public static void validateUpload(File sourceFile) {
        validateSource(sourceFile, null);
    }

    /**
     * Validates an upload operation end-to-end and returns the safe destination path.
     *
     * <p>Performs the following validations:</p>
     * <ol>
     *   <li>Validates source file exists and is from an allowed location</li>
     *   <li>Sanitizes the user-provided filename</li>
     *   <li>Validates the destination path is within the allowed directory</li>
     * </ol>
     *
     * @param sourceFile the uploaded file (from Struts2/Tomcat)
     * @param userProvidedFileName the original filename from the upload
     * @param destinationDir the directory where the file should be written
     * @return the validated destination File
     * @throws SecurityException if any validation fails
     */
    public static File validateUpload(
            File sourceFile,
            String userProvidedFileName,
            File destinationDir) {

        // 1. Validate source
        validateSource(sourceFile, destinationDir);

        // 2. Validate destination path
        return validatePath(userProvidedFileName, destinationDir);
    }

    // ========================================================================
    // INTERNAL VALIDATION METHODS
    // ========================================================================

    private static void validateSource(File sourceFile, File expectedBaseDir) {
        if (sourceFile == null) {
            throw new SecurityException("Uploaded file is null");
        }

        if (!sourceFile.exists()) {
            throw new SecurityException("Uploaded file does not exist");
        }

        if (!sourceFile.isFile()) {
            throw new SecurityException("Uploaded file is not a regular file");
        }

        // Check expected base directory first
        if (expectedBaseDir != null && isWithinDirectory(sourceFile, expectedBaseDir)) {
            return;
        }

        // Fallback: check temp directories only if file looks like a temp file
        if (isTempFile(sourceFile) && isInAllowedTempDirectory(sourceFile)) {
            return;
        }

        logger.error("Invalid upload source path: " + sourceFile.getPath());
        throw new SecurityException("Invalid upload source");
    }

    private static void validateWithinDirectory(File file, File allowedBaseDir) {
        if (allowedBaseDir == null || file == null) {
            throw new SecurityException("File or allowed base directory is null");
        }

        try {
            String baseCanonical = allowedBaseDir.getCanonicalPath();
            String fileCanonical = file.getCanonicalPath();
            
            if (!fileCanonical.equals(baseCanonical) && !fileCanonical.startsWith(baseCanonical + File.separator)) {
                logger.error("Path " + fileCanonical + " is outside allowed directory " + baseCanonical);
                throw new SecurityException("Invalid file path");
            }
        } catch (IOException e) {
            logger.error("Error validating file path", e);
            throw new SecurityException("Error validating file path");
        }
    }

    private static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new SecurityException("Filename is null or empty");
        }

        // Use Apache Commons IO to extract just the filename
        String baseName = FilenameUtils.getName(fileName);

        // Reject hidden files (starting with .)
        if (baseName.startsWith(".")) {
            logger.warn("Hidden filenames not allowed: " + fileName);
            throw new SecurityException("Invalid filename: hidden files not allowed");
        }

        // Ensure the result is not empty
        if (baseName.trim().isEmpty()) {
            logger.warn("Filename became empty after sanitization: " + fileName);
            throw new SecurityException("Invalid filename");
        }

        return baseName;
    }

    // ========================================================================
    // INTERNAL HELPER METHODS
    // ========================================================================

    private static boolean isTempFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        // Struts2/Tomcat temp files follow pattern: upload_<hex_ids>_<counter>.tmp
        // Examples: upload__37055a77_11ac9568d10__7ffe_00000033.tmp
        //           upload_c850bd37_8bd7_40cb_88ae_1e86670a61ee_00000000.tmp
        return name.matches("^upload_+[a-f0-9_\\-]+\\.tmp$");
    }

    private static boolean isWithinDirectory(File file, File directory) {
        if (file == null || directory == null) {
            return false;
        }

        try {
            String canonicalPath = file.getCanonicalPath();
            String dirCanonical = directory.getCanonicalPath();
            return canonicalPath.equals(dirCanonical) || canonicalPath.startsWith(dirCanonical + File.separator);
        } catch (IOException e) {
            logger.error("Error checking if file is within directory", e);
            return false;
        }
    }

    private static boolean isInAllowedTempDirectory(File file) {
        if (file == null) {
            return false;
        }

        try {
            String canonicalPath = file.getCanonicalPath();
            List<String> allowedDirectories = getTempDirectories();

            if (allowedDirectories.isEmpty()) {
                logger.warn("No temp directories configured - temp file uploads will be rejected. " +
                           "Check java.io.tmpdir and catalina.base/catalina.home system properties.");
                return false;
            }

            for (String allowedDir : allowedDirectories) {
                if (canonicalPath.equals(allowedDir) || canonicalPath.startsWith(allowedDir + File.separator)) {
                    return true;
                }
            }

            logger.error("File not in allowed temp directory: " + canonicalPath);
            return false;
        } catch (IOException e) {
            logger.error("Error validating upload file path", e);
            return false;
        }
    }

    private static List<String> getTempDirectories() {
        List<String> tempDirs = new ArrayList<>();

        // System temp directory
        String tempDir = System.getProperty("java.io.tmpdir");
        if (tempDir != null) {
            try {
                File tempDirFile = new File(tempDir);
                tempDirs.add(tempDirFile.getCanonicalPath());
            } catch (IOException e) {
                logger.warn("Could not resolve canonical path for java.io.tmpdir", e);
            }
        }

        // Tomcat work directory (where Struts2/Tomcat stores uploaded files)
        String catalinaBase = System.getProperty("catalina.base");
        if (catalinaBase != null) {
            try {
                File tomcatWorkDir = new File(catalinaBase, "work");
                tempDirs.add(tomcatWorkDir.getCanonicalPath());
            } catch (IOException e) {
                logger.warn("Could not resolve canonical path for Tomcat work directory", e);
            }
        }

        // Also check catalina.home in case work dir is there
        String catalinaHome = System.getProperty("catalina.home");
        if (catalinaHome != null && !catalinaHome.equals(catalinaBase)) {
            try {
                File tomcatWorkDir = new File(catalinaHome, "work");
                tempDirs.add(tomcatWorkDir.getCanonicalPath());
            } catch (IOException e) {
                logger.warn("Could not resolve canonical path for Tomcat home work directory", e);
            }
        }

        return tempDirs;
    }
}
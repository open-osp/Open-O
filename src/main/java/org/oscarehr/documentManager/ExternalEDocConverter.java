package org.oscarehr.documentManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

public class ExternalEDocConverter implements EDocConverterInterface {
    private final CommandLine cmdLine;
    private final DefaultExecutor executor = new DefaultExecutor();

    public ExternalEDocConverter(String command, String args) {
        cmdLine = new CommandLine(command);
        cmdLine.addArguments(args, false);
        cmdLine.addArgument("-", false); // stdin
        cmdLine.addArgument("-", false); // stdout
    }

    /**
     * Converts HTML to PDF using an external CLI tool, such as wkhtmltopdf.
     * Please set the WKHTMLTOPDF_COMMAND to your external CLI tool, 
     * and WKHTMLTOPDF_ARGS to your arguments that will be attached to the CLI tool call
     * 
     * @param html the complete HTML string to convert to PDF
     * @param os the {@link ByteArrayOutputStream} where the generated PDF content will be written
     * @throws Exception if the external process fails or PDF conversion is unsuccessful
    */
    @Override
    public void convert(String document, OutputStream os) throws IOException {
        ByteArrayInputStream stdin = new ByteArrayInputStream(document.getBytes(StandardCharsets.UTF_8));
        PumpStreamHandler streams = new PumpStreamHandler(os, os, stdin);
        executor.setStreamHandler(streams);
        int exitCode = executor.execute(cmdLine);
        if (exitCode != 0) {
            throw new IOException("wkhtmltopdf failed with exit code " + exitCode);
        }
    }
}
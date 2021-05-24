package dev.innov8.diy_dic.core.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleLogger implements Logger {

    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_CYAN = "\u001b[36m";
    protected static final String ANSI_YELLOW = "\u001b[33;1m";

    protected static SimpleLogger logger;
    protected final boolean printToConsole;
    protected Writer logWriter;
    protected LocalDate logDate;

    private SimpleLogger(boolean printToConsole) {

        this.printToConsole = printToConsole;

        try {
            this.logWriter = new FileWriter(getLogFileName(), true);
        } catch (IOException e) {
            printMessageToConsole("ERROR", "Could create writer due to: " + e.getMessage());
        }

        this.logDate = LocalDate.now();

    }

    public static SimpleLogger getLogger(boolean loggingToConsole) {
        if (logger == null) {
            logger = new SimpleLogger(loggingToConsole);
        }
        return logger;
    }

    public void info(String message, Object... args) {
        processMessage("INFO", String.format(message, args));
    }

    public void debug(String message, Object... args) {
        processMessage("DEBUG", String.format(message, args));
    }

    public void warn(String message, Object... args) {
        processMessage("WARN", String.format(message, args));
    }

    public void error(String message, Object... args) {
        processMessage("ERROR", String.format(message, args));
    }

    public void fatal(String message, Object... args) {
        processMessage("FATAL", String.format(message, args));
    }

    protected boolean shouldFileRotate() {
        LocalDate currentTime = LocalDate.now();
        return currentTime.isAfter(logDate);
    }

    protected void rotateLogWriter() {
        try {
            this.logWriter = new FileWriter(getLogFileName(), true);
        } catch (IOException e) {
            printMessageToConsole("ERROR", "Could create writer due to: " + e.getMessage());
        }
    }

    protected String getLogFileName() {
        String logDirectoryPath = "src/main/resources/logs";
        String fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("d-MM-uuuu"));
        return logDirectoryPath + "/" + fileName + ".log";
    }

    protected String getCallerName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[4].toString();
    }

    protected String formatMessage(String level, String message) {
        String threadName = Thread.currentThread().getName();
        String callerName = getCallerName();
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return String.format("%s [%s] %s (%s) - %s", datetime, threadName, level, callerName, message);
    }

    protected void processMessage(String logLevel, String message) {
        try {
            if (shouldFileRotate()) rotateLogWriter();
            String formattedMessage = formatMessage(logLevel, message);
            logWriter.write(formattedMessage + "\n");
            if (printToConsole) printMessageToConsole(logLevel, formattedMessage);
        } catch (IOException e) {
            printMessageToConsole("ERROR", "Could not write message to file");
        }
    }

    protected void printMessageToConsole(String level, String message) {
        switch (level) {
            case "INFO":
                System.out.println(message);
                break;
            case "DEBUG":
                System.out.println(ANSI_CYAN + message + ANSI_RESET);
                break;
            case "WARN":
                System.out.println(ANSI_YELLOW + message + ANSI_RESET);
                break;
            case "ERROR":
            case "FATAL":
                System.out.println(ANSI_RED + message + ANSI_RESET);
                break;
        }
    }
}

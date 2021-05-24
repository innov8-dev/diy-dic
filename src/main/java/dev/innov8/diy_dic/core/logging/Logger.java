package dev.innov8.diy_dic.core.logging;

public interface Logger {

    void info(String logMessage, Object... args);
    void debug(String logMessage, Object... args);
    void warn(String logMessage, Object... args);
    void error(String logMessage, Object... args);
    void fatal(String logMessage, Object... args);

}

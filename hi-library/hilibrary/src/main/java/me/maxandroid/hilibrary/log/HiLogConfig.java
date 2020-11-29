package me.maxandroid.hilibrary.log;

public abstract class HiLogConfig {

    private static final String DEFAULT_LOG_TAG = "HiLog";
    public static int MAX_LEN = 512;
    public static HiStackTraceFormatter HI_STACK_TRACE_FORMATTER = new HiStackTraceFormatter();
    public static HiThreadFormatter HI_THREAD_FORMATTER = new HiThreadFormatter();

    public JsonParser injectParser() {
        return null;
    }

    public String getGlobalTag() {
        return DEFAULT_LOG_TAG;
    }

    public boolean enable() {
        return true;
    }

    public boolean includeThread() {
        return false;
    }

    public int stackTraceDepth() {
        return 5;
    }

    public HiLogPrinter[] printers() {
        return null;
    }

    public interface JsonParser {
        String toJson(Object src);
    }
}

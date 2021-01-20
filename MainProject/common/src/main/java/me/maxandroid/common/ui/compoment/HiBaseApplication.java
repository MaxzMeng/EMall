package me.maxandroid.common.ui.compoment;

import android.app.Application;

import com.google.gson.Gson;

import me.maxandroid.hilibrary.log.HiConsolePrinter;
import me.maxandroid.hilibrary.log.HiLogConfig;
import me.maxandroid.hilibrary.log.HiLogManager;

public class HiBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    private void initLog() {
        HiLogManager.init(new HiLogConfig() {
            @Override
            public JsonParser injectParser() {
                return (src) -> new Gson().toJson(src);
            }

            @Override
            public boolean includeThread() {
                return true;
            }
        }, new HiConsolePrinter());
    }
}

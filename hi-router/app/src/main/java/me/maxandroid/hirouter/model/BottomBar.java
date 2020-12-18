package me.maxandroid.hirouter.model;

import java.util.List;

public class BottomBar {

    public int selectTab;
    public List<Tab> tabs;

    public static class Tab{
        public int size;
        public boolean enable;
        public int index;
        public String pageUrl;
        public String title;
    }
}

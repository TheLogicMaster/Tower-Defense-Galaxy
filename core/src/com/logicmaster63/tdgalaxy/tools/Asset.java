package com.logicmaster63.tdgalaxy.tools;

import com.logicmaster63.tdgalaxy.constants.Source;

public class Asset {

    public Class clazz;
    public String path;
    public Source source;

    public Asset(Source source, String path, Class clazz) {
        this.clazz = clazz;
        this.path = path;
        this.source = source;
    }
}

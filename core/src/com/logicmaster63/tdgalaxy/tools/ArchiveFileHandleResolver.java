package com.logicmaster63.tdgalaxy.tools;

import java.util.zip.ZipFile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class ArchiveFileHandleResolver implements FileHandleResolver {

    private final ZipFile archive;

    public ArchiveFileHandleResolver (ZipFile archive) {
        this.archive = archive;
    }

    @Override
    public FileHandle resolve (String fileName) {
        if(fileName.endsWith(".wav") || fileName.endsWith(".mp3") || fileName.endsWith(".ogg"))
            return Tools.getRealFileHandle(fileName, archive);
        return new ArchiveFileHandle(archive, fileName);
    }
}
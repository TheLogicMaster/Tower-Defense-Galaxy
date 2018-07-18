package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.files.FileHandle;
import com.logicmaster63.tdgalaxy.config.CampaignConfigProvider;

public interface TDGalaxyAPI {

    FileHandle loadFile(String path);

    void registerCampaign(String configFile);

    void registerCampaign(FileHandle configFile);

    void registerCampaign(CampaignConfigProvider configProvider);
}

/*
 * Copyright (c) [2016-2017] [University of Minnesota]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.grouplens.samantha.server.indexer;

import org.grouplens.samantha.server.common.JsonHelpers;
import org.grouplens.samantha.server.config.ConfigKey;
import org.grouplens.samantha.server.config.SamanthaConfigService;
import org.grouplens.samantha.server.io.RequestContext;
import play.Configuration;
import play.inject.Injector;

public class JsonFileIndexerConfig implements IndexerConfig {
    private final String filePathKey;
    private final Injector injector;
    private final String daoConfigKey;
    private final Configuration daoConfigs;
    private final Configuration config;

    private JsonFileIndexerConfig(Configuration config, Injector injector,
                                  Configuration daoConfigs, String daoConfigKey, String filePathKey) {
        this.filePathKey = filePathKey;
        this.injector = injector;
        this.daoConfigKey = daoConfigKey;
        this.daoConfigs = daoConfigs;
        this.config = config;
    }

    public static IndexerConfig getIndexerConfig(Configuration indexerConfig,
                                                 Injector injector) {
        return new JsonFileIndexerConfig(indexerConfig, injector,
                indexerConfig.getConfig(ConfigKey.ENTITY_DAOS_CONFIG.get()),
                indexerConfig.getString("daoConfigKey"),
                indexerConfig.getString("filePathKey"));
    }

    public Indexer getIndexer(RequestContext requestContext) {
        String filePath = JsonHelpers.getRequiredString(requestContext.getRequestBody(), filePathKey);
        SamanthaConfigService configService = injector.instanceOf(SamanthaConfigService.class);
        return new JsonFileIndexer(config, configService, daoConfigs, daoConfigKey,
                injector, filePath);
    }
}

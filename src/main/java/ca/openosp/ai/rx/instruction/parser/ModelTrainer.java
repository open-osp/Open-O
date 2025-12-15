/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package ca.openosp.ai.rx.instruction.parser;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.BaseModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ModelTrainer {

    private static final Logger log = LogManager.getLogger(ModelTrainer.class);

    private final String resourcePath;
    private final String modelName;
    private final String trainingDataPath;

    public ModelTrainer(String resourcePath, String modelName, String trainingDataPath) {
        this.resourcePath = resourcePath;
        this.modelName = modelName;
        this.trainingDataPath = trainingDataPath;
    }

    public void trainAndSaveModel(int MAX_ITERATIONS, int CUTOFF) {
        try {
            String trainingDataPath = this.resourcePath + this.trainingDataPath;

            InputStreamFactory inputStreamFactory = () -> Files.newInputStream(Paths.get(trainingDataPath));
            ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
            ObjectStream<NameSample> sampleStream = new ObjectStreamNameSampleImpl(lineStream);

            TrainingParameters params = TrainingParameters.defaultParams();
            params.put(TrainingParameters.ITERATIONS_PARAM, MAX_ITERATIONS);
            params.put(TrainingParameters.CUTOFF_PARAM, CUTOFF);

            TokenNameFinderModel model = NameFinderME.train("en", null, sampleStream,
                    params, new TokenNameFinderFactory());
            sampleStream.close();
            lineStream.close();
    
            saveModel(model);
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }

    private void saveModel(BaseModel model) throws IOException {
        Path path = Paths.get(this.resourcePath + this.modelName);
        try (OutputStream modelOut = Files.newOutputStream(path)) {
            model.serialize(modelOut);
        }
    }

}
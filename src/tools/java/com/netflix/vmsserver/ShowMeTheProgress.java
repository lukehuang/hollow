package com.netflix.vmsserver;

import com.netflix.hollow.util.memory.WastefulRecycler;

import com.netflix.hollow.HollowSchema;
import com.netflix.hollow.filter.HollowFilterConfig;
import com.netflix.hollow.read.engine.HollowBlobReader;
import com.netflix.hollow.read.engine.HollowReadStateEngine;
import com.netflix.hollow.write.HollowBlobWriter;
import com.netflix.hollow.write.HollowWriteStateEngine;
import com.netflix.vms.transformer.SimpleTransformer;
import com.netflix.vms.transformer.hollowinput.VMSHollowVideoInputAPI;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.junit.Test;

public class ShowMeTheProgress {

    @Test
    public void start() throws Exception {
        SimpleTransformer transformer = new SimpleTransformer();

        HollowReadStateEngine inputStateEngine = loadStateEngine("/filtered-input.hollow");
        VMSHollowVideoInputAPI api = new VMSHollowVideoInputAPI(inputStateEngine);

        HollowWriteStateEngine outputStateEngine = transformer.transform(inputStateEngine, api);
        HollowReadStateEngine actualOutputReadStateEngine = roundTripOutputStateEngine(outputStateEngine);
        HollowReadStateEngine expectedOutputStateEngine = loadStateEngine("/expected-output.hollow", getDiffFilter(actualOutputReadStateEngine.getSchemas()));

        ShowMeTheProgressDiffTool.startTheDiff(expectedOutputStateEngine, actualOutputReadStateEngine);
    }

    public static HollowFilterConfig getDiffFilter(Collection<HollowSchema> outputSchemas) {
        HollowFilterConfig filter = new HollowFilterConfig();
        filter.addFieldRecursive("CompleteVideo", "id", outputSchemas);
        filter.addFieldRecursive("CompleteVideo", "country", outputSchemas);
        filter.addField("CompleteVideo", "facetData");
        filter.addFieldRecursive("CompleteVideoFacetData", "videoCollectionsData", outputSchemas);
        filter.addFieldRecursive("CompleteVideoFacetData", "videoMetaData", outputSchemas);

        filter.addTypeRecursive("DrmSystem", outputSchemas);

        return filter;
    }


    private HollowReadStateEngine loadStateEngine(String resourceFilename) throws IOException {
        return loadStateEngine(resourceFilename, new HollowFilterConfig(true));
    }

    private HollowReadStateEngine loadStateEngine(String resourceFilename, HollowFilterConfig filter) throws IOException {
        return loadStateEngine(new BufferedInputStream(this.getClass().getResourceAsStream(resourceFilename)), filter);
    }

    private HollowReadStateEngine loadStateEngine(InputStream is, HollowFilterConfig filter) throws IOException {
        HollowReadStateEngine stateEngine = new HollowReadStateEngine(WastefulRecycler.DEFAULT_INSTANCE);

        HollowBlobReader reader = new HollowBlobReader(stateEngine);

        reader.readSnapshot(is, filter);

        return stateEngine;
    }

    private HollowReadStateEngine roundTripOutputStateEngine(HollowWriteStateEngine stateEngine) throws IOException {
        HollowBlobWriter writer = new HollowBlobWriter(stateEngine);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.writeSnapshot(baos);

        HollowReadStateEngine actualOutputStateEngine = new HollowReadStateEngine();
        HollowBlobReader reader = new HollowBlobReader(actualOutputStateEngine);
        reader.readSnapshot(new ByteArrayInputStream(baos.toByteArray()));
        return actualOutputStateEngine;
    }

}

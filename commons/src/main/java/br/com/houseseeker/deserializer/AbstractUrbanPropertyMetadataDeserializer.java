package br.com.houseseeker.deserializer;

import br.com.houseseeker.domain.provider.UrbanPropertyMetadata;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbstractUrbanPropertyMetadataDeserializer extends JsonDeserializer<List<UrbanPropertyMetadata>> {

    @Override
    public List<UrbanPropertyMetadata> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);
        return deserializeNodes(objectCodec, jsonNode);
    }

    private List<UrbanPropertyMetadata> deserializeNodes(ObjectCodec objectCodec, JsonNode jsonNode) throws JsonProcessingException {
        List<UrbanPropertyMetadata> result = new ArrayList<>();
        Iterator<JsonNode> iterator = jsonNode.elements();
        while (iterator.hasNext())
            result.add(deserializeNode(objectCodec, iterator.next()));
        return result;
    }

    private UrbanPropertyMetadata deserializeNode(ObjectCodec objectCodec, JsonNode jsonNode) throws JsonProcessingException {
        return objectCodec.treeToValue(jsonNode, UrbanPropertyMetadata.class);
    }

}

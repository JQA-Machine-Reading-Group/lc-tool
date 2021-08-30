package us.jbec.lct.transformers;

import us.jbec.lct.models.ImageJob;
import us.jbec.lct.models.ImageJobFields;
import us.jbec.lct.models.capture.DocumentCaptureData;
import us.jbec.lct.models.geometry.LineSegment;

import java.util.HashMap;
import java.util.Map;

public class DocumentCaptureDataTransformer {

    public static ImageJob apply(DocumentCaptureData documentCaptureData) throws CloneNotSupportedException {
        DocumentCaptureData source = DocumentCaptureData.flatten(documentCaptureData, documentCaptureData.getUuid());
        ImageJob target = new ImageJob();

        target.setEdited(source.isEdited());
        target.setCompleted(source.isCompleted());
        target.setId(source.getUuid());

        Map<String, String> fields = new HashMap<>();
        fields.put(ImageJobFields.NOTES.name(), source.getNotes());

        target.setFields(fields);

        source.getCharacterCaptureDataMap().values().stream()
                .map(entry -> entry.get(0).getLabeledRectangle())
                .forEach(labeledRectangle -> {
                    target.getCharacterRectangles().add(labeledRectangle.generateCoordinatesAsList());
                    target.getCharacterLabels().add(labeledRectangle.getLabel());
                });

        target.setWordLines(source.getWordCaptureDataMap().values().stream()
                .map(entry -> entry.get(0).getLineSegment())
                .map(LineSegment::getCoordinatesAsList)
                .toList());

        target.setLineLines(source.getLineCaptureDataMap().values().stream()
                .map(entry -> entry.get(0).getLineSegment())
                .map(LineSegment::getCoordinatesAsList)
                .toList());

        return target;
    }
}

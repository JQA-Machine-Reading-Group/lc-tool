package us.jbec.lct.models.capture;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DocumentCaptureData {
    private String uuid;
    private String notes;
    private boolean completed;
    private boolean edited;

    private Map<String, List<CharacterCaptureData>> characterCaptureDataMap;
    private Map<String, List<WordCaptureData>> wordCaptureDataMap;
    private Map<String, List<LineCaptureData>> lineCaptureDataMap;

    public DocumentCaptureData(@JsonProperty("uuid") String uuid) {
        this.uuid = uuid;
        completed = false;
        edited = false;
        notes = "";
        characterCaptureDataMap = new HashMap<>();
        wordCaptureDataMap = new HashMap<>();
        lineCaptureDataMap = new HashMap<>();
    }

    public static DocumentCaptureData flatten(DocumentCaptureData source) throws CloneNotSupportedException {
        return DocumentCaptureData.flatten(source, source.getUuid());
    }

    public static DocumentCaptureData flatten(DocumentCaptureData source, String uuid) throws CloneNotSupportedException {
        DocumentCaptureData target = new DocumentCaptureData(uuid);
        target.setCompleted(source.isCompleted());
        target.setEdited(source.isEdited());
        target.setNotes(source.getNotes());

        target.setCharacterCaptureDataMap(flattenDataMap(source.getCharacterCaptureDataMap()));
        target.setWordCaptureDataMap(flattenDataMap(source.getWordCaptureDataMap()));
        target.setLineCaptureDataMap(flattenDataMap(source.getLineCaptureDataMap()));

        return target;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNotes() {
        return Jsoup.clean(notes, Safelist.basic());
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public Map<String, List<CharacterCaptureData>> getCharacterCaptureDataMap() {
        return characterCaptureDataMap;
    }

    public void setCharacterCaptureDataMap(Map<String, List<CharacterCaptureData>> characterCaptureDataMap) {
        this.characterCaptureDataMap = characterCaptureDataMap;
    }

    public Map<String, List<WordCaptureData>> getWordCaptureDataMap() {
        return wordCaptureDataMap;
    }

    public void setWordCaptureDataMap(Map<String, List<WordCaptureData>> wordCaptureDataMap) {
        this.wordCaptureDataMap = wordCaptureDataMap;
    }

    public Map<String, List<LineCaptureData>> getLineCaptureDataMap() {
        return lineCaptureDataMap;
    }

    public void setLineCaptureDataMap(Map<String, List<LineCaptureData>> lineCaptureDataMap) {
        this.lineCaptureDataMap = lineCaptureDataMap;
    }

    public void insertCharacterCaptureData(CharacterCaptureData data) {
        if (!characterCaptureDataMap.containsKey(data.getUuid())) {
            characterCaptureDataMap.put(data.getUuid(), new ArrayList<>());
        }
        characterCaptureDataMap.get(data.getUuid()).add(data);
    }

    public void insertWordCaptureData(WordCaptureData data) {
        if (!wordCaptureDataMap.containsKey(data.getUuid())) {
            wordCaptureDataMap.put(data.getUuid(), new ArrayList<>());
        }
        wordCaptureDataMap.get(data.getUuid()).add(data);
    }

    public void insertLineCaptureData(LineCaptureData data) {
        if (!lineCaptureDataMap.containsKey(data.getUuid())) {
            lineCaptureDataMap.put(data.getUuid(), new ArrayList<>());
        }
        lineCaptureDataMap.get(data.getUuid()).add(data);
    }

    private static <T extends CaptureData> Map<String, List<T>> flattenDataMap(Map<String, List<T>> mapToFlatten) throws CloneNotSupportedException {
        var resultMap = new HashMap<String, List<T>>();
        for(var entry : mapToFlatten.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            var containsDeleteRecord = entry.getValue().stream()
                    .anyMatch(record -> record.getCaptureDataRecordType() == CaptureDataRecordType.DELETE);
            if (!containsDeleteRecord) {
                var recordToKeep = entry.getValue().get(0);
                resultMap.put(entry.getKey(), List.of((T) recordToKeep.clone()));
            }
        }
        return resultMap;
    }
}

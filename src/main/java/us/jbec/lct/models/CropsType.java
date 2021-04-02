package us.jbec.lct.models;

public enum CropsType {
    LETTERS("letters"),
    WORDS("words"),
    LINES("lines");

    private String directoryName;

    CropsType(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}
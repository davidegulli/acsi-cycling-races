package it.acsi.cycling.races.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import it.acsi.cycling.races.domain.enumeration.FileType;

/**
 * A DTO for the {@link it.acsi.cycling.races.domain.File} entity.
 */
public class FileDTO implements Serializable {

    private Long id;

    private String title;

    @NotNull
    private FileType type;

    @NotNull
    private String mimeType;

    @Lob
    private byte[] binary;

    private String binaryContentType;
    private String url;


    private Long raceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    public String getBinaryContentType() {
        return binaryContentType;
    }

    public void setBinaryContentType(String binaryContentType) {
        this.binaryContentType = binaryContentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getRaceId() {
        return raceId;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileDTO fileDTO = (FileDTO) o;
        if (fileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FileDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", binary='" + getBinary() + "'" +
            ", url='" + getUrl() + "'" +
            ", race=" + getRaceId() +
            "}";
    }
}

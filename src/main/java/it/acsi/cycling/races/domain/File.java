package it.acsi.cycling.races.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import it.acsi.cycling.races.domain.enumeration.FileType;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "title")
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FileType type;

    @NotNull
    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Lob
    @Column(name = "jhi_binary")
    private byte[] binary;

    @Column(name = "jhi_binary_content_type")
    private String binaryContentType;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JsonIgnoreProperties("attachments")
    private Race race;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public File title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileType getType() {
        return type;
    }

    public File type(FileType type) {
        this.type = type;
        return this;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public File mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getBinary() {
        return binary;
    }

    public File binary(byte[] binary) {
        this.binary = binary;
        return this;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    public String getBinaryContentType() {
        return binaryContentType;
    }

    public File binaryContentType(String binaryContentType) {
        this.binaryContentType = binaryContentType;
        return this;
    }

    public void setBinaryContentType(String binaryContentType) {
        this.binaryContentType = binaryContentType;
    }

    public String getUrl() {
        return url;
    }

    public File url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Race getRace() {
        return race;
    }

    public File race(Race race) {
        this.race = race;
        return this;
    }

    public void setRace(Race race) {
        this.race = race;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return id != null && id.equals(((File) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", binary='" + getBinary() + "'" +
            ", binaryContentType='" + getBinaryContentType() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}

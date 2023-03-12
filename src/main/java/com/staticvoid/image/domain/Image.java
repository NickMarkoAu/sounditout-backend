package com.staticvoid.image.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.File;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image implements Serializable {

    public Image(Long id, String fileName, String userId, String tags, File file) {
        this.id = id;
        this.fileName = fileName;
        this.userId = userId;
        this.tags = tags;
        this.file = file;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;
    private String userId;
    private String s3uri;
    private String tags;

    @Transient
    private File file;
}

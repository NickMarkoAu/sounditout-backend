package com.staticvoid.image.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.File;
import java.io.Serializable;

@Entity
@Data
public class Image implements Serializable {
    @Id
    private String id;
    private String userId;
    private String s3uri;
    private String tags;
    @Transient
    private File file;
}

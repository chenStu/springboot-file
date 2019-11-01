package com.example.file.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author CJS
 * @description 文件信息
 * @date 10:18 19/10/21
 */
@Data
public class FileInfo implements Serializable {

    /**
     * 文件id号
     */
    private Integer id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件标识符
     */
    private String identifier;

    /**
     * 文件大小
     */
    private Long totalSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件位置
     */
    private String location;

}

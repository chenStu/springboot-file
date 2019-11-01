package com.example.file.service;

import com.example.file.entity.FileInfo;

import java.util.List;

/**
 * @author CJS
 * @description
 * @date 14:02 19/10/21
 */
public interface FileInfoService {

    /**
     * @param fileInfo 文件对象
     * @return void
     * @author CJS
     * @description 保存文件信息
     * @date 14:02 19/10/21
     */
    void saveFileInfo(FileInfo fileInfo);

    /**
     * @return java.util.List<com.example.file.entity.FileInfo>
     * @author CJS
     * @description 查看所有文件
     * @date 14:19 19/10/22
     */
    List<FileInfo> queryAllFiles();
}

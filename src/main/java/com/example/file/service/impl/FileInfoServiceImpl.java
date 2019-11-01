package com.example.file.service.impl;

import com.example.file.entity.FileInfo;
import com.example.file.mapper.FileInfoMapper;
import com.example.file.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CJS
 * @description
 * @date 14:04 19/10/21
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    /**
     * @param fileInfo 文件对象
     * @return void
     * @author CJS
     * @description 保存文件信息
     * @date 14:02 19/10/21
     */
    @Override
    public void saveFileInfo(FileInfo fileInfo) {
        fileInfoMapper.saveFileInfo(fileInfo);
    }

    /**
     * @return java.util.List<com.example.file.entity.FileInfo>
     * @author CJS
     * @description 查看所有文件
     * @date 14:19 19/10/22
     */
    @Override
    public List<FileInfo> queryAllFiles() {
        return fileInfoMapper.queryAllFiles();
    }

}

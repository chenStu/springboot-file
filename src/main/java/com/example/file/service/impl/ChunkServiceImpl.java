package com.example.file.service.impl;

import com.example.file.entity.Chunk;
import com.example.file.mapper.ChunkMapper;
import com.example.file.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CJS
 * @description
 * @date 11:36 19/10/21
 */
@Service
public class ChunkServiceImpl implements ChunkService {

    @Autowired
    private ChunkMapper chunkMapper;

    /**
     * @param chunk 文件块对象
     * @return void
     * @author CJS
     * @description 保存文件块
     * @date 11:22 19/10/21
     */
    @Override
    public void saveChunk(Chunk chunk) {
        chunkMapper.saveChunk(chunk);
    }

    /**
     * @param identifier 文件标识
     * @return boolean
     * @author CJS
     * @description 根据文件唯一标识和当前块数，判断是否已经上传过这个块
     * @date 11:23 19/10/21
     */
    @Override
    public int[] checkChunk(String identifier) {
        return chunkMapper.checkChunk(identifier);
    }
}

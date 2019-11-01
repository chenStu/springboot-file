package com.example.file.service;

import com.example.file.entity.Chunk;

/**
 * @author CJS
 * @description
 * @date 11:35 19/10/21
 */
public interface ChunkService {

    /**
     * @param chunk 块对象
     * @return void
     * @author CJS
     * @description 保存块
     * @date 11:22 19/10/21
     */
    void saveChunk(Chunk chunk);

    /**
     * @param identifier 文件标识
     * @return boolean
     * @author CJS
     * @description 根据文件唯一标识和当前块数，判断是否已经上传过这个块
     * @date 11:23 19/10/21
     */
    int[] checkChunk(String identifier);

}

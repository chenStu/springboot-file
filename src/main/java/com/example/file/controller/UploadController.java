package com.example.file.controller;

import com.example.file.entity.Chunk;
import com.example.file.entity.FileInfo;
import com.example.file.service.ChunkService;
import com.example.file.service.FileInfoService;
import com.example.file.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CJS
 * @description
 * @date 13:56 19/10/21
 */
@RestController
@Slf4j
@RequestMapping("/uploader")
public class UploadController {

    // 上传文件路径
    private String uploadFolder = "E:" + File.separator + "uploaderFiles";

    // 分块文件路径
    private String chunkFolder;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private ChunkService chunkService;

    @PostMapping("/chunk")
    public ResultUtil uploadChunk(Chunk chunk) {
        MultipartFile file = chunk.getFile();
        log.info("【分块上传】file originName: " + file.getOriginalFilename() + ", chunkNumber: {}" + chunk.getChunkNumber());
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(generatePath(uploadFolder, chunk));
            log.info("【分块上传】分块路径：" + path);
            //文件写入指定路径
            Files.write(path, bytes);
            log.info("【分块上传】文件 " + chunk.getFilename() + "写入成功, uuid:{}" + chunk.getIdentifier());
            chunkService.saveChunk(chunk);
            return ResultUtil.success("文件上传成功", null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error("后端异常...", null);
        }
    }

    @GetMapping("/chunk")
    public Map<String, Object> checkChunk(Chunk chunk) {
        Map<String, Object> res = new HashMap<>(32);
        int[] chunkNumbers = chunkService.checkChunk(chunk.getIdentifier());
        if (chunkNumbers != null) {
            res.put("chunkNumbers", chunkNumbers);
        }
        return res;
    }

    @PostMapping("/merge")
    public void merge(Chunk chunk) {
        log.info("【分块合并】合并开始");
        String fileName = chunk.getFilename();
        File file = merge(fileName);
        if (file != null) {
            File directory = new File(chunkFolder);
            log.info("【分块合并】文件夹大小：" + directory.listFiles().length);
            if (directory.listFiles().length == 0) {
                directory.delete();
                log.info("【分块合并】合并成功");
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(file.getName());
                fileInfo.setIdentifier(chunk.getIdentifier());
                fileInfo.setTotalSize(file.length());
                fileInfo.setFileType((file.getName().substring(file.getName().lastIndexOf("."))).substring(1));
                fileInfo.setLocation(file.getPath());
                fileInfoService.saveFileInfo(fileInfo);
                log.info("【分块合并】文件信息保存成功");
            }
        } else {
            log.info("【分块合并】合并失败");
        }

    }

    /**
     * 生成分块的文件路径
     */
    private String generatePath(String uploadFolder, Chunk chunk) {
        StringBuilder sb = new StringBuilder();
        // 拼接上传的路径
        sb.append(uploadFolder).append(File.separator).append(chunk.getIdentifier());
        chunkFolder = sb.toString();

        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(chunkFolder))) {
            try {
                Files.createDirectories(Paths.get(chunkFolder));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        // 返回以 - 隔离的分块文件，后面跟的chunkNumber方便后面进行排序进行merge
        return sb.append(File.separator)
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();

    }

    /**
     * 合并文件
     */
    private File merge(String fileName) {
        try {
            // 如果合并后的路径不存在，则新建
            if (!Files.isWritable(Paths.get(uploadFolder))) {
                Files.createDirectories(Paths.get(uploadFolder));
            }
            // 合并的文件名
            String target = uploadFolder + File.separator + fileName;
            // 创建文件
            Files.createFile(Paths.get(target));
            // 遍历分块的文件夹，并进行过滤和排序后以追加的方式写入到合并后的文件
            log.info("【分块合并】合并所需块路径：" + chunkFolder);
            Files.list(Paths.get(chunkFolder))
                    //过滤带有"-"的文件
                    .filter(path -> path.getFileName().toString().contains("-"))
                    //按照从小到大进行排序
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(target), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            return new File(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

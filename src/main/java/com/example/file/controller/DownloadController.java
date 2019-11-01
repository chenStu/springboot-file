package com.example.file.controller;

import com.example.file.entity.FileInfo;
import com.example.file.service.FileInfoService;
import com.example.file.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author CJS
 * @description
 * @date 13:55 19/10/22
 */
@RestController
@Slf4j
@RequestMapping("/download")
public class DownloadController {

    private final static String ROOT_PATH = "E:" + File.separator + "uploaderFiles" + File.separator;

    @Autowired
    private FileInfoService fileInfoService;

    @GetMapping("/allFiles")
    public ResultUtil allFiles() {
        List<FileInfo> fileInfos = fileInfoService.queryAllFiles();
        if (fileInfos != null) {
            return ResultUtil.success("查询成功", fileInfos);
        }
        return ResultUtil.error("无数据", null);
    }

    @GetMapping("/file")
    public Object downloadFile(String fileName, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        File f = new File(ROOT_PATH + fileName);
        System.out.println("下载文件：" + f);
        try {
            out = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            in = new FileInputStream(f);
            // 复制
            IOUtils.copy(in, out);
            out.flush();
            log.info(fileName + "下载成功");
        } catch (IOException e) {
            log.error("下载文件失败:" + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error("错误:" + e.getMessage());
            }
        }
        return null;
    }

    @GetMapping("/isExist")
    public ResultUtil isExist(String fileName) {
        File file = new File(ROOT_PATH + fileName);
        if (file.exists()) {
            return ResultUtil.success("文件存在", null);
        }
        return ResultUtil.error(fileName + "该文件不存在", null);
    }

}

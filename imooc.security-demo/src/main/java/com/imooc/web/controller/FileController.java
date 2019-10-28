package com.imooc.web.controller;

import com.imooc.dto.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {

    String folder = "D:\\program\\IdeaProjects\\spring\\imoocsecurity\\imooc.security-demo\\src\\main\\java\\com\\imooc\\web\\controller";


    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

        //实际应用不会存到项目中，
        File localFile = new File(folder, new Date().getTime() + ".txt");
        file.transferTo(localFile);
        return new FileInfo(folder);
    }

    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try(
                InputStream inputStream = new FileInputStream(new File(folder, id + ".txt"));
                OutputStream outputStream = response.getOutputStream();
                ) {
            response.setHeader("Content-Disposition", "attachment;filename=test.txt");
            response.setContentType("application/x-download");
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
        }
    }

}

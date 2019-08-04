package com.ddl.poiTest;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;

public class convertImgTest {
    public static void main(String[] args) throws IOException {
        /**
         * D:\soft\LibreOffice_6.font-awesome.6\program\soffice:表示libreoffice安装路径
         * D:\Desktop\DocCloud\testDir\hadoopInstall.doc：表示要转化的word文件
         */
        String fileName= "E:\\dd.docx";
        String command = "C:\\Program Files\\LibreOffice\\program\\soffice --headless --invisible --convert-to html "+fileName;
        /**
         *workDir:表示转化之后的HTML文件保存的路径地址
         */
        String workDir = "E:\\"+ UUID.randomUUID().toString()+"\\";
        File file = new File(workDir);
        //创建目录--因为是UUID所以不用判断目录一定不存在
        file.mkdirs();
        /**
         * command:命令
         * null：操作系统运行程序时通过envp 参数将系统环境变量传递给程序
         * file：命令在那个路径下执行
         */
        //返回过程对象--Process
        Process exec = Runtime.getRuntime().exec(command,null,file);
        //错误信息
        InputStream errorStream = exec.getErrorStream();
        //结果信息
        InputStream inputStream = exec.getInputStream();
        //IOUtils-直接将流转化成字符串
        String error = IOUtils.toString(errorStream);
        String result = IOUtils.toString(inputStream);
        //打印信息
        System.out.println(error);
        System.out.println(result);
    }
}

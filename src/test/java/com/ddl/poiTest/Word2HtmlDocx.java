package com.ddl.poiTest;

import java.io.*;
import java.net.URLDecoder;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

/**
 * @author: 牛杰
 * @since: May 3, 2012
 * @modified: May 3, 2012
 * @version:
 */
@Slf4j
@Component
public class Word2HtmlDocx {

    public static void main(String arg[]) throws Exception {

        try {
            docx2Html("E:/dd.docx", "E:/docx.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String content, String path, String header) {
        //利用jsoup解析HTML
        org.jsoup.nodes.Document doc = Jsoup.parse(content);

        Elements table = doc.getElementsByTag("table");

        Elements div = doc.getElementsByTag("div");

        Element node = table.get(table.size() - 1);

        //获取解析后的页眉，与之前做对比，如果相同，则提取
        String text = node.text().replaceAll("\\s", "");

        String s1 = header.replaceAll("\\s", "").replaceAll("\r", "");

        //把页眉替换到头部
        Element child = div.first().child(0);
        child.before(node.toString());
        //删除多余页眉
        node.remove();

        //遍历取出图片
        Elements img = doc.select("img");
        for (Element element : img) {
            String src = element.attr("src");

            if (src.endsWith("emf")) {

            }
        }

        //遍历取出所有a标签，解析href，替换为自己的接口,访问接口去数据库对比是否存在文件
        Elements e = doc.select("a");
        for (int i = 0; i < e.size(); i++) {
            Element anode = e.get(i);
            String href = anode.attr("href");

            if (href.startsWith("http") || href.startsWith("https")) {

            } else {
                try {
                    href = URLDecoder.decode(href, "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                String[] split = href.split("/");
                String s = split[split.length - 1];

                href = "http://localhost/test/wordDoc/?word=" + s; //修改style中的url值
            }
            anode.attr("href", href);
        }

        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));

            bw.write(doc.toString());
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }

    /**
     * 解析doc
     *
     * @param fileName
     * @param outPutFile
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void convert2Html(String fileName, String outPutFile)
            throws TransformerException, IOException,
            ParserConfigurationException {
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));

        //WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
        HeaderStories headerStories = new HeaderStories(wordDocument);
        String header = headerStories.getHeader(1);

        PicturesTable picturesTable = wordDocument.getPicturesTable();
        List<Picture> allPictures = picturesTable.getAllPictures();

        for (Picture allPicture : allPictures) {
            String mimeType = allPicture.getMimeType();
            PictureType pictureType = allPicture.suggestPictureType();
            byte[] rawContent = allPicture.getRawContent();

            //String s = UtilHelper.byte2Base64StringFun(rawContent);
        }

        Range overallRange = wordDocument.getOverallRange(); //页眉页脚
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());

        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content,
                                      PictureType pictureType, String suggestedName,
                                      float widthInches, float heightInches) {
                String mime = pictureType.getMime();


                return "" + suggestedName;
            }
        });
        wordToHtmlConverter.processDocumentPart(wordDocument, overallRange);
//        wordToHtmlConverter.processDocument(wordDocument);

        //save pictures
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                String mimeType = pic.getMimeType();
                System.out.println(pic);

                try {
                    pic.writeImageContent(new FileOutputStream("E:/file/"
                            + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        writeFile(new String(out.toByteArray()), outPutFile, header);
    }

    /**
     * 解析docx
     *
     * @param fileName   文件名称
     * @param outPutFile 输出路径
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static String docx2Html(String fileName, String outPutFile) throws TransformerException, IOException,
            ParserConfigurationException {
        String fileOutName = outPutFile;
        long startTime = System.currentTimeMillis();
        XWPFDocument document = new XWPFDocument(new FileInputStream(fileName));
        XHTMLOptions options = XHTMLOptions.create();

        options.setIgnoreStylesIfUnused(true);
        options.setFragment(true);

        //页眉页脚
        XWPFHeaderFooterPolicy headerFooterPolicy = document.getHeaderFooterPolicy();
        XWPFHeader header = headerFooterPolicy.getDefaultHeader();  //页眉

        XWPFFooter footer = headerFooterPolicy.getDefaultFooter();  //页脚

        List<XWPFTable> headerTables = header.getTables();
        for (XWPFTable table : headerTables) {
            //页眉解析
            CTTbl ctTbl = table.getCTTbl();    //xml格
            String text = table.getText();
            //XHTMLConverter.getInstance().convert(part, new FileOutputStream("F:\\file\\1.html"), options);
            System.out.println();
        }
        //List<XWPFTable> footerTables = footer.getTables();
        XWPFDocument xwpfDocument = footer.getXWPFDocument();
        //导出页眉图片
        List<XWPFPictureData> allPictures = header.getAllPictures();
        int size = allPictures.size();
        if (size > 0) {
            for (XWPFPictureData picture : allPictures) {
                byte[] data = picture.getData();
                new FileOutputStream("E:\\file" + picture.getFileName()).write(data);
            }
        }
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            String paragraphText = paragraph.getText();
            System.out.println();
        }
        //导出图片
        File imageFolder = new File("E:/file");
        options.setExtractor(new FileImageExtractor(imageFolder));
        // URI resolver word的html中图片的目录路径
        options.URIResolver(new BasicURIResolver("images"));

        File outFile = new File(fileOutName);
        outFile.getParentFile().mkdirs();
        OutputStream out = new FileOutputStream(outFile);
        XHTMLConverter.getInstance().convert(document, out, options);
        FileInputStream fs = new FileInputStream(fileOutName);
        byte[] buf = new byte[4096];
        int readLength;
        String htmls = "";
        while (((readLength = fs.read(buf)) != -1)) {
            htmls += (char) readLength;
        }
        //fileOutName
        htmls = readFileToString(fileOutName, "html");

        return htmls;
    }

    public static String readFileToString(String path, String type) {
        // 定义返回结果
        String jsonString = "";
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));// 读取文件
            String thisLine = null;
            while ((thisLine = in.readLine()) != null) {
                sb.append(thisLine);
                if ("txt".equals(type))
                    sb.append("<br/>");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException el) {
                }
            }
        }
        jsonString = sb.toString();
        // 返回拼接好的JSON String
        return jsonString;
    }
}

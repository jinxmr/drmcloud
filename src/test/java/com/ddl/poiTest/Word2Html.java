package com.ddl.poiTest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
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
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author: 牛杰
 * @since: May 3, 2012
 * @modified: May 3, 2012
 * @version:
 */
@Slf4j
@Component
public class Word2Html {

    public static void main(String argv[]) {
        try {
            convert2Html("E://tt.doc", "E://file//1.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String content, String path) {
        //利用jsoup解析HTML
        org.jsoup.nodes.Document doc = Jsoup.parse(content);

        //取出职责内容,存库对应关系
        String text = doc.text();
        String s1 = text.split("Responsibilities 职责")[1];

        String s2 = s1.split("5.1")[0];


        Elements table = doc.getElementsByTag("table");

        Elements div = doc.getElementsByTag("div");

        Element node = table.get(table.size() - 1);
        //把页眉替换到头部
        Element child = div.first().child(0);
        child.before(node.toString());
        //删除多余页眉
        node.remove();

        //遍历取出图片
        Elements img = doc.select("img");
        for (Element element : img) {
            String src= element.attr("src");

            if (src.endsWith("emf")) {

            }
        }

        //遍历取出所有a标签，解析href，替换为自己的接口,访问接口去数据库对比是否存在文件
        Elements e = doc.select("a");
        for (int i = 0; i < e.size(); i++) {
            Element anode = e.get(i);
            String href= anode.attr("href");

            if (href.startsWith("http") || href.startsWith("https")) {

            } else {
                try {
                    href= URLDecoder.decode(href,"UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                String[] split = href.split("/");
                String s = split[split.length - 1];

                href = "http://localhost/test/wordDoc/?word="+s; //修改style中的url值
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

    public static void convert2Html(String fileName, String outPutFile)
            throws TransformerException, IOException,
            ParserConfigurationException {
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
        HeaderStories headerStories = new HeaderStories(wordDocument);

        Range overallRange = wordDocument.getOverallRange(); //页眉页脚

        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());

        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content,
                                      PictureType pictureType, String suggestedName,
                                      float widthInches, float heightInches) {
                return "" + suggestedName;
            }
        });


        wordToHtmlConverter.processDocumentPart(wordDocument, overallRange);

        //save pictures
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                String mimeType = pic.getMimeType();
                byte[] content = pic.getContent();
                if(mimeType.toLowerCase().equals("image/x-emf")) {
                    InputStream is = new ByteArrayInputStream(content);
                    byte[] bytes = emfToPng(is);
                    BASE64Encoder encoder = new BASE64Encoder();
                    String str = encoder.encode(bytes).trim();

                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] imgbyte = decoder.decodeBuffer(str);
                    OutputStream os = new FileOutputStream("E:\\file\\text.png");
                    os.write(imgbyte, 0, imgbyte.length);
                    os.flush();
                    os.close();
                }

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
        writeFile(new String(out.toByteArray()), outPutFile);
    }

    private static byte[] emfToPng(InputStream is){
        // InputStream inputStream=null;
        byte[] by=null;
        EMFInputStream emf = null;
        EMFRenderer emfRenderer = null;
        //创建储存图片二进制流的输出流
        ByteArrayOutputStream baos = null;
        //创建ImageOutputStream流
        ImageOutputStream imageOutputStream = null;
        try {
            emf = new EMFInputStream(is, EMFInputStream.DEFAULT_VERSION);
            emfRenderer = new EMFRenderer(emf);

            final int width = (int)emf.readHeader().getBounds().getWidth();
            final int height = (int)emf.readHeader().getBounds().getHeight();
            final BufferedImage result = new BufferedImage(width+60, height+40, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = (Graphics2D)result.createGraphics();
            emfRenderer.paint(g2);
            //开启文字抗锯齿
            //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //创建储存图片二进制流的输出流
            baos = new ByteArrayOutputStream();
            //创建ImageOutputStream流
            imageOutputStream = ImageIO.createImageOutputStream(baos);
            //将二进制数据写进ByteArrayOutputStream
            ImageIO.write(result, "jpg", imageOutputStream);
            //inputStream = new ByteArrayInputStream(baos.toByteArray());
            by=baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(imageOutputStream!=null){
                    imageOutputStream.close();
                }
                if(baos!=null){
                    baos.close();
                }
                if(emfRenderer!=null){
                    emfRenderer.closeFigure();
                }
                if(emf!=null){
                    emf.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return by;
    }

    /**
     * 二进制流转Base64字符串
     *
     * @param data 二进制流
     * @return data
     * @throws IOException 异常
     */
    public static String getImageString(byte[] data) throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? encoder.encode(data) : "";
    }
}

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
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

public class convertImgTest {
    public static void main(String[] args) throws IOException {
        /*Domain d1 = new Domain("c");
        Domain d2 = new Domain("c");
        Domain d3 = new Domain("b");
        Domain d4 = new Domain("d");
        System.out.println(d1.compareTo(d2));
        System.out.println(d1.compareTo(d3));
        System.out.println(d1.compareTo(d4));*/
        Integer[] arr = {-1, 1, 2, -3, 4};
        Arrays.sort(arr);
        for(Integer str : arr) {
            System.out.println(str);
        }
    }
}

class DomainComparator implements Comparator<Domain>
{
    public int compare(Domain domain1, Domain domain2)
    {
        if (domain1.getStr().compareTo(domain2.getStr()) > 0)
            return 1;
        else if (domain1.getStr().compareTo(domain2.getStr()) == 0)
            return 0;
        else
            return -1;
    }
}

class Domain implements Comparable<Domain>
{
    private String str;

    public Domain(String str)
    {
        this.str = str;
    }

    public int compareTo(Domain domain)
    {
        if (this.str.compareTo(domain.str) > 0)
            return 1;
        else if (this.str.compareTo(domain.str) == 0)
            return 0;
        else
            return -1;
    }

    public String getStr()
    {
        return str;
    }
}
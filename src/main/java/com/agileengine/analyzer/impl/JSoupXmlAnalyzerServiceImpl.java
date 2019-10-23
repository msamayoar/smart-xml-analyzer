package com.agileengine.analyzer.impl;

import com.agileengine.analyzer.XmlAnalyzerService;
import com.agileengine.analyzer.util.PrintUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class JSoupXmlAnalyzerServiceImpl implements XmlAnalyzerService{

    private static Logger LOGGER = Logger.getLogger(JSoupXmlAnalyzerServiceImpl.class.getName());

    private static String CHARSET_NAME = "utf8";

    public JSoupXmlAnalyzerServiceImpl(){
    }

    @Override
    public File getHtmlResource(final String filePath) {
        File file = null;
        try{
            file = new File(filePath);
        }catch (Exception e){
            System.out.println("Error:" + e.getMessage());
        }
        return file;
    }

    @Override
    public Optional<Element> getElementByIf(File htmlFile, String element){
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            return Optional.of(doc.getElementById(element));

        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void doDiff(File htmlDiff, Element mainElement){
        try {
            Document doc = Jsoup.parse(
                    htmlDiff,
                    CHARSET_NAME,
                    htmlDiff.getAbsolutePath());

            Elements elements = doc.getElementsByTag(mainElement.tagName());

            if(null != elements && elements.size()>0){

                getElementsAndCoCompare(elements, mainElement);

            }else{
                System.out.println("No elements were found.");
            }


        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());

        }
    }

    private void getElementsAndCoCompare(Elements elements, Element mainElement){

        List<Element> toCompareElements = elements.stream().
                filter(foundElem -> foundElem.text().trim().equalsIgnoreCase(mainElement.text())  &&
                        checkAttributes(mainElement.attributes(), foundElem.attributes()) )
                .collect(Collectors.toList());

        if(null!= toCompareElements && toCompareElements.size()>0){
            for(Element e: toCompareElements){
                System.out.println("Element found: " + e);
                PrintUtil.printXpathElement(e);
                PrintUtil.printDiff(mainElement, e);
            }
        }else{
            System.out.println("No diff were found.");
        }

    }




    private boolean checkAttributes(Attributes srcAttributes, Attributes diffAttributes){
        if(srcAttributes.size() != diffAttributes.size()) return true;

        boolean diffOne = false;

        for(Attribute a: srcAttributes){
            String diffVal = diffAttributes.get(a.getKey());

            if(StringUtils.isNotEmpty(diffVal)){
                diffOne = diffVal.equals(a.getValue());
            }
            if(diffOne) break;
        }

        return diffOne ;
    }

}

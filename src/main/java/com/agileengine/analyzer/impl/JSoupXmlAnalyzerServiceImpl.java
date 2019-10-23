package com.agileengine.analyzer.impl;

import com.agileengine.analyzer.XmlAnalyzerService;
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
            //LOGGER , "Error reading file: " + filePath);
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
            //LOGGER.log(Level.ERROR, "Error reading [{}] file", htmlFile.getAbsolutePath(), e);
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

                List<Element> toCompareElements = elements.stream().
                        filter(foundElem -> foundElem.text().trim().equalsIgnoreCase(mainElement.text())  &&
                                checkAttributes(mainElement.attributes(), foundElem.attributes()) )
                        .collect(Collectors.toList());

                for(Element e: toCompareElements){

                    System.out.println("Element found: " + e);
                    printXpathElement(e);
                    printDiff(mainElement, e);
                }
            }


        } catch (IOException e) {
            //LOGGER.log(Level.ERROR, "Error reading [{}] file", htmlFile.getAbsolutePath(), e);

        }
    }

    private void printXpathElement(Element e){
        if( e == null){
            return;
        }else{
            this.printXpathElement(e.parent());
            System.out.print(e.tagName() + " > ");
        }

    }

    private void printDiff(Element srcE, Element diffE){
        System.out.println("");
        for(Attribute a: srcE.attributes()){
            String diffVal = diffE.attributes().get(a.getKey());

            if(StringUtils.isNotEmpty(diffVal)){
                System.out.println("diff >>>>" + a.getKey() + "=\""  + diffE.attributes().get(a.getKey()) +"\"" );
            }
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

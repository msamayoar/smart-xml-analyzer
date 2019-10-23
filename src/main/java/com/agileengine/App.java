package com.agileengine;

import com.agileengine.analyzer.XmlAnalyzerService;
import com.agileengine.analyzer.impl.JSoupXmlAnalyzerServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.Optional;


public class App 
{
    public static void main( String[] args )
    {
        if(null == args || args.length < 2){
            System.out.println("Please enter a VALID INPUT to process");
            System.out.println(">> original_file.html changed_file.html");
            return;
        }

        final String originalHtmlFile = args[0];
        final String changedHtmlFile =  args[1];

        final XmlAnalyzerService xmlService = new JSoupXmlAnalyzerServiceImpl();

        if(StringUtils.isNotEmpty(originalHtmlFile) && StringUtils.isNotEmpty(changedHtmlFile) ) {
            File fileSrcHtml = xmlService.getHtmlResource(originalHtmlFile);
            File fileChangedHtml = xmlService.getHtmlResource(changedHtmlFile);

            if(null != fileSrcHtml && null != fileChangedHtml){
                final String id = "make-everything-ok-button";
                Optional<Element> elementToIdentify =  xmlService.getElementByIf(fileSrcHtml, id);

                if(elementToIdentify.isPresent()){
                    xmlService.doDiff(fileChangedHtml, elementToIdentify.get());
                }else{
                    System.out.println("No element were found");
                }

            }
        }else{
            System.out.println("Invalid file");
        }

    }


}

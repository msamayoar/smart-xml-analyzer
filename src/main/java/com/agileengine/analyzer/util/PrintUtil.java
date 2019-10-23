package com.agileengine.analyzer.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public final class PrintUtil {

    private PrintUtil(){}

    public static void printXpathElement(Element e){
        if( e == null){
            return;
        }else{
            printXpathElement(e.parent());
            System.out.print(e.tagName() + " > ");
        }

    }

    public static void printDiff(Element srcE, Element diffE){
        System.out.println("");
        for(Attribute a: srcE.attributes()){
            String diffVal = diffE.attributes().get(a.getKey());

            if(StringUtils.isNotEmpty(diffVal) && diffVal.compareTo(a.getValue()) !=0 ){
                System.out.println("diff >>>>" + a.getKey() + "=\""  + diffE.attributes().get(a.getKey()) +"\"" );
            }
        }

    }

}

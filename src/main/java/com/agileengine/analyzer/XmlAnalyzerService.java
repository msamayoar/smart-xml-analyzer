package com.agileengine.analyzer;

import org.jsoup.nodes.Element;

import java.io.File;
import java.util.Optional;

public interface XmlAnalyzerService {

    public File getHtmlResource(String fileResource);

    public Optional<Element> getElementByIf(File htmlFile, String element);

    public void doDiff(File htmlDiff, Element mainElement);

}

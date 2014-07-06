package org.kt3k.gradle.plugin.coveralls.domain

class XmlParserFactory {

  def getXmlParser() {
    // create parser
    XmlParser parser = new XmlParser()

    // skip external DTD validation
    // see http://xerces.apache.org/xerces2-j/features.html#nonvalidating.load-external-dtd
    parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)

    // allow DOCTYPE declaration
    // see http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
    // this is the same as the default, but in some situation it seems to turn to be true ( see https://github.com/kt3k/coveralls-gradle-plugin/pull/16 )
    parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)

    return parser
  }
}

package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test

import static org.junit.Assert.assertFalse

class XmlParserFactoryTest {

  @Test
  void testXmlParserConfiguration() {
    def parser = new XmlParserFactory().xmlParser

    assertFalse parser.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd")
    assertFalse parser.getFeature("http://apache.org/xml/features/disallow-doctype-decl")
  }
}

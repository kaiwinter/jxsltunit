# jxsltUnit
[![Build Status](https://api.travis-ci.org/kaiwinter/jxsltunit.svg)](https://travis-ci.org/kaiwinter/jxsltunit)
[![Coverage Status](https://coveralls.io/repos/kaiwinter/jxsltunit/badge.svg?branch=master&service=github)](https://coveralls.io/github/kaiwinter/jxsltunit?branch=master)

Unit tests for XSL Transformations.

## How to
Call the main class with a test definition XML:
`java -jar xsltunit -config testsuite-multiple-testcases.xml`

This is how the test definition looks like:
```xml
<xsltTestsuite xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="jxsltunit jxslttestsuite.xsd" xmlns="jxsltunit"
	description="Testsuite Test"
	xml="min-test.xml"
	xslt="min-test.xslt"
	path="pa > ch">
	<xsltTestcase match_number="0">
		<![CDATA[<ch>child 1</ch>]]>
	</xsltTestcase>
	<xsltTestcase match_number="1">
		<![CDATA[<ch>child 2</ch>]]>
	</xsltTestcase>
</xsltTestsuite>
```
A `xsltTestsuite` defines the `xml` and `xslt` attributes to make jxsltunit do the transformation.
The `path` attribute is a pseudo path similar to xpath used by jsoup to identify the node in the target XML which is to be tested.
The element `xsltTestcase` contains the data which is expected in the target XML on the `path`.
As the target node can contain a list of elements use `match_number` to test against specific elements in the list.
_Note_: Because of technical reasons `<ch>` must be contained in the matching rule. 

To understand the above test XML, this is the input `min-test.xml`:
```xml
<parent>
	<child>child 1</child>
	<child>child 2</child>
</parent>
``` 

And this the `min-test.xslt` XSLT (it transforms the `parent` tags to `pa` and `child` to `ch`):
```xml
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	version="2.0">
	<xsl:template match="/">
			<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="parent">
		<pa>
			<xsl:for-each select="child">
				<ch>
					<xsl:value-of select="text()" />
				</ch>
			</xsl:for-each>
		</pa>
	</xsl:template>
</xsl:stylesheet>
```

The `<xsltTestcase match_number="0">` expects `child 1` on the path `pa/ch[0]` and `<xsltTestcase match_number="1">` expects `child 2` on `pa/ch[1]`. Pretty easy:
```xml
<pa>
	<ch>child 1</ch>
	<ch>child 2</ch>
</pa>
```

## Output
If not set the result is printed as plain text to standard out:
```
Testsuite 'Testsuite Test'
- Match number: 1 ... OK
- Match number: 2 ... OK
``` 
This can be changed to junit XML format by calling jxsltunit with `-format xml` (Eclipse and Jenkins can interpret those XML files like junit results!):
```xml
<testsuites>
    <testsuite name="Testsuite Test">
        <testcase name="Match number: 1"/>
        <testcase name="Match number: 2"/>
    </testsuite>
</testsuites>
```
To write the results to a file pass the parameter `-outfile <junit-out.xml>`.

## Jenkins integration
In Jenkins you can add a post-build step to call jxsltunit to continuously test your XSLT files.
Output the results to an XML file and let Jenkins collect those to make them show up in your unit test results in Jenkins.

## Group testsuites
In addition to the above example multiple `xsltTestsuite`s can be bundled in one `xsltTestsuites` element.
Also the junit XML result will be group in the same way.
```xml
<xsltTestsuites xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="jxsltunit jxslttestsuite.xsd" xmlns="jxsltunit">

	<xsltTestsuite description="Testsuite Test - 1"
		xml="min-test.xml"
		xslt="min-test.xslt"
		path="pa > ch">
		<xsltTestcase match_number="1">
			<![CDATA[<ch>child 2</ch>]]>
		</xsltTestcase>
	</xsltTestsuite>
	
	<xsltTestsuite description="Testsuite Test - 2"
		xml="min-test.xml"
		xslt="min-test.xslt"
		path="pa > ch">
		<xsltTestcase match_number="0">
			<![CDATA[<ch>child 1</ch>]]>
		</xsltTestcase>
	</xsltTestsuite>

</xsltTestsuites>
```

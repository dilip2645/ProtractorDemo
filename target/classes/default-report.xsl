<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java" xmlns:str="http://exslt.org/strings"
	exclude-result-prefixes="fo">
	<xsl:key name="excludeHeader" match="ReportData/excludeFields/*"
		use="." />
	<xsl:variable name="headcount" select="count(ReportData/headers/*)" />
	<xsl:variable name="pagewidth">
		<xsl:choose>
			<xsl:when test="$headcount &gt; 9">
				29.7cm
			</xsl:when>
			<xsl:otherwise>
				21cm
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="pageheight">
		<xsl:choose>
			<xsl:when test="$headcount &gt; 9">
				21cm
			</xsl:when>
			<xsl:otherwise>
				29.7cm
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="{$pageheight}" page-width="{$pagewidth}" margin-top="0.5cm"
					margin-left="0.7cm" margin-right="0.7cm" padding-right="0.3cm">
					<fo:region-body region-name="xsl-region-body"
						margin-top="2.2cm" margin-bottom=".7cm" />
					<fo:region-before region-name="xsl-region-before"
						extent="5in" />
					<fo:region-after region-name="xsl-region-after"
						extent=".5in" />
				</fo:simple-page-master>

			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block>
						<fo:table table-layout="fixed">
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell width="5cm">
										<fo:block>
											<xsl:for-each select="ReportData/logo">
												<xsl:variable name="logo">
													<xsl:value-of select="normalize-space(.)" />
												</xsl:variable>
												<fo:external-graphic content-height="scale-to-fit"
													height="0.70in" width="1.90in" content-width="scale-to-fit"
													scaling="non-uniform" src='{$logo}' />
											</xsl:for-each>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell display-align="center" text-align="center">
										<fo:block font-size="14pt" font-weight="bold">
											<xsl:for-each select="ReportData/reportTitle">
												<xsl:value-of select="." /> 
											</xsl:for-each>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell width="4.5cm" display-align="center">
										<fo:block font-size="10pt" font-weight="normal"
											text-align="end">
											Date:
											<xsl:value-of
												select="java:format(java:java.text.SimpleDateFormat.new('MM/dd/yyyy hh:mm a'), java:java.util.Date.new())" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:static-content>
				<fo:static-content flow-name="xsl-region-after">
					<fo:block>
						<fo:leader leader-length="100%" leader-pattern="rule"
							rule-style="solid" rule-thickness="0.9px" />
					</fo:block>
					<fo:block>
						<fo:block font-size="10pt" font-weight="normal"
							text-align="center">
							Page
							<fo:page-number />
							of
							<fo:page-number-citation ref-id="lastPage" />
						</fo:block>
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<xsl:choose>
						<xsl:when test="count(ReportData/data/*)=0">
							<fo:block font-size="16pt" text-align="center" color="#ff3333"
								padding-top="4cm">
								...No Data found...
							</fo:block>
						</xsl:when>
						<xsl:otherwise>
							<fo:block font-size="8pt">
								<fo:table table-layout="fixed" width="100%"
									border-width="2px" border-separation="1pt" border-collapse="separate">
									<fo:table-header font-size="10pt"
										margin-bottom="2cm">
										<xsl:for-each select="ReportData/headers/*">
											<fo:table-cell padding-top="0.3cm"
												padding-bottom="0.3cm" background-color="#1B3A6B">
												<fo:block font-weight="bold" text-align="center"
													color="white">
													<xsl:value-of select="." />
												</fo:block>
											</fo:table-cell>
										</xsl:for-each>
									</fo:table-header>

									<fo:table-body>
										<xsl:for-each select="ReportData/data/*">
											<xsl:variable name="bgclr">
												<xsl:choose>
													<xsl:when test="position() mod 2">
														#e2edf7
													</xsl:when>
													<xsl:otherwise>
														#FFFFFF
													</xsl:otherwise>
												</xsl:choose>
											</xsl:variable>
											<fo:table-row height="1cm" background-color="{$bgclr}">
												<xsl:for-each select="*">
													<xsl:if test="not(key('excludeHeader',name()))">
														<fo:table-cell display-align="center"
															text-align="center">
															<fo:block font-weight="normal">
																<xsl:value-of select="." />
															</fo:block>
														</fo:table-cell>
													</xsl:if>
												</xsl:for-each>
											</fo:table-row>
										</xsl:for-each>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</xsl:otherwise>
					</xsl:choose>
					<fo:block id="lastPage">
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
/**
 * Copyright 2005-2018 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.xml.xstream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Evaluates simple XPath expressions to follow paths through a document generated by XStream which uses
 * "reference" elements to handle circular and duplicate references.  For example, an XML document 
 * generated from XStream might look like the following:
 * 
 * <pre><test>
 *   <a>hello</a>
 *   <b>
 *     <a reference="../../a"/>
 *   </b>
 * </test></pre>
 * 
 * <p>In the above case, the XPath expression /test/a would result in the "hello" text but the 
 * XPath expression /test/b/a would result in the empty string.  However, if the evaluator below is mapped
 * as an XPath function, than it could be used as follows on the second expression to produce the desired result of "hello": 
 * xstreamsafe('/test/b/a', root())
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XStreamSafeEvaluator {
	
	private static final String MATCH_ANY = "//";
	private static final String MATCH_ROOT = "/";
	private static final String MATCH_CURRENT = ".";
	private static final String XSTREAM_REFERENCE_ATTRIBUTE = "reference";
	private XPath xpath;

	public XStreamSafeEvaluator() {}
	
	public XStreamSafeEvaluator(XPath xpath) {
		this.xpath = xpath;
	}
	/**
	 * Evaluates the given XPath expression against the given Node while following reference attributes
	 * on Nodes in a way which is compatible with the XStream library.
	 *
	 * @throws XPathExpressionException if there was a problem evaluation the XPath expression.
	 */
	public NodeList evaluate(String xPathExpression, Node rootSearchNode) throws XPathExpressionException {
		XPath xpathEval = this.getXpath();
		List segments = new ArrayList();
		parseExpression(segments, xPathExpression, true);
		SimpleNodeList nodes = new SimpleNodeList();
		nodes.getList().add(rootSearchNode);
		for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
			SimpleNodeList newNodeList = new SimpleNodeList();
			XPathSegment expression = (XPathSegment) iterator.next();
			for (Iterator nodeIterator = nodes.getList().iterator(); nodeIterator.hasNext();) {
				Node node = (Node)nodeIterator.next();
				node = resolveNodeReference(xpathEval, node);
				if (node != null) {
					NodeList evalSet = (NodeList)xpathEval.evaluate(expression.getXPathExpression(), node, XPathConstants.NODESET);
					if (evalSet != null) {
						for (int nodeIndex = 0; nodeIndex < evalSet.getLength(); nodeIndex++) {
							Node newNode = evalSet.item(nodeIndex);
							newNodeList.getList().add(newNode);
						}
					}
				}
			}
			nodes = newNodeList;
		}
		// now, after we've reached "the end of the line" check our leaf nodes and resolve any XStream references on them
		// TODO I noticed that the original implementation of this method was not doing the following work so I'm just tacking it on the end, there's
		// probably a more elegent way to integrate it with the algorithm above...
		SimpleNodeList newNodes = new SimpleNodeList();
		for (Iterator iterator = nodes.getList().iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			newNodes.getList().add(resolveNodeReference(xpathEval, node));
		}
		return newNodes;
	}
	
	/**
	 * Parses the given XPath expression into a List of segments which can be evaluated in order.
	 */
	private void parseExpression(List segments, String xPathExpression, boolean isInitialSegment) throws XPathExpressionException {
		if (StringUtils.isEmpty(xPathExpression)) {
			return;
		}
		XPathSegment segment = isInitialSegment ? parseInitialSegment(xPathExpression) : parseNextSegment(xPathExpression);
		segments.add(segment);
		parseExpression(segments, xPathExpression.substring(segment.getLength()), false);
	}

	
//	private XPathSegment parseNextSegment(String xPathExpression) throws XPathExpressionException {
//		int operatorLength = 2;
//		int firstIndex = xPathExpression.indexOf(MATCH_ANY);
//		if (firstIndex != 0) {
//			firstIndex = xPathExpression.indexOf(MATCH_CURRENT);
//			if (firstIndex != 0) {
//				operatorLength = 1;
//				firstIndex = xPathExpression.indexOf(MATCH_ROOT);				
//			}
//		}
//		// the operator should be at the beginning of the string
//		if (firstIndex != 0) {
//			throw new XPathExpressionException("Could not locate an appropriate ./, /, or // operator at the begginingg of the xpath segment: " + xPathExpression);
//		}
//		int nextIndex = xPathExpression.indexOf(MATCH_ANY, operatorLength);
//		if (nextIndex == -1) {
//			nextIndex = xPathExpression.indexOf(MATCH_ROOT, operatorLength);
//		}
//		if (nextIndex == -1) {
//			nextIndex = xPathExpression.length();
//		}
//		return new XPathSegment(xPathExpression.substring(0,operatorLength),
//				xPathExpression.substring(operatorLength, nextIndex));
//	}
	
	/**
	 * Parses the next segment of the given XPath expression by grabbing the first
	 * segment off of the given xpath expression.  The given xpath expression must
	 * start with either ./, /, or // otherwise an XPathExpressionException is thrown.
	 */
	private XPathSegment parseInitialSegment(String xPathExpression) throws XPathExpressionException {
		// TODO we currently can't support expressions that start with .//
		if (xPathExpression.startsWith(MATCH_CURRENT+MATCH_ANY)) {
			throw new XPathExpressionException("XStream safe evaluator currenlty does not support expressions that start with " +MATCH_CURRENT+MATCH_ANY);
		}
		//int operatorLength = 3;
		//int firstIndex = xPathExpression.indexOf(MATCH_CURRENT+MATCH_ANY);
		//if (firstIndex != 0) {
			int operatorLength = 2;
			int firstIndex = xPathExpression.indexOf(MATCH_CURRENT+MATCH_ROOT);
			if (firstIndex != 0) {
				firstIndex = xPathExpression.indexOf(MATCH_ANY);
				if (firstIndex != 0) {
					operatorLength = 1;
					firstIndex = xPathExpression.indexOf(MATCH_ROOT);
				}
			}
		//}
		// the operator should be at the beginning of the string
		if (firstIndex != 0) {
			throw new XPathExpressionException("Could not locate an appropriate ./, /, or // operator at the begginingg of the xpath segment: " + xPathExpression);
		}
		int nextIndex = xPathExpression.indexOf(MATCH_ROOT, operatorLength);
		if (nextIndex == -1) {
			nextIndex = xPathExpression.length();
		}
		return new XPathSegment(xPathExpression.substring(0, operatorLength),
				xPathExpression.substring(operatorLength, nextIndex), true);
	}

	/**
	 * Parses the next segment of the given XPath expression by grabbing the first
	 * segment off of the given xpath expression.  The given xpath expression must
	 * start with / otherwise an XPathExpressionException is thrown.  This is because
	 * the "next" segments represent the internal pieces in an XPath expression.
	 */
	private XPathSegment parseNextSegment(String xPathExpression) throws XPathExpressionException {
		if (!xPathExpression.startsWith(MATCH_ROOT)) {
			throw new XPathExpressionException("Illegal xPath segment, the given segment is not a valid segment and should start with a '"+MATCH_ROOT+"'.  Value was: " + xPathExpression);
		}
		int operatorLength = MATCH_ROOT.length();
		int nextIndex = xPathExpression.indexOf(MATCH_ROOT, operatorLength);
		if (nextIndex == -1) {
			nextIndex = xPathExpression.length();
		}
		return new XPathSegment(MATCH_CURRENT+MATCH_ROOT, xPathExpression.substring(operatorLength, nextIndex), false);
	}
	
	/**
	 * Resolves the reference to a Node by checking for a "reference" attribute and returning the resolved node if
	 * it's there.  The resolution happens by grabbing the value of the reference and evaluation it as an XPath
	 * expression against the given Node.  If there is no reference attribute, the node passed in is returned.
	 * The method is recursive in the fact that it will continue to follow XStream "reference" attributes until it
	 * reaches a resolved node.
	 */
	private Node resolveNodeReference(XPath xpath, Node node) throws XPathExpressionException{
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			Node referenceNode = attributes.getNamedItem(XSTREAM_REFERENCE_ATTRIBUTE);
			if (referenceNode != null) {
				node = (Node)xpath.evaluate(referenceNode.getNodeValue(), node, XPathConstants.NODE);
				if (node != null) {
					node = resolveNodeReference(xpath, node);
				} else {
					throw new XPathExpressionException("Could not locate the node for the given XStream references expression: '" + referenceNode.getNodeValue() + "'");
				}
			}
		}
		return node;
	}

	/**
	 * A single segment of an XPath expression.
	 */
	private class XPathSegment {
		private final String operator;
		private final String value;
		private final boolean isInitialSegment;
		public XPathSegment(String operator, String value, boolean isInitialSegment) {
			this.operator = operator;
			this.value = value;
			// if it's not an initial segment then a '.' will preceed the operator and should not be counted in the length
			this.isInitialSegment = isInitialSegment;
		}
		public int getLength() {
			// if it's not an initial segment then a '.' will preceed the operator and should not be counted in the length
			if (!isInitialSegment) {
				return operator.length() + value.length() - 1;
			}
			return operator.length() + value.length();
		}
		/**
		 * Returns an XPath expression which can be evaluated in the context of the 
		 * node returned by the previously executed segment.
		 */
		public String getXPathExpression() {
			return operator+value;
		}
	}
	
	/**
	 * A simple NodeList implementation, as simple as it gets.  This allows us to not be tied to
	 * any particular XML service provider's NodeList implementation.
	 */
	private class SimpleNodeList implements NodeList {
		private List nodes = new ArrayList();
		public Node item(int index) {
			return (Node)nodes.get(index);
		}
		public int getLength() {
			return nodes.size();
		}
		public List getList() {
			return nodes;
		}
	}
	
	public XPath getXpath() {
		if (this.xpath == null) {
			return XPathHelper.newXPath();
		}
		return xpath;
	}

	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}

}

package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();
	final static Deque<String> parenthese  = new ArrayDeque<String>();
	final static boolean isPhilosophy = false;
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
	 * 2. Ignoring external links, links to the current page, or red links
	 * 3. Stopping when reaching "Philosophy", a page with no links or a page
	 *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static String firstLink (String url) throws IOException{
		Elements paragraphs = wf.fetchWikipedia(url);
		Element firstPara = paragraphs.get(0);

		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		for (Node node: iter) {
			if (node instanceof TextNode) {
				//System.out.print(node);
				String word = node.toString();
				//check if node is parentheses
				if (word.contains("(")){
					parenthese.push("(");
				}else if (word.contains(")")){
					parenthese.pop();
				}
			}if(node instanceof Element){
				Element e = (Element) node;
				if (isValid(e)){
					return e.attr("abs:href");
				}
			}
		}
		return null;
	}

	public static boolean isValid(Element e){
		if (!e.tagName().equals("a")) {
			return false;
		}
		// in italics
		Element current = e;
		while(current.parent() !=null){
			if (current.tagName().equals("i") || current.tagName().equals("em")){
				return false;
			}
			current = current.parent();
		}
		// in parenthesis
		if (!parenthese.isEmpty()){
			return false;
		}return true;
	}


	public static void main(String[] args) throws IOException {
		// some example code to get you started
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

		while (!url.equals("https://en.wikipedia.org/wiki/Philosophy")){
			if (url==null || url.equals("")){
				System.out.println("No valid links!");
			}else {
				System.out.println(url);
				url = firstLink(url);
			}
		}
		if(url.equals("https://en.wikipedia.org/wiki/Philosophy")){
			System.out.println("Philosphy page found");
		}else {
			System.out.println("Philosphy page not found");
		}
	}
}

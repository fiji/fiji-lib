/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2009 - 2021 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitString {
	/**
	 * Split a command line into single parts, respecting quoted
	 * arguments.
	 *
	 * Example:
	 *
	 *   hello "this 'is'" 'an example'
	 *
	 * would be split into the three parts
	 *
	 *   hello
	 *   this 'is'
	 *   an example
	 *
	 * In other words, it respects quoting inside quoted arguments.
	 */
	public static List<String> splitCommandLine(String commandLine) throws ParseException {
		List<String> result = new ArrayList<String>();
		if (commandLine == null)
			return result;
		int len = commandLine.length();
		StringBuffer current = new StringBuffer();

		for (int i = 0; i < len; i++) {
			char c = commandLine.charAt(i);
			if (isQuote(c)) {
				int i2 = findClosingQuote(commandLine, c, i + 1, len);
				current.append(commandLine.substring(i + 1, i2));
				i = i2;
				continue;
			}
			if (Character.isWhitespace(c)) {
				if (current.length() == 0)
					continue;
				result.add(current.toString());
				current.setLength(0);
			} else
				current.append(c);
		}
		if (current.length() > 0)
			result.add(current.toString());
		return result;
	}

	protected static int findClosingQuote(String s, char quote, int index, int len) throws ParseException {
		for (int i = index; i < len; i++) {
			char c = s.charAt(i);
			if (c == quote)
				return i;
			if (isQuote(c))
				i = findClosingQuote(s, c, i + 1, len);
		}
		throw new ParseException("Unclosed quote: " + s, index);
	}

	protected static boolean isQuote(char c) {
		return c == '"' || c == '\'';
	}

	/**
	 * Split a macro-type option string into single parts, respecting brackets.
	 * <p>
	 * Example:
	 * </p>
	 * <pre>
	 *   path=[C:\Documents and Settings\ImageJ\Desktop\My Beautiful Image.jpg] radius=5
	 * </pre>
	 * <p>
	 * would be split into the two parts
	 * </p>
	 * <pre>
	 *   path -&gt; C:\Documents and Settings\ImageJ\Desktop\My Beautiful Image.jpg
	 *   radius -&gt; 5
	 * </pre>
	 * <p>
	 * In other words, it splits by white space, however it keeps arguments intact that
	 * are enclosed in brackets.
	 * </p>
	 */
	public static Map<String, String> splitMacroOptions(String options) throws ParseException {
		Map<String, String> result = new HashMap<String, String>();
		int len = options.length();
		StringBuffer current = new StringBuffer();

		for (int i = 0; i < len; i++) {
			char c = options.charAt(i);
			if (c == '[') {
				int i2 = i + 1;
				while (i2 < len && options.charAt(i2) != ']')
					i2++;
				current.append(options.substring(i + 1, i2));
				i = i2;
				continue;
			}
			if (Character.isWhitespace(c)) {
				if (current.length() == 0)
					continue;
				putPair(result, current.toString());
				current.setLength(0);
				while (i + 1 < len && Character.isWhitespace(options.charAt(i + 1)))
					i++;
			} else
				current.append(c);
		}
		if (current.length() > 0)
			putPair(result, current.toString());
		return result;
	}

	protected static void putPair(Map<String, String> map, String arg) throws ParseException {
		int equal = arg.indexOf('=');
		if (equal < 0)
			throw new ParseException("Missing '=': " + arg, 0);
		map.put(arg.substring(0, equal), arg.substring(equal + 1));
	}

	public static void main(String[] args) {
		if (args == null || args.length == 0 || (args.length == 1 && args[0].equals("")))
			args = new String[] { "path=[C:\\Documents and Settings\\ImageJ\\Desktop\\My Beautiful Image.jpg] radius=5" };
		for (String arg : args) try {
			Map<String, String> map = splitMacroOptions(arg);
			System.out.println("The string " + arg + " is split into:");
			for (String key : map.keySet())
				System.out.println("\t" + key + " -> " + map.get(key));
			System.out.println("");
		} catch (ParseException e) {
			System.err.println("There was a parse exception for " + arg + ": " + e.getMessage());
		}
	}
}

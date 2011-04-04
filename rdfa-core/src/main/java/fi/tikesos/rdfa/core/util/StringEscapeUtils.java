/**
 * Copyright (C) 2011 ssakorho <sami.s.korhonen@uef.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fi.tikesos.rdfa.core.util;

public class StringEscapeUtils {
	public static void escapeXML(char []input, int start, int length, StringBuffer output) {
		for (int i = 0;i < length;i++) {
			char c = input[start + i];
			switch (c) {
			case '<':
				// &lt;
				output.append("&lt;");
				break;
			case '>':
				// &gt;
				output.append("&gt;");
				break;
			case '"':
				// &quot;
				output.append("&quot;");
				break;
			case '&':
				// &amp;
				output.append("&amp;");
				break;
			case '\'':
				// &apos;
				output.append("&apos;");
				break;
			default:
				// others
				output.append(c);
				break;
			}
		}
	}
	
	public static String escapeXML(char []input, int start, int length) {
		StringBuffer output = new StringBuffer(length + 32);
		return output.toString();
	}
	
	public static void escapeXML(String input, StringBuffer output) {
		for (int i = 0;i < input.length();i++) {
			char c = input.charAt(i);
			switch (c) {
			case '<':
				// &lt;
				output.append("&lt;");
				break;
			case '>':
				// &gt;
				output.append("&gt;");
				break;
			case '"':
				// &quot;
				output.append("&quot;");
				break;
			case '&':
				// &amp;
				output.append("&amp;");
				break;
			case '\'':
				// &apos;
				output.append("&apos;");
				break;
			default:
				// others
				output.append(c);
				break;
			}
		}
	}
	
	public static String escapeXML(String input) {
		StringBuffer output = new StringBuffer(input.length() + 32);
		escapeXML(input, output);
		return output.toString();
	}
}

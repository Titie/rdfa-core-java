/**
 * Copyright (C) 2011 ssakorho <sami.s.korhonen@uef.fi>
 *
 * Licensed under the GNU Lesser General Public Licence, Version 3
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *         http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.tikesos.rdfa.core.exception;

import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;

public class TripleErrorHandler implements ErrorHandler {
	public final String RDFA_PROCESSOR_NS = "http://tikesos.fi/processor/rdfa#";
	public final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private long exceptionCount;
	private TripleSink tripleSink;

	public TripleErrorHandler(TripleSink tripleSink) {
		this.tripleSink = tripleSink;
		this.exceptionCount = 1;
	}

	private void generateRDFaExceptionTriple(String exceptionType,
			RDFaException exception) {
		String subject = RDFA_PROCESSOR_NS + "exception_" + exceptionCount;
		tripleSink.generateTriple(subject, RDF_NS + "type", RDFA_PROCESSOR_NS + exceptionType);
		tripleSink.generateTriple(subject, RDFA_PROCESSOR_NS + "type", RDFA_PROCESSOR_NS + exception.getClass().getSimpleName());
		tripleSink.generateTripleLiteral(subject, RDFA_PROCESSOR_NS + "elementName", exception.getElementName(), null, null);
//		tripleSink.generateTripleLiteral(subject, RDFA_PROCESSOR_NS + "message", exception.getMessage(), null, null);
//		tripleSink.generateTripleLiteral(subject, RDFA_PROCESSOR_NS + "lineNumber", String.valueOf(exception.getLine()), null, null);
//		tripleSink.generateTripleLiteral(subject, RDFA_PROCESSOR_NS + "columnNumber", String.valueOf(exception.getColumn()), null, null);
		this.exceptionCount++;
	}

	@Override
	public void warning(Exception exception) {
		if (exception instanceof RDFaException) {
			generateRDFaExceptionTriple("warning", (RDFaException) exception);
		}
	}

	@Override
	public void fatalError(Exception exception) {
		if (exception instanceof RDFaException) {
			generateRDFaExceptionTriple("fatalError", (RDFaException) exception);
		}
	}
}

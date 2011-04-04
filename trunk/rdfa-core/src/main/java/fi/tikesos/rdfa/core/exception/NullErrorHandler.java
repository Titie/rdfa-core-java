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

package fi.tikesos.rdfa.core.exception;

/**
 * NullErrorHandler does absolutely nothing.
 * 
 * @author ssakorho
 *
 */
public class NullErrorHandler implements ErrorHandler {
	public void warning(Exception exception) {
		// Does absolutely nothing!
	}

	public void fatalError(Exception exception) {
		// Does absolutely nothing!
	}
}

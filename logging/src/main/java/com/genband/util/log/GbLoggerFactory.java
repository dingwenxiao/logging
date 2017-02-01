/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.genband.util.log;

import org.modeshape.common.logging.LogFactory;
import org.modeshape.common.logging.Logger;

/**
 * To create a custom ModeShape logger, create a class in the <code>org.modeshape.common.logging</code> package that is named
 * {@link GbLoggerFactory} and extends {@link LogFactory}. It should create your own implementation of {@link Logger}.
 */
public class GbLoggerFactory extends LogFactory {

    @Override
    protected Logger getLogger( String name ) {
        return new GbLogger(name);
    }

}

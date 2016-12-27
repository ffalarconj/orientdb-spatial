/**
 * Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * For more information: http://www.orientdb.com
 */
package com.orientechnologies.spatial.functions;

import com.orientechnologies.common.exception.OException;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.sql.functions.OSQLFunction;
import com.orientechnologies.orient.core.sql.functions.OSQLFunctionAbstract;
import com.orientechnologies.orient.core.sql.functions.OSQLFunctionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OSpatialFunctionsFactory implements OSQLFunctionFactory {

  public static final Map<String, Object> FUNCTIONS = new HashMap<String, Object>();

  static {
    register(new OSTGeomFromTextFunction());
    register(new OSTAsTextFunction());
    register(new OSTWithinFunction());
    register(new OSTDWithinFunction());
    register(new OSTEqualsFunction());
    register(new OSTAsBinaryFunction());
    register(new OSTEnvelopFunction());
    register(new OSTBufferFunction());
    register(new OSTDistanceFunction());
    register(new OSTDistanceSphereFunction());
    register(new OSTDisjointFunction());
    register(new OSTIntersectsFunction());
    register(new OSTContainsFunction());
    register(new OSTSrid());

  }

  private static void register(final OSQLFunctionAbstract function) {
    FUNCTIONS.put(function.getName().toLowerCase(), function);
  }

  @Override
  public boolean hasFunction(String iName) {
    return FUNCTIONS.containsKey(iName);
  }

  @Override
  public Set<String> getFunctionNames() {
    return FUNCTIONS.keySet();
  }

  @Override
  public OSQLFunction createFunction(String name) throws OCommandExecutionException {
    final Object obj = FUNCTIONS.get(name);

    if (obj == null) {
      throw new OCommandExecutionException("Unknown function name :" + name);
    }

    if (obj instanceof OSQLFunction) {
      return (OSQLFunction) obj;
    } else {
      // it's a class
      final Class<?> clazz = (Class<?>) obj;
      try {
        return (OSQLFunction) clazz.newInstance();
      } catch (Exception e) {
        throw OException.wrapException(new OCommandExecutionException("Error in creation of function " + name
            + "(). Probably there is not an empty constructor or the constructor generates errors"), e);
      }
    }

  }

  public Map<String, Object> getFunctions() {
    return FUNCTIONS;
  }
}

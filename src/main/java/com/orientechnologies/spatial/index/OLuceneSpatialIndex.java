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
package com.orientechnologies.spatial.index;

import com.orientechnologies.lucene.index.OLuceneIndexNotUnique;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.storage.impl.local.OAbstractPaginatedStorage;
import com.orientechnologies.spatial.shape.OShapeFactory;
import com.vividsolutions.jts.geom.Geometry;
import org.locationtech.spatial4j.shape.Shape;

public class OLuceneSpatialIndex extends OLuceneIndexNotUnique {

  OShapeFactory shapeFactory = OShapeFactory.INSTANCE;

  public OLuceneSpatialIndex(String name, String typeId, String algorithm, int version, OAbstractPaginatedStorage storage,
      String valueContainerAlgorithm, ODocument metadata) {
    super(name, typeId, algorithm, version, storage, valueContainerAlgorithm, metadata);

  }

  @Override
  public OLuceneIndexNotUnique put(Object key, OIdentifiable singleValue) {
    if (key == null) {
      return this;
    }
    return super.put(key, singleValue);
  }

  @Override
  protected Object encodeKey(Object key) {

    if (key instanceof ODocument) {
      Shape shape = shapeFactory.fromDoc((ODocument) key);
      return shapeFactory.toGeometry(shape);
    }
    return key;
  }

  @Override
  protected Object decodeKey(Object key) {

    if (key instanceof Geometry) {
      Geometry geom = (Geometry) key;
      return shapeFactory.toDoc(geom);
    }
    return key;
  }
}

package org.connector.api;

import java.util.List;

public interface ServerInterface {


    String getInstanceMetaInfo();

    List<String> getAllDbs();
}

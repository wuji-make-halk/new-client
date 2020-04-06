package com.weimingfj.liner.cache;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weimingfj.common.cache.ICacheService;

@Service("systemPropertiesCache")
public class SystemPropertiesCache implements ICacheService {
    
    Logger log = LoggerFactory.getLogger(SystemPropertiesCache.class);
    
    Map<String, String> props = new HashMap<String, String>();
    
    @Override
    public Map<String, String> getCacheContext() {
        String fileName = "system.properties";
        File syspropFile = null;
        
        String classesDir = SystemPropertiesCache.class.getClassLoader()
                .getResource("").toString().replaceAll("^file:", "");
        log.debug("classesDir = " + classesDir);
        syspropFile = new File(classesDir + fileName);
        
        if (!syspropFile.exists()) {
            String webinfDir = classesDir.replaceAll("classes/$", "");
            log.debug("webinfDir = " + webinfDir);
            syspropFile = new File(webinfDir + "conf/" + fileName);
        }
        
        if (!syspropFile.exists()) {
            log.debug("system.properties isn't finded. ");
        } else {
            log.debug("system.properties is finded. path = " + syspropFile.getAbsolutePath());
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(syspropFile));
                if (!p.isEmpty()) {
                    for (Iterator<Object> it = p.keySet().iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        String val = p.getProperty(key);
                        props.put(key, val);
                        log.debug("system.properties loaded. key = " + key + ", val = " + val);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    @Override
    public long getCacheLiveTime() {
        return 0;
    }

    public Map<String, String> getProps() {
        return props;
    }

}
